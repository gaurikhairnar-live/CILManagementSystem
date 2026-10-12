
package services;

import dsa.EmployeeLinkedList;
import dsa.CustomHashTable;
import dsa.QuickSort;
import dsa.BinarySearch;
import models.Employee;

import java.util.Comparator;

public class EmployeeService {

    private EmployeeLinkedList employeeList;
    private CustomHashTable<Integer, Employee> employeeTable;

    // Constructor
    public EmployeeService(EmployeeLinkedList employeeList,
            CustomHashTable<Integer, Employee> employeeTable) {

        this.employeeList = employeeList;
        this.employeeTable = employeeTable;
    }

    // Retrieve one employee using EIS number
    public Employee getEmployee(int employeeId) {
        return employeeTable.get(employeeId);
    }

    // Retrieve all employees as an array
    public Employee[] getAllEmployees() {
        return employeeList.toArray();
    }

    // Select the comparison rule for a particular column
    private Comparator<Employee> getComparator(int column) {

        switch (column) {

            case 1: // Employee ID
                return (a, b) -> Integer.compare(
                        a.getEmployeeId(), b.getEmployeeId());

            case 2: // Name
                return (a, b) -> a.getName()
                        .compareToIgnoreCase(b.getName());

            case 3: // Department
                return (a, b) -> a.getDepartment()
                        .compareToIgnoreCase(b.getDepartment());

            case 4: // Basic salary
                return (a, b) -> Double.compare(
                        a.getBasicSalary(), b.getBasicSalary());

            case 5: // Grade
                return (a, b) -> Integer.compare(
                        Integer.parseInt(a.getGrade().substring(1)),
                        Integer.parseInt(b.getGrade().substring(1)));

            case 6: // Performance rating
                return (a, b) -> Double.compare(
                        a.getPerformanceRating(),
                        b.getPerformanceRating());

            case 7: // Date of joining
                return (a, b) -> a.getDateOfJoining()
                        .compareTo(b.getDateOfJoining());

            case 8: // Supervisor ID
                return (a, b) -> Integer.compare(
                        a.getSupervisorId(), b.getSupervisorId());

            default:
                throw new IllegalArgumentException(
                        "Invalid employee column");
        }
    }

    // Sort employees by a selected column
    public Employee[] sortEmployees(int column, boolean ascending) {

        Employee[] employees = getAllEmployees();

        Comparator<Employee> comparator = getComparator(column);

        if (!ascending) {
            comparator = comparator.reversed();
        }

        QuickSort.sort(employees, comparator);

        return employees;
    }

    // Search for employees using a selected column
    public Employee[] searchEmployees(int column, String searchValue) {

        if (searchValue == null || searchValue.trim().isEmpty()) {
            return new Employee[0];
        }

        Employee[] employees = getAllEmployees();

        Comparator<Employee> comparator = getComparator(column);

        // Binary Search requires a sorted array
        QuickSort.sort(employees, comparator);

        Employee target = null;

        try {

            // Build a search target using an existing employee record
            for (Employee employee : employees) {

                if (matchesValue(employee, column, searchValue.trim())) {
                    target = employee;
                    break;
                }
            }

        } catch (NumberFormatException e) {
            return new Employee[0];
        }

        if (target == null) {
            return new Employee[0];
        }

        // Find the first matching position
        int firstIndex = BinarySearch.searchFirst(
                employees, target, comparator);

        if (firstIndex == -1) {
            return new Employee[0];
        }

        // Count employees with the same searched value
        int count = 0;

        for (int i = firstIndex; i < employees.length; i++) {

            if (comparator.compare(employees[i], target) != 0) {
                break;
            }

            count++;
        }

        Employee[] results = new Employee[count];

        for (int i = 0; i < count; i++) {
            results[i] = employees[firstIndex + i];
        }

        return results;
    }

    // Check whether an employee matches the entered search value
    private boolean matchesValue(Employee employee, int column,
            String value) {

        switch (column) {

            case 1:
                return employee.getEmployeeId() == Integer.parseInt(value);

            case 2:
                return employee.getName().equalsIgnoreCase(value);

            case 3:
                return employee.getDepartment().equalsIgnoreCase(value);

            case 4:
                return Double.compare(
                        employee.getBasicSalary(),
                        Double.parseDouble(value)) == 0;

            case 5:
                return employee.getGrade().equalsIgnoreCase(value);

            case 6:
                return Double.compare(
                        employee.getPerformanceRating(),
                        Double.parseDouble(value)) == 0;

            case 7:
                return employee.getDateOfJoining()
                        .toString().equals(value);

            case 8:
                return employee.getSupervisorId() == Integer.parseInt(value);

            default:
                return false;
        }
    }

    // Display employee records in HR's table
    public void displayEmployees(Employee[] employees) {

        if (employees == null || employees.length == 0) {
            System.out.println("No matching employees found.");
            return;
        }

        System.out.printf(
                "%-7s %-20s %-12s %-12s %-7s %-12s %-8s %-12s %-10s%n",
                "EIS", "Name", "Department", "Post", "Grade",
                "Salary", "Rating", "DOJ", "Manager");

        System.out.println(
                "-------------------------------------------------------------------------------------------------------------");

        for (Employee employee : employees) {

            String manager = employee.getSupervisorId() == -1
                    ? "-"
                    : String.valueOf(employee.getSupervisorId());

            System.out.printf(
                    "%-7d %-20s %-12s %-12s %-7s %-12.2f %-8.1f %-12s %-10s%n",
                    employee.getEmployeeId(),
                    employee.getName(),
                    employee.getDepartment(),
                    employee.getPost(),
                    employee.getGrade(),
                    employee.getBasicSalary(),
                    employee.getPerformanceRating(),
                    employee.getDateOfJoining(),
                    manager);
        }
    }
}
