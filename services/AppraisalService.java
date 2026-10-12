

package services;

import java.util.ArrayList;

import models.Employee;
import models.Appraisal;
import dsa.CustomHashTable;

public class AppraisalService {

    private CustomHashTable<Integer, Employee> employeeTable;

    // Stores all submitted appraisals
    private ArrayList<Appraisal> appraisals;

    // Constructor - unchanged
    public AppraisalService(
            CustomHashTable<Integer, Employee> employeeTable) {

        this.employeeTable = employeeTable;
        this.appraisals = new ArrayList<>();
    }

    // =====================================================
    // 1. EMPLOYEE SUBMITS SELF-APPRAISAL
    // =====================================================

    public void submitAppraisal(
            int employeeId,
            String achievements,
            String teamContribution,
            String challenges,
            String improvedSkills,
            String futureGoals) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        // Prevent duplicate pending appraisals
        for (Appraisal appraisal : appraisals) {

            if (appraisal.getEmployeeId() == employeeId
                    && !appraisal.isReviewed()) {

                System.out.println(
                    "You already have an appraisal awaiting HR review."
                );
                return;
            }
        }

        if (isBlank(achievements)
                || isBlank(teamContribution)
                || isBlank(challenges)
                || isBlank(improvedSkills)
                || isBlank(futureGoals)) {

            System.out.println(
                "All self-appraisal fields must be filled."
            );
            return;
        }

        Appraisal appraisal = new Appraisal(
            employee,
            achievements.trim(),
            teamContribution.trim(),
            challenges.trim(),
            improvedSkills.trim(),
            futureGoals.trim()
        );

        appraisals.add(appraisal);

        System.out.println(
            "\nSelf-appraisal submitted successfully!"
        );

        System.out.println(
            "Status: PENDING HR REVIEW"
        );
    }

    // =====================================================
    // 2. LOAD EXISTING APPRAISALS
    // =====================================================

    public void addExistingAppraisal(Appraisal appraisal) {

        if (appraisal == null) {
            return;
        }

        Employee employee =
            employeeTable.get(appraisal.getEmployeeId());

        if (employee == null) {
            System.out.println(
                "Cannot add appraisal: Employee not found."
            );
            return;
        }

        appraisals.add(appraisal);
    }

    // =====================================================
    // 3. DISPLAY EMPLOYEE'S OWN APPRAISALS
    // =====================================================

    public void displayEmployeeAppraisals(int employeeId) {

        boolean found = false;

        System.out.println(
            "\n========== MY SELF-APPRAISALS =========="
        );

        for (Appraisal appraisal : appraisals) {

            if (appraisal.getEmployeeId() == employeeId) {

                appraisal.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                "No self-appraisals submitted yet."
            );
        }
    }

    // =====================================================
    // 4. HR VIEWS ALL APPRAISALS
    // =====================================================

    public void displayAllAppraisals() {

        System.out.println(
            "\n========== ALL EMPLOYEE APPRAISALS =========="
        );

        if (appraisals.isEmpty()) {
            System.out.println("No appraisals available.");
            return;
        }

        for (Appraisal appraisal : appraisals) {
            appraisal.display();
        }
    }

    // =====================================================
    // 5. HR VIEWS APPRAISALS OF ONE EMPLOYEE
    // =====================================================

    public void displayAppraisalsByEmployee(int employeeId) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        boolean found = false;

        System.out.println(
            "\n========== EMPLOYEE APPRAISALS =========="
        );

        System.out.println(
            "Employee: " + employee.getName()
        );

        for (Appraisal appraisal : appraisals) {

            if (appraisal.getEmployeeId() == employeeId) {
                appraisal.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                "No appraisals found for this employee."
            );
        }
    }

    // =====================================================
    // 6. DISPLAY APPRAISAL SUMMARIES
    // =====================================================

    public void displayAppraisalSummaries() {

        System.out.println(
            "\n========== APPRAISAL SUMMARIES =========="
        );

        if (appraisals.isEmpty()) {
            System.out.println("No appraisals available.");
            return;
        }

        for (Appraisal appraisal : appraisals) {
            appraisal.displaySummary();
        }
    }

    // =====================================================
    // 7. HR VIEWS PENDING APPRAISALS
    // =====================================================

    public void displayPendingAppraisals() {

        System.out.println(
            "\n========== PENDING HR REVIEWS =========="
        );

        boolean found = false;

        for (Appraisal appraisal : appraisals) {

            if (!appraisal.isReviewed()) {
                appraisal.displaySummary();
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                "No pending appraisals."
            );
        }
    }

    // =====================================================
    // 8. HR VIEWS REVIEWED APPRAISALS
    // =====================================================

    public void displayReviewedAppraisals() {

        System.out.println(
            "\n========== REVIEWED APPRAISALS =========="
        );

        boolean found = false;

        for (Appraisal appraisal : appraisals) {

            if (appraisal.isReviewed()) {
                appraisal.displaySummary();
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                "No reviewed appraisals."
            );
        }
    }

    // =====================================================
    // 9. HR REVIEWS AN EMPLOYEE'S SELF-APPRAISAL
    // =====================================================

    public boolean reviewEmployeeAppraisal(
            int employeeId,
            double rating,
            int performancePoints,
            String feedback) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return false;
        }

        // Find the latest pending appraisal
        Appraisal pendingAppraisal = null;

        for (int i = appraisals.size() - 1; i >= 0; i--) {

            Appraisal appraisal = appraisals.get(i);

            if (appraisal.getEmployeeId() == employeeId
                    && !appraisal.isReviewed()) {

                pendingAppraisal = appraisal;
                break;
            }
        }

        if (pendingAppraisal == null) {

            System.out.println(
                "No pending self-appraisal found for this employee."
            );

            return false;
        }

        // Validate and save HR review
        boolean success = pendingAppraisal.reviewAppraisal(
            rating,
            performancePoints,
            feedback
        );

        if (!success) {
            return false;
        }

        // Update the shared Employee object
        employee.setPerformanceRating(rating);
        employee.setPerformancePoints(performancePoints);

        System.out.println(
            "\n========== APPRAISAL REVIEW SAVED =========="
        );

        System.out.println(
            "Employee EIS       : " + employeeId
        );

        System.out.println(
            "Employee Name      : " + employee.getName()
        );

        System.out.printf(
            "New Rating         : %.1f / 5.0%n",
            employee.getPerformanceRating()
        );

        System.out.println(
            "Performance Points : "
            + employee.getPerformancePoints()
            + " / 100"
        );

        System.out.println(
            "Review Status      : REVIEWED"
        );

        System.out.println(
            "Employee performance record updated successfully."
        );

        return true;
    }

    // =====================================================
    // 10. DISPLAY CURRENT EMPLOYEE PERFORMANCE
    // =====================================================

    public void displayEmployeePerformance(int employeeId) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println(
            "\n========== MY PERFORMANCE =========="
        );

        System.out.println(
            "Employee ID : " + employee.getEmployeeId()
        );

        System.out.println(
            "Name        : " + employee.getName()
        );

        System.out.printf(
            "Rating      : %.1f / 5.0%n",
            employee.getPerformanceRating()
        );

        System.out.println(
            "Points      : "
            + employee.getPerformancePoints()
            + " / 100"
        );

        System.out.println(
            "\n--- HR APPRAISAL HISTORY ---"
        );

        boolean found = false;

        for (Appraisal appraisal : appraisals) {

            if (appraisal.getEmployeeId() == employeeId) {

                appraisal.displaySummary();
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                "No self-appraisal history available."
            );
        }
    }

    // =====================================================
    // HELPER METHOD
    // =====================================================

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
