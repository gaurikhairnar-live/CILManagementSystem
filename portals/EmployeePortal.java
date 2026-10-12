
package portals;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import models.Employee;

import services.AttendanceService;
import services.LeaveService;
import services.PayrollService;
import services.HousingService;
import services.PromotionService;
import services.AppraisalService;
import services.MedicalService;

public class EmployeePortal {

    private Employee employee;
    private Scanner sc;

    private AttendanceService attendanceService;
    private LeaveService leaveService;
    private PayrollService payrollService;
    private HousingService housingService;
    private PromotionService promotionService;
    private AppraisalService appraisalService;
    private MedicalService medicalService;

    public EmployeePortal(
            Employee employee,
            Scanner sc,
            AttendanceService attendanceService,
            LeaveService leaveService,
            PayrollService payrollService,
            HousingService housingService,
            PromotionService promotionService,
            AppraisalService appraisalService,
            MedicalService medicalService) {

        this.employee = employee;
        this.sc = sc;
        this.attendanceService = attendanceService;
        this.leaveService = leaveService;
        this.payrollService = payrollService;
        this.housingService = housingService;
        this.promotionService = promotionService;
        this.appraisalService = appraisalService;
        this.medicalService = medicalService;
    }

    // =====================================================
    // MAIN EMPLOYEE DASHBOARD
    // =====================================================

