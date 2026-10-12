
package services;

import models.Employee;
import models.PayrollRecord;

import dsa.CustomHashTable;
import dsa.QuickSort;
import dsa.BinarySearch;

import java.util.Comparator;

public class PayrollService {

    // Stores monthly payroll records
    private PayrollRecord[] payrollRecords;
    private int count;

    // Employee lookup
    private CustomHashTable<Integer, Employee> employeeTable;

    // Constructor
    public PayrollService(
            CustomHashTable<Integer, Employee> employeeTable) {

        this.employeeTable = employeeTable;

        // 30 employees * 3 months = 90 initial records
        // Extra capacity for future records
        payrollRecords = new PayrollRecord[200];

        count = 0;
    }

    // Add an existing payroll record
    public boolean addPayrollRecord(PayrollRecord record) {

        if (record == null || count >= payrollRecords.length) {
            return false;
        }

        if (record.getMonth() < 1 || record.getMonth() > 12
                || record.getYear() <= 0) {
            return false;
        }

        if (employeeTable.get(record.getEmployeeId()) == null) {
            return false;
        }

        // Prevent duplicate payroll records for the same month
        if (getPayrollRecord(
                record.getEmployeeId(),
                record.getMonth(),
                record.getYear()) != null) {

            return false;
        }

        payrollRecords[count] = record;
        count++;

        return true;
    }

