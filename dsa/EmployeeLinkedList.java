
package dsa;

import models.Employee;

public class EmployeeLinkedList {

    // Each node stores one employee and the next node's address
    private static class Node {

        Employee data;
        Node next;

        Node(Employee employee) {
            data = employee;
            next = null;
        }
    }

    private Node head;
    private int count;

    // Constructor
    public EmployeeLinkedList() {
        head = null;
        count = 0;
    }

    // Add a new employee at the end of the linked list
    public boolean addEmployee(Employee employee) {

        if (employee == null) {
            return false;
        }

        // Employee IDs must be unique
        if (findEmployee(employee.getEmployeeId()) != null) {
            return false;
        }

        Node newNode = new Node(employee);

        // First employee becomes the head
        if (head == null) {
            head = newNode;
        } else {

            Node temp = head;

            // Traverse until the last node
            while (temp.next != null) {
                temp = temp.next;
            }

            temp.next = newNode;
        }

        count++;
        return true;
    }

    // Search for an employee using their EIS number
    public Employee findEmployee(int employeeId) {

        Node temp = head;

        while (temp != null) {

            if (temp.data.getEmployeeId() == employeeId) {
                return temp.data;
            }

            temp = temp.next;
        }

        return null;
    }

    // Display all employees in a table
    public void displayEmployees() {

        if (head == null) {
            System.out.println("No employee records found.");
            return;
        }

        System.out.printf(
                "%-8s %-22s %-14s %-8s %-14s %-12s%n",
                "EIS",
                "Name",
                "Department",
                "Grade",
                "Basic Salary",
                "Supervisor");

        System.out.println(
                "-------------------------------------------------------------------------------");

        Node temp = head;

        while (temp != null) {

            Employee employee = temp.data;

            String supervisor = employee.getSupervisorId() == -1
                    ? "-"
                    : String.valueOf(employee.getSupervisorId());

            System.out.printf(
                    "%-8d %-22s %-14s %-8s %-14.2f %-12s%n",
                    employee.getEmployeeId(),
                    employee.getName(),
                    employee.getDepartment(),
                    employee.getGrade(),
                    employee.getBasicSalary(),
                    supervisor);

            temp = temp.next;
        }
    }

    // Return the total number of employees
    public int size() {
        return count;
    }

    // Convert the linked list into an array for sorting and searching
    public Employee[] toArray() {

        Employee[] employees = new Employee[count];

        Node temp = head;
        int index = 0;

        while (temp != null) {
            employees[index] = temp.data;
            index++;
            temp = temp.next;
        }

        return employees;
    }
}
