

package models;

import java.time.LocalDate;

public class Appraisal {

    private int employeeId;
    private String employeeName;
    private String department;

    // Employee self-appraisal details
    private String achievements;
    private String teamContribution;
    private String challenges;
    private String improvedSkills;
    private String futureGoals;

    private LocalDate submissionDate;

    // HR review details
    private String reviewStatus;
    private double hrRating;
    private int hrPerformancePoints;
    private String hrFeedback;
    private LocalDate reviewDate;

    // Constructor
    public Appraisal(
            Employee employee,
            String achievements,
            String teamContribution,
            String challenges,
            String improvedSkills,
            String futureGoals) {

        this.employeeId = employee.getEmployeeId();
        this.employeeName = employee.getName();
        this.department = employee.getDepartment();

        this.achievements = achievements;
        this.teamContribution = teamContribution;
        this.challenges = challenges;
        this.improvedSkills = improvedSkills;
        this.futureGoals = futureGoals;

        this.submissionDate = LocalDate.now();

        // New appraisals must be reviewed by HR
        this.reviewStatus = "PENDING";
        this.hrRating = 0.0;
        this.hrPerformancePoints = 0;
        this.hrFeedback = "Not reviewed yet";
        this.reviewDate = null;
    }

    // Getters

    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getDepartment() {
        return department;
    }

    public String getAchievements() {
        return achievements;
    }

    public String getTeamContribution() {
        return teamContribution;
    }

    public String getChallenges() {
        return challenges;
    }

    public String getImprovedSkills() {
        return improvedSkills;
    }

    public String getFutureGoals() {
        return futureGoals;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public double getHrRating() {
        return hrRating;
    }

    public int getHrPerformancePoints() {
        return hrPerformancePoints;
    }

    public String getHrFeedback() {
        return hrFeedback;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    // Check whether appraisal has been reviewed
    public boolean isReviewed() {
        return reviewStatus.equals("REVIEWED");
    }

    // HR submits rating and feedback
    public boolean reviewAppraisal(
            double rating,
            int performancePoints,
            String feedback) {

        // Rating must be between 0 and 5
        if (Double.isNaN(rating)
                || Double.isInfinite(rating)
                || rating < 0
                || rating > 5) {

            System.out.println(
                "Rating must be between 0 and 5."
            );
            return false;
        }

        // Points must be between 0 and 100
        if (performancePoints < 0 || performancePoints > 100) {

            System.out.println(
                "Performance points must be between 0 and 100."
            );
            return false;
        }

        if (feedback == null || feedback.trim().isEmpty()) {

            System.out.println("HR feedback cannot be empty.");
            return false;
        }

        if (isReviewed()) {

            System.out.println(
                "This appraisal has already been reviewed."
            );
            return false;
        }

        this.hrRating = rating;
        this.hrPerformancePoints = performancePoints;
        this.hrFeedback = feedback.trim();

        this.reviewStatus = "REVIEWED";
        this.reviewDate = LocalDate.now();

        return true;
    }

    // Display complete self-appraisal and HR review
    public void display() {

        System.out.println("\n========== SELF-APPRAISAL ==========");

        System.out.println("Employee ID       : " + employeeId);
        System.out.println("Employee Name     : " + employeeName);
        System.out.println("Department        : " + department);
        System.out.println("Submission Date   : " + submissionDate);

        System.out.println("\n--- SELF-APPRAISAL DETAILS ---");

        System.out.println("Achievements      : " + achievements);
        System.out.println("Team Contribution : " + teamContribution);
        System.out.println("Challenges        : " + challenges);
        System.out.println("Improved Skills   : " + improvedSkills);
        System.out.println("Future Goals      : " + futureGoals);

        System.out.println("\n--- HR REVIEW ---");

        System.out.println("Review Status     : " + reviewStatus);

        if (isReviewed()) {

            System.out.printf(
                "HR Rating         : %.1f / 5.0%n",
                hrRating
            );

            System.out.println(
                "Performance Points: " + hrPerformancePoints + " / 100"
            );

            System.out.println("HR Feedback       : " + hrFeedback);
            System.out.println("Review Date       : " + reviewDate);

        } else {

            System.out.println(
                "Your self-appraisal is awaiting HR review."
            );
        }

        System.out.println("====================================");
    }

    // Display short summary for HR
    public void displaySummary() {

        System.out.printf(
            "EIS: %-6d | Name: %-20s | Submitted: %s | Status: %s%n",
            employeeId,
            employeeName,
            submissionDate,
            reviewStatus
        );

        if (isReviewed()) {

            System.out.printf(
                "HR Rating: %.1f/5 | Points: %d/100%n",
                hrRating,
                hrPerformancePoints
            );
        }
    }
}