    public void start() {

        while (true) {

            System.out.println("\n======================================");
            System.out.println("       CIL EMPLOYEE PORTAL");
            System.out.println("======================================");
            System.out.println("Welcome, " + employee.getName());
            System.out.println("EIS Number: " + employee.getEmployeeId());

            System.out.println("\n1. My Profile");
            System.out.println("2. Attendance");
            System.out.println("3. Leave Management");
            System.out.println("4. Payroll");
            System.out.println("5. Transfer History");
            System.out.println("6. Housing / Quarter");
            System.out.println("7. Performance");
            System.out.println("8. Promotion Eligibility");
            System.out.println("9. Self-Appraisal");
            System.out.println("10. Medical");
            System.out.println("11. Insurance");
            System.out.println("0. Logout");

            int choice = readInt("\nEnter choice: ");

            switch (choice) {

                case 1:
                    displayProfile();
                    break;

                case 2:
                    attendanceService.displayAttendance(
                        employee.getEmployeeId()
                    );
                    break;

                case 3:
                    leaveMenu();
                    break;

                case 4:
                    payrollMenu();
                    break;

                case 5:
                    displayTransferHistory();
                    break;

                case 6:
                    housingMenu();
                    break;

                case 7:
                    appraisalService.displayEmployeePerformance(
                        employee.getEmployeeId()
                    );
                    break;

                case 8:
                    promotionService.displayPromotionEligibility(
                        employee.getEmployeeId()
                    );
                    break;

                case 9:
                    appraisalMenu();
                    break;

                case 10:
                    medicalMenu();
                    break;

                case 11:
                    displayInsurance();
                    break;

                case 0:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =====================================================
    // MY PROFILE
    // =====================================================

    private void displayProfile() {

        System.out.println("\n========== MY PROFILE ==========");

        System.out.println("EIS Number  : " + employee.getEmployeeId());
        System.out.println("Name        : " + employee.getName());
        System.out.println("Department  : " + employee.getDepartment());
        System.out.println("Post        : " + employee.getPost());
        System.out.println("Grade       : " + employee.getGrade());
        System.out.println("Mobile      : " + employee.getMobileNumber());
        System.out.println("Joining Date: " + employee.getDateOfJoining());
        System.out.println("Basic Salary: Rs. " + employee.getBasicSalary());

        System.out.printf(
            "Rating      : %.1f / 5.0%n",
            employee.getPerformanceRating()
        );

        System.out.println(
            "Points      : " + employee.getPerformancePoints()
        );

        System.out.println(
            "Supervisor EIS: " + employee.getSupervisorId()
        );
    }

    // =====================================================
    // LEAVE MANAGEMENT
    // =====================================================

    private void leaveMenu() {

        while (true) {

            System.out.println("\n========== LEAVE MANAGEMENT ==========");
            System.out.println("1. View Leave Balance");
            System.out.println("2. Apply for Leave");
            System.out.println("3. My Leave Applications");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    leaveService.displayLeaveBalance(
                        employee.getEmployeeId()
                    );
                    break;

                case 2:
                    applyLeave();
                    break;

                case 3:
                    leaveService.displayEmployeeRequests(
                        employee.getEmployeeId()
                    );
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void applyLeave() {

        System.out.println("\n========== APPLY FOR LEAVE ==========");

        System.out.println("1. Sick");
        System.out.println("2. Casual");
        System.out.println("3. Paid");

        int choice = readInt("Select leave type: ");

        String leaveType;

        switch (choice) {

            case 1:
                leaveType = "Sick";
                break;

            case 2:
                leaveType = "Casual";
                break;

            case 3:
                leaveType = "Paid";
                break;

            default:
                System.out.println("Invalid leave type.");
                return;
        }

        LocalDate fromDate = readDate("From date (YYYY-MM-DD): ");
        LocalDate toDate = readDate("To date (YYYY-MM-DD): ");

        if (toDate.isBefore(fromDate)) {
            System.out.println("End date cannot be before start date.");
            return;
        }

        String reason = readText("Reason: ");

        leaveService.applyForLeave(
            employee.getEmployeeId(),
            leaveType,
            fromDate,
            toDate,
            reason
        );
    }

    // =====================================================
    // PAYROLL
    // =====================================================

    private void payrollMenu() {

        while (true) {

            System.out.println("\n========== PAYROLL ==========");
            System.out.println("1. View Salary Slip");
            System.out.println("2. View Payroll History");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:

                    int month = readInt("Enter month (1-12): ");
                    int year = readInt("Enter year: ");

                    if (month < 1 || month > 12) {
                        System.out.println("Invalid month.");
                        break;
                    }

                    payrollService.displaySalarySlip(
                        employee.getEmployeeId(),
                        month,
                        year
                    );
                    break;

                case 2:
                    payrollService.displayEmployeePayrollHistory(
                        employee.getEmployeeId()
                    );
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =====================================================
    // TRANSFER HISTORY
    // =====================================================

    private void displayTransferHistory() {

        System.out.println("\n========== TRANSFER HISTORY ==========");
        System.out.println("Current Department: " + employee.getDepartment());
        System.out.println("Current Post      : " + employee.getPost());

        System.out.println(
            "No transfer history records are available in the current dataset."
        );
    }

    // =====================================================
    // HOUSING / QUARTER - UPDATED
    // =====================================================

    private void housingMenu() {

        while (true) {

            System.out.println("\n========== HOUSING / QUARTER ==========");
            System.out.println("Employee: " + employee.getName());
            System.out.println("Grade   : " + employee.getGrade());

            System.out.println(
                "Eligible Quarter Type: "
                + housingService.getEligibleQuarterType(
                    employee.getEmployeeId()
                )
            );

            System.out.println("\n1. My Allotted Quarter");
            System.out.println("2. Apply for Quarter");
            System.out.println("3. My Housing Applications");
            System.out.println("4. View Colony Vacancies");
            System.out.println("5. Vacate My Quarter");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    housingService.displayEmployeeQuarter(
                        employee.getEmployeeId()
                    );
                    break;

                case 2:
                    applyForQuarter();
                    break;

                case 3:
                    housingService.displayEmployeeApplications(
                        employee.getEmployeeId()
                    );
                    break;

                case 4:
                    housingService.displayColonyVacancies();
                    break;

                case 5:
                    vacateMyQuarter();
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void applyForQuarter() {

        System.out.println("\n========== APPLY FOR QUARTER ==========");

        String eligibleType =
            housingService.getEligibleQuarterType(
                employee.getEmployeeId()
            );

        if (eligibleType == null) {
            System.out.println("Cannot determine eligible quarter type.");
            return;
        }

        System.out.println("Employee Grade: " + employee.getGrade());
        System.out.println("Eligible Type : " + eligibleType);

        System.out.println(
            "\nQuarter type is selected automatically based on your grade."
        );

        if (confirm("Submit housing application? (Y/N): ")) {

            housingService.applyForHousing(
                employee.getEmployeeId()
            );
        }
    }

    private void vacateMyQuarter() {

        System.out.println("\n========== VACATE MY QUARTER ==========");

        housingService.displayEmployeeQuarter(
            employee.getEmployeeId()
        );

        if (confirm("\nConfirm quarter vacation? (Y/N): ")) {

            housingService.vacateEmployeeQuarter(
                employee.getEmployeeId()
            );
        }
    }

    // =====================================================
    // SELF-APPRAISAL - UPDATED
    // =====================================================

    private void appraisalMenu() {

        while (true) {

            System.out.println("\n========== SELF-APPRAISAL ==========");
            System.out.println("1. Submit Self-Appraisal");
            System.out.println("2. View My Appraisals");
            System.out.println("3. View HR Review and Rating");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    submitSelfAppraisal();
                    break;

                case 2:
                    appraisalService.displayEmployeeAppraisals(
                        employee.getEmployeeId()
                    );
                    break;

                case 3:
                    appraisalService.displayEmployeePerformance(
                        employee.getEmployeeId()
                    );
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void submitSelfAppraisal() {

        System.out.println("\n========== SUBMIT SELF-APPRAISAL ==========");

        String achievements = readText(
            "Achievements: "
        );

        String teamContribution = readText(
            "Team Contribution: "
        );

        String challenges = readText(
            "Challenges Faced: "
        );

        String improvedSkills = readText(
            "Skills Improved: "
        );

        String futureGoals = readText(
            "Future Goals: "
        );

        appraisalService.submitAppraisal(
            employee.getEmployeeId(),
            achievements,
            teamContribution,
            challenges,
            improvedSkills,
            futureGoals
        );
    }

    // =====================================================
    // MEDICAL
    // =====================================================

    private void medicalMenu() {

        while (true) {

            System.out.println("\n========== MEDICAL ==========");
            System.out.println("1. My Medical Profile");
            System.out.println("2. My Dependents' Medical Benefits");
            System.out.println("3. Medical Referrals");
            System.out.println("4. Medical History");
            System.out.println("0. Back");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    medicalService.displayMedicalProfile(
                        employee.getEmployeeId()
                    );
                    break;

                case 2:
                    medicalService.displayDependentBenefits(
                        employee.getEmployeeId()
                    );
                    break;

                case 3:
                    medicalService.displayMedicalReferrals(
                        employee.getEmployeeId()
                    );
                    break;

                case 4:
                    medicalService.displayMedicalHistory(
                        employee.getEmployeeId()
                    );
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =====================================================
    // INSURANCE
    // =====================================================

    private void displayInsurance() {

        System.out.println("\n========== INSURANCE ==========");
        System.out.println("Employee EIS: " + employee.getEmployeeId());

        System.out.println(
            "Insurance policy details are not available in the current dataset."
        );
    }

    // =====================================================
    // INPUT HELPERS
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

    private LocalDate readDate(String prompt) {

        while (true) {

            System.out.print(prompt);

            String value = sc.nextLine().trim();

            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException exception) {
                System.out.println(
                    "Invalid date. Use YYYY-MM-DD."
                );
            }
        }
    }

    private boolean confirm(String prompt) {

        System.out.print(prompt);

        String answer = sc.nextLine().trim();

        return answer.equalsIgnoreCase("Y")
            || answer.equalsIgnoreCase("YES");
    }
}
