
package portals;

import java.util.Scanner;

import models.Employee;

import services.EmployeeService;
import services.AttendanceService;
import services.LeaveService;
import services.PayrollService;
import services.HousingService;
import services.PromotionService;
import services.AppraisalService;
import services.MedicalService;

public class HRPortal {

    private Scanner sc;

    private EmployeeService employeeService;
    private AttendanceService attendanceService;
    private LeaveService leaveService;
    private PayrollService payrollService;
    private HousingService housingService;
    private PromotionService promotionService;
    private AppraisalService appraisalService;
    private MedicalService medicalService;

    public HRPortal(
            Scanner sc,
            EmployeeService employeeService,
            AttendanceService attendanceService,
            LeaveService leaveService,
            PayrollService payrollService,
            HousingService housingService,
            PromotionService promotionService,
            AppraisalService appraisalService,
            MedicalService medicalService) {

        this.sc = sc;
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
        this.leaveService = leaveService;
        this.payrollService = payrollService;
        this.housingService = housingService;
        this.promotionService = promotionService;
        this.appraisalService = appraisalService;
        this.medicalService = medicalService;
    }

    // =====================================================
    // MAIN HR DASHBOARD
    // =====================================================

    public void start() {

        while (true) {

            System.out.println("\n======================================");
            System.out.println("          CIL HR PORTAL");
            System.out.println("======================================");

            System.out.println("1. Employee Management");
            System.out.println("2. Attendance Management");
            System.out.println("3. Leave Management");
            System.out.println("4. Payroll Management");
            System.out.println("5. Housing / Quarter Management");
            System.out.println("6. Promotion Management");
            System.out.println("7. Appraisal Management");
            System.out.println("8. Medical Management");
            System.out.println("9. Organization Hierarchy");
            System.out.println("0. Logout");

            int choice = readInt("\nEnter choice: ");

            switch (choice) {

                case 1:
                    employeeManagementMenu();
                    break;

                case 2:
                    attendanceManagementMenu();
                    break;

                case 3:
                    leaveManagementMenu();
                    break;

                case 4:
                    payrollManagementMenu();
                    break;

                case 5:
                    housingManagementMenu();
                    break;

                case 6:
                    promotionManagementMenu();
                    break;

                case 7:
                    appraisalManagementMenu();
                    break;

                case 8:
                    medicalManagementMenu();
                    break;

                case 9:
                    hierarchyMenu();
                    break;

                case 0:
                    System.out.println("HR logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =====================================================
    // 1. EMPLOYEE MANAGEMENT
    // =====================================================

    private void employeeManagementMenu() {

        while (true) {

            System.out.println("\n========== EMPLOYEE MANAGEMENT ==========");
            System.out.println("1. Display All Employees");
            System.out.println("2. Search Employee by EIS");
            System.out.println("3. Search Employees by Name");
            System.out.println("4. Search Employees by Department");
            System.out.println("5. Sort Employees by EIS");
            System.out.println("6. Sort Employees by Salary");
            System.out.println("7. Sort Employees by Performance Rating");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    employeeService.displayEmployees(
                        employeeService.getAllEmployees()
                    );
                    break;

                case 2:
                    displayEmployeeById();
                    break;

                case 3:
                    searchEmployees(2);
                    break;

                case 4:
                    searchEmployees(3);
                    break;

                case 5:
                    sortEmployees(1);
                    break;

                case 6:
                    sortEmployees(2);
                    break;

                case 7:
                    sortEmployees(3);
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void displayEmployeeById() {

        int employeeId = readInt("Enter Employee EIS: ");

        Employee employee = employeeService.getEmployee(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        displayEmployeeDetails(employee);
    }

    private void displayEmployeeDetails(Employee employee) {

        System.out.println("\n========== EMPLOYEE DETAILS ==========");

        System.out.println("EIS        : " + employee.getEmployeeId());
        System.out.println("Name       : " + employee.getName());
        System.out.println("Department : " + employee.getDepartment());
        System.out.println("Post       : " + employee.getPost());
        System.out.println("Grade      : " + employee.getGrade());
        System.out.println("Mobile     : " + employee.getMobileNumber());
        System.out.println("Joining    : " + employee.getDateOfJoining());
        System.out.println("Salary     : Rs. " + employee.getBasicSalary());

        System.out.printf(
            "Rating     : %.1f / 5.0%n",
            employee.getPerformanceRating()
        );

        System.out.println(
            "Points     : " + employee.getPerformancePoints()
        );

        System.out.println(
            "Supervisor : " + employee.getSupervisorId()
        );
    }

    private void searchEmployees(int searchType) {

        String value = readText("Enter search value: ");

        Employee[] results =
            employeeService.searchEmployees(searchType, value);

        employeeService.displayEmployees(results);
    }

    private void sortEmployees(int sortType) {

        System.out.println("1. Ascending");
        System.out.println("2. Descending");

        int order = readInt("Select order: ");

        if (order != 1 && order != 2) {
            System.out.println("Invalid sorting order.");
            return;
        }

        boolean ascending = (order == 1);

        Employee[] results =
            employeeService.sortEmployees(sortType, ascending);

        employeeService.displayEmployees(results);
    }

    // =====================================================
    // 2. ATTENDANCE MANAGEMENT
    // =====================================================

    private void attendanceManagementMenu() {

        while (true) {

            System.out.println("\n========== ATTENDANCE MANAGEMENT ==========");
            System.out.println("1. View Employee Attendance");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    int employeeId =
                        readInt("Enter Employee EIS: ");

                    attendanceService.displayAttendance(employeeId);
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =====================================================
    // 3. LEAVE MANAGEMENT
    // =====================================================

    private void leaveManagementMenu() {

        while (true) {

            System.out.println("\n========== LEAVE MANAGEMENT ==========");
            System.out.println("1. View Pending Leave Requests");
            System.out.println("2. View All Leave Requests");
            System.out.println("3. Approve Leave");
            System.out.println("4. Reject Leave");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    leaveService.displayPendingRequests();
                    break;

                case 2:
                    leaveService.displayAllRequests();
                    break;

                case 3:
                    processLeave(true);
                    break;

                case 4:
                    processLeave(false);
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void processLeave(boolean approve) {

        int employeeId = readInt("Enter Employee EIS: ");
        String requestId = readText("Enter Leave Request ID: ");

        if (approve) {

            leaveService.approveLeave(employeeId, requestId);

        } else {

            leaveService.rejectLeave(employeeId, requestId);
        }
    }

    // =====================================================
    // 4. PAYROLL MANAGEMENT
    // =====================================================

    private void payrollManagementMenu() {

        while (true) {

            System.out.println("\n========== PAYROLL MANAGEMENT ==========");
            System.out.println("1. Generate Employee Payroll");
            System.out.println("2. View Employee Salary Slip");
            System.out.println("3. View Monthly Payroll");
            System.out.println("4. View All Payroll Records");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    generatePayroll();
                    break;

                case 2:
                    viewSalarySlip();
                    break;

                case 3:
                    viewMonthlyPayroll();
                    break;

                case 4:
                    payrollService.displayAllPayrollRecords();
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void generatePayroll() {

        int employeeId = readInt("Enter Employee EIS: ");
        int month = readInt("Enter Month (1-12): ");
        int year = readInt("Enter Year: ");

        if (month < 1 || month > 12) {
            System.out.println("Invalid month.");
            return;
        }

        payrollService.generatePayroll(employeeId, month, year);
    }

    private void viewSalarySlip() {

        int employeeId = readInt("Enter Employee EIS: ");
        int month = readInt("Enter Month (1-12): ");
        int year = readInt("Enter Year: ");

        payrollService.displaySalarySlip(employeeId, month, year);
    }

    private void viewMonthlyPayroll() {

        int month = readInt("Enter Month (1-12): ");
        int year = readInt("Enter Year: ");

        payrollService.displayMonthlyPayroll(month, year);
    }

    // =====================================================
    // 5. HOUSING MANAGEMENT
    // =====================================================

    private void housingManagementMenu() {

        while (true) {

            System.out.println("\n========== HOUSING MANAGEMENT ==========");

            System.out.println("1. View All Four Priority Queues");
            System.out.println("2. View Type A Priority Queue");
            System.out.println("3. View Type B Priority Queue");
            System.out.println("4. View Type C Priority Queue");
            System.out.println("5. View Type D Priority Queue");
            System.out.println("6. Automatic Quarter Allotment");
            System.out.println("7. View Colony-wise Vacancies");
            System.out.println("8. View All Quarters");
            System.out.println("9. View All Housing Applications");
            System.out.println("10. Reject Housing Application");
            System.out.println("11. Vacate Employee Quarter");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    housingService.displayAllPriorityQueues();
                    break;

                case 2:
                    housingService.displayPriorityQueue("A");
                    break;

                case 3:
                    housingService.displayPriorityQueue("B");
                    break;

                case 4:
                    housingService.displayPriorityQueue("C");
                    break;

                case 5:
                    housingService.displayPriorityQueue("D");
                    break;

                case 6:
                    automaticQuarterAllotment();
                    break;

                case 7:
                    housingService.displayColonyVacancies();
                    break;

                case 8:
                    housingService.displayAllQuarters();
                    break;

                case 9:
                    housingService.displayAllApplications();
                    break;

                case 10:
                    rejectHousingApplication();
                    break;

                case 11:
                    hrVacateQuarter();
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void automaticQuarterAllotment() {

        System.out.println(
            "\n========== AUTOMATIC QUARTER ALLOTMENT =========="
        );

        int employeeId = readInt("Enter Employee EIS: ");

        Employee employee = employeeService.getEmployee(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        String eligibleType =
            housingService.getEligibleQuarterType(employeeId);

        System.out.println("Employee Name : " + employee.getName());
        System.out.println("Employee Grade: " + employee.getGrade());
        System.out.println("Quarter Type  : " + eligibleType);

        System.out.println(
            "\nThe system will check application priority "
            + "and automatically select an available quarter."
        );

        if (confirm("Proceed with allotment? (Y/N): ")) {

            housingService.autoAllotQuarter(employeeId);
        }
    }

    private void rejectHousingApplication() {

        int employeeId = readInt("Enter Employee EIS: ");
        String requestId = readText("Enter Housing Request ID: ");

        if (confirm("Reject this application? (Y/N): ")) {

            housingService.rejectApplication(
                employeeId,
                requestId
            );
        }
    }

    private void hrVacateQuarter() {

        System.out.println(
            "\n========== VACATE EMPLOYEE QUARTER =========="
        );

        int employeeId = readInt("Enter Employee EIS: ");

        Employee employee = employeeService.getEmployee(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("Employee: " + employee.getName());

        housingService.displayEmployeeQuarter(employeeId);

        if (confirm("Confirm quarter vacation? (Y/N): ")) {

            housingService.hrVacateEmployeeQuarter(employeeId);
        }
    }

    // =====================================================
    // 6. PROMOTION MANAGEMENT
    // =====================================================

    private void promotionManagementMenu() {

        while (true) {

            System.out.println("\n========== PROMOTION MANAGEMENT ==========");
            System.out.println("1. View Promotion Priority List");
            System.out.println("2. Check Employee Promotion Eligibility");
            System.out.println("3. Promote Employee");
            System.out.println("4. View All Promotion History");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    promotionService.displayPromotionPriorityList();
                    break;

                case 2:
                    int employeeId =
                        readInt("Enter Employee EIS: ");

                    promotionService.displayPromotionEligibility(employeeId);
                    break;

                case 3:
                    promoteEmployee();
                    break;

                case 4:
                    promotionService.displayAllPromotionHistory();
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void promoteEmployee() {

        int employeeId = readInt("Enter Employee EIS: ");
        String newPost = readText("Enter New Designation: ");

        if (confirm("Confirm promotion? (Y/N): ")) {

            promotionService.promoteEmployee(
                employeeId,
                newPost
            );
        }
    }

    // =====================================================
    // 7. APPRAISAL MANAGEMENT
    // =====================================================

    private void appraisalManagementMenu() {

        while (true) {

            System.out.println("\n========== APPRAISAL MANAGEMENT ==========");
            System.out.println("1. View All Appraisals");
            System.out.println("2. View Pending Appraisals");
            System.out.println("3. View Reviewed Appraisals");
            System.out.println("4. View Employee Appraisals");
            System.out.println("5. Review and Rate Employee");
            System.out.println("6. View Appraisal Summaries");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    appraisalService.displayAllAppraisals();
                    break;

                case 2:
                    appraisalService.displayPendingAppraisals();
                    break;

                case 3:
                    appraisalService.displayReviewedAppraisals();
                    break;

                case 4:
                    int employeeId =
                        readInt("Enter Employee EIS: ");

                    appraisalService.displayAppraisalsByEmployee(
                        employeeId
                    );
                    break;

                case 5:
                    reviewEmployeeAppraisal();
                    break;

                case 6:
                    appraisalService.displayAppraisalSummaries();
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void reviewEmployeeAppraisal() {

        System.out.println(
            "\n========== REVIEW EMPLOYEE APPRAISAL =========="
        );

        int employeeId = readInt("Enter Employee EIS: ");

        Employee employee = employeeService.getEmployee(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("\nEmployee: " + employee.getName());

        appraisalService.displayAppraisalsByEmployee(employeeId);

        System.out.printf(
            "\nCurrent Rating: %.1f / 5.0%n",
            employee.getPerformanceRating()
        );

        System.out.println(
            "Current Points: " + employee.getPerformancePoints()
        );

        double rating = readDouble(
            "\nEnter New HR Rating (0-5): "
        );

        if (rating < 0 || rating > 5) {
            System.out.println("Rating must be between 0 and 5.");
            return;
        }

        int points = readInt(
            "Enter Performance Points (0-100): "
        );

        if (points < 0 || points > 100) {
            System.out.println(
                "Performance points must be between 0 and 100."
            );
            return;
        }

        String feedback = readText("Enter HR Feedback: ");

        if (confirm("Save HR appraisal review? (Y/N): ")) {

            appraisalService.reviewEmployeeAppraisal(
                employeeId,
                rating,
                points,
                feedback
            );
        }
    }

    // =====================================================
    // 8. MEDICAL MANAGEMENT
    // =====================================================

    private void medicalManagementMenu() {

        while (true) {

            System.out.println("\n========== MEDICAL MANAGEMENT ==========");
            System.out.println("1. View Employee Medical Profile");
            System.out.println("2. View Employee Dependents");
            System.out.println("3. View Employee Medical Referrals");
            System.out.println("4. View Employee Medical History");
            System.out.println("5. View Complete Medical Record");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            if (choice == 0) {
                return;
            }

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice.");
                continue;
            }

            int employeeId = readInt("Enter Employee EIS: ");

            switch (choice) {

                case 1:
                    medicalService.displayMedicalProfile(employeeId);
                    break;

                case 2:
                    medicalService.displayDependentBenefits(employeeId);
                    break;

                case 3:
                    medicalService.displayMedicalReferrals(employeeId);
                    break;

                case 4:
                    medicalService.displayMedicalHistory(employeeId);
                    break;

                case 5:
                    medicalService.displayCompleteMedicalRecord(employeeId);
                    break;
            }
        }
    }

    // =====================================================
    // 9. ORGANIZATION HIERARCHY - DFS ONLY
    // =====================================================

    private void hierarchyMenu() {

        while (true) {

            System.out.println("\n========== ORGANIZATION HIERARCHY ==========");
            System.out.println("1. Display Hierarchy");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    int rootId = readInt("Enter Root Employee EIS: ");
                    promotionService.displayHierarchyDFS(rootId);
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =====================================================
    // INPUT HELPER METHODS
    // =====================================================

    private int readInt(String prompt) {

        while (true) {

            System.out.print(prompt);

            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private double readDouble(String prompt) {

        while (true) {

            System.out.print(prompt);

            String input = sc.nextLine().trim();

            try {

                double value = Double.parseDouble(input);

                if (Double.isNaN(value)
                        || Double.isInfinite(value)) {

                    System.out.println(
                        "Please enter a finite numeric value."
                    );

                    continue;
                }

                return value;

            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String readText(String prompt) {

        while (true) {

            System.out.print(prompt);

            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    private boolean confirm(String prompt) {

        System.out.print(prompt);

        String answer = sc.nextLine().trim();

        return answer.equalsIgnoreCase("Y")
            || answer.equalsIgnoreCase("YES");
    }
}
