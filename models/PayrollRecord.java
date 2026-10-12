
package models;

public class PayrollRecord {

    // Employee and payroll identification
    private int employeeId;
    private int month;
    private int year;

    // Salary components
    private double basicSalary;
    private double ta;
    private double dra;
    private double deductions;
    private double netSalary;

    // Constructor
    public PayrollRecord(int employeeId, int month, int year,
            double basicSalary) {

        this.employeeId = employeeId;
        this.month = month;
        this.year = year;
        this.basicSalary = basicSalary;

        // Calculate salary components as per project rules
        calculateSalary();
    }

    // Calculate salary using fixed percentages
    private void calculateSalary() {

        ta = basicSalary * 0.10;
        dra = basicSalary * 0.15;
        deductions = basicSalary * 0.05;

        netSalary = basicSalary + ta + dra - deductions;
    }

    // Getters

    public int getEmployeeId() {
        return employeeId;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getTA() {
        return ta;
    }

    public double getDRA() {
        return dra;
    }

    public double getDeductions() {
        return deductions;
    }

    public double getNetSalary() {
        return netSalary;
    }

    // Display the monthly salary slip
    public void displaySalarySlip() {

        System.out.println("\n========== SALARY SLIP ==========");

        System.out.println("Employee ID : " + employeeId);
        System.out.println("Month       : " + month);
        System.out.println("Year        : " + year);

        System.out.println("---------------------------------");

        System.out.printf("Basic Salary : Rs. %.2f%n", basicSalary);
        System.out.printf("TA           : Rs. %.2f%n", ta);
        System.out.printf("DRA          : Rs. %.2f%n", dra);
        System.out.printf("Deductions   : Rs. %.2f%n", deductions);

        System.out.println("---------------------------------");

        System.out.printf("Net Salary   : Rs. %.2f%n", netSalary);

        System.out.println("=================================");
    }

    // Display one payroll record in HR's payroll table
    public void display() {

        System.out.printf(
                "%-10d %-7d %-7d %-15.2f %-15.2f%n",
                employeeId,
                month,
                year,
                basicSalary,
                netSalary);
    }
}