    // Generate a new monthly payroll record
    public boolean generatePayroll(int employeeId,
            int month, int year) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return false;
        }

        if (month < 1 || month > 12 || year <= 0) {
            System.out.println("Invalid month or year.");
            return false;
        }

        if (getPayrollRecord(employeeId, month, year) != null) {
            System.out.println(
                    "Payroll already exists for this month.");
            return false;
        }

        PayrollRecord record = new PayrollRecord(
                employeeId,
                month,
                year,
                employee.getBasicSalary());

        if (!addPayrollRecord(record)) {
            System.out.println("Unable to generate payroll.");
            return false;
        }

        System.out.println("Payroll generated successfully.");
        return true;
    }

    // Get all stored payroll records
    public PayrollRecord[] getAllPayrollRecords() {

        PayrollRecord[] result = new PayrollRecord[count];

        for (int i = 0; i < count; i++) {
            result[i] = payrollRecords[i];
        }

        return result;
    }

    // Comparator: Employee ID -> Year -> Month
    private Comparator<PayrollRecord> getPayrollComparator() {

        return (a, b) -> {

            int idComparison = Integer.compare(
                    a.getEmployeeId(),
                    b.getEmployeeId());

            if (idComparison != 0) {
                return idComparison;
            }

            int yearComparison = Integer.compare(
                    a.getYear(),
                    b.getYear());

            if (yearComparison != 0) {
                return yearComparison;
            }

            return Integer.compare(
                    a.getMonth(),
                    b.getMonth());
        };
    }

    // Binary Search for a specific employee's monthly payroll
    public PayrollRecord getPayrollRecord(int employeeId,
            int month, int year) {

        if (month < 1 || month > 12 || year <= 0) {
            return null;
        }

        PayrollRecord[] sorted = getAllPayrollRecords();

        Comparator<PayrollRecord> comparator = getPayrollComparator();

        // Binary Search requires sorted records
        QuickSort.sort(sorted, comparator);

        // Search key: employee ID, year, and month.
        // Salary is irrelevant to the comparison.
        PayrollRecord target = new PayrollRecord(
                employeeId,
                month,
                year,
                0);

        int index = BinarySearch.search(
                sorted,
                target,
                comparator);

        if (index == -1) {
            return null;
        }

        return sorted[index];
    }

    // Get all monthly payroll records for one employee
    public PayrollRecord[] getEmployeePayroll(int employeeId) {

        int matchingCount = 0;

        for (int i = 0; i < count; i++) {

            if (payrollRecords[i].getEmployeeId() == employeeId) {
                matchingCount++;
            }
        }

        PayrollRecord[] result = new PayrollRecord[matchingCount];

        int index = 0;

        for (int i = 0; i < count; i++) {

            if (payrollRecords[i].getEmployeeId() == employeeId) {

                result[index] = payrollRecords[i];
                index++;
            }
        }

        // Sort chronologically
        QuickSort.sort(result, (a, b) -> {

            int yearComparison = Integer.compare(
                    a.getYear(), b.getYear());

            if (yearComparison != 0) {
                return yearComparison;
            }

            return Integer.compare(
                    a.getMonth(), b.getMonth());
        });

        return result;
    }

    // Display a salary slip for an employee
    public void displaySalarySlip(int employeeId,
            int month, int year) {

        PayrollRecord record = getPayrollRecord(
                employeeId, month, year);

        if (record == null) {
            System.out.println(
                    "No payroll record found for the selected month.");
            return;
        }

        Employee employee = employeeTable.get(employeeId);

        if (employee != null) {

            System.out.println("\nEmployee Name : "
                    + employee.getName());

            System.out.println("Department    : "
                    + employee.getDepartment());

            System.out.println("Designation   : "
                    + employee.getPost());
        }

        record.displaySalarySlip();
    }

    // Display all available payroll months for an employee
    public void displayEmployeePayrollHistory(int employeeId) {

        PayrollRecord[] records = getEmployeePayroll(employeeId);

        if (records.length == 0) {
            System.out.println("No payroll history found.");
            return;
        }

        System.out.println("\n=== MY PAYROLL HISTORY ===");

        System.out.printf(
                "%-10s %-10s %-15s %-15s%n",
                "Month", "Year", "Basic Salary", "Net Salary");

        System.out.println(
                "-----------------------------------------------------");

        for (PayrollRecord record : records) {

            System.out.printf(
                    "%-10d %-10d %-15.2f %-15.2f%n",
                    record.getMonth(),
                    record.getYear(),
                    record.getBasicSalary(),
                    record.getNetSalary());
        }
    }

    // HR: Display payroll records for a selected month
    public void displayMonthlyPayroll(int month, int year) {

        if (month < 1 || month > 12 || year <= 0) {
            System.out.println("Invalid month or year.");
            return;
        }

        System.out.println(
                "\n=== PAYROLL REPORT: " + month + "/" + year + " ===");

        System.out.printf(
                "%-8s %-20s %-15s %-15s%n",
                "EIS", "Employee Name", "Basic Salary", "Net Salary");

        System.out.println(
                "---------------------------------------------------------------");

        boolean found = false;
        double totalPayroll = 0;

        for (int i = 0; i < count; i++) {

            PayrollRecord record = payrollRecords[i];

            if (record.getMonth() == month
                    && record.getYear() == year) {

                Employee employee = employeeTable.get(
                        record.getEmployeeId());

                String name = employee != null
                        ? employee.getName()
                        : "Unknown";

                System.out.printf(
                        "%-8d %-20s %-15.2f %-15.2f%n",
                        record.getEmployeeId(),
                        name,
                        record.getBasicSalary(),
                        record.getNetSalary());

                totalPayroll += record.getNetSalary();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No payroll records found.");
            return;
        }

        System.out.println(
                "---------------------------------------------------------------");

        System.out.printf(
                "Total Net Payroll: Rs. %.2f%n",
                totalPayroll);
    }

    // HR: Display all payroll records
    public void displayAllPayrollRecords() {

        PayrollRecord[] records = getAllPayrollRecords();

        if (records.length == 0) {
            System.out.println("No payroll records found.");
            return;
        }

        QuickSort.sort(records, getPayrollComparator());

        System.out.println("\n=== ALL PAYROLL RECORDS ===");

        for (PayrollRecord record : records) {
            record.display();
        }
    }

    // Number of payroll records
    public int size() {
        return count;
    }
}
