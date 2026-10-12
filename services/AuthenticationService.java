
package services;

import dsa.CustomHashTable;
import models.Employee;

public class AuthenticationService {

    // Employee records indexed by EIS number
    private CustomHashTable<Integer, Employee> employeeTable;

    // Separate login credentials for HR portal
    private CustomHashTable<String, String> hrCredentials;

    // Constructor
    public AuthenticationService(
            CustomHashTable<Integer, Employee> employeeTable) {

        this.employeeTable = employeeTable;
        this.hrCredentials = new CustomHashTable<>();

        // Predefined HR accounts for the project demonstration
        hrCredentials.put("HR001", "hr@123");
        hrCredentials.put("HR002", "hr@456");
    }

    // Authenticate an employee using EIS number and password
    public Employee employeeLogin(int employeeId, String password) {

        if (password == null) {
            return null;
        }

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {
            return null;
        }

        if (employee.getPassword().equals(password)) {
            return employee;
        }

        return null;
    }

    // Authenticate an HR user using separate credentials
    public boolean hrLogin(String hrId, String password) {

        if (hrId == null || password == null) {
            return false;
        }

        String storedPassword = hrCredentials.get(hrId);

        if (storedPassword == null) {
            return false;
        }

        return storedPassword.equals(password);
    }
}
