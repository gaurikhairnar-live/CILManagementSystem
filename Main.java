
import java.util.Scanner;

import models.Employee;

import dsa.EmployeeLinkedList;
import dsa.CustomHashTable;
import dsa.OrganizationGraph;

import services.AuthenticationService;
import services.EmployeeService;
import services.AttendanceService;
import services.LeaveService;
import services.PayrollService;
import services.HousingService;
import services.PromotionService;
import services.AppraisalService;
import services.MedicalService;

import portals.EmployeePortal;
import portals.HRPortal;

import data.DataInitializer;

public class Main {

    private static Scanner sc = new Scanner(System.in);

    private static AuthenticationService authenticationService;

    private static EmployeeService employeeService;
    private static AttendanceService attendanceService;
    private static LeaveService leaveService;
    private static PayrollService payrollService;
    private static HousingService housingService;
    private static PromotionService promotionService;
    private static AppraisalService appraisalService;
    private static MedicalService medicalService;

    // Read integer safely
    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Integer.parseInt(sc.nextLine().trim());

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Initialize all DSA structures, services and sample data
    private static void initializeSystem() {

        // Custom DSA structures
        EmployeeLinkedList employeeList = new EmployeeLinkedList();

        CustomHashTable<Integer, Employee> employeeTable = new CustomHashTable<>();

        OrganizationGraph organizationGraph = new OrganizationGraph();

        // Services
        authenticationService = new AuthenticationService(employeeTable);

        employeeService = new EmployeeService(
                employeeList,
                employeeTable);

        attendanceService = new AttendanceService();

        leaveService = new LeaveService(
                employeeTable,
                attendanceService);

        payrollService = new PayrollService(
                employeeTable);

        housingService = new HousingService(
                employeeTable);

        promotionService = new PromotionService(
                employeeTable,
                organizationGraph);

        appraisalService = new AppraisalService(
                employeeTable);

        medicalService = new MedicalService(
                employeeTable);

        // Initialize fictional sample records
        DataInitializer initializer = new DataInitializer(
                employeeList,
                employeeTable,
                organizationGraph,
                attendanceService,
                leaveService,
                payrollService,
                housingService,
                promotionService,
                appraisalService,
                medicalService);

        initializer.initializeData();
    }

    // Employee login
    private static void employeeLogin() {

        System.out.println("\n========== EMPLOYEE LOGIN ==========");

        int employeeId = readInt("Enter EIS Number: ");

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        Employee employee = authenticationService.employeeLogin(
                employeeId,
                password);

        if (employee == null) {

            System.out.println(
                    "Invalid EIS number or password.");

            return;
        }

        System.out.println("\nLogin successful!");
        System.out.println("Welcome, " + employee.getName());

        EmployeePortal portal = new EmployeePortal(
                employee,
                sc,
                attendanceService,
                leaveService,
                payrollService,
                housingService,
                promotionService,
                appraisalService,
                medicalService);

        portal.start();
    }

    // HR login
    private static void hrLogin() {

        System.out.println("\n========== HR LOGIN ==========");

        System.out.print("Enter HR ID: ");
        String hrId = sc.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        boolean valid = authenticationService.hrLogin(
                hrId,
                password);

        if (!valid) {

            System.out.println(
                    "Invalid HR ID or password.");

            return;
        }

        System.out.println("\nHR login successful!");

        HRPortal portal = new HRPortal(
                sc,
                employeeService,
                attendanceService,
                leaveService,
                payrollService,
                housingService,
                promotionService,
                appraisalService,
                medicalService);

        portal.start();
    }

    // Main entry point
    public static void main(String[] args) {

        System.out.println(
                "Initializing CIL Employee & HR Management System...");

        initializeSystem();

        int choice;

        do {

            System.out.println("\n========================================");
            System.out.println("  CIL EMPLOYEE & HR MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Employee Login");
            System.out.println("2. HR Login");
            System.out.println("0. Exit");
            System.out.println("========================================");

            choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    employeeLogin();
                    break;

                case 2:
                    hrLogin();
                    break;

                case 0:
                    System.out.println(
                            "\nThank you for using the CIL Management System.");
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again.");
            }

        } while (choice != 0);

        sc.close();
    }
}
