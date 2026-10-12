
package models;

import java.time.LocalDate;

public class HousingRequest {

    // Application identification
    private String requestId;
    private int employeeId;

    // Housing eligibility details
    private String grade;
    private String quarterType;
    private boolean hasExistingQuarter;

    // Details used to calculate housing priority
    private LocalDate applicationDate;
    private LocalDate dateOfJoining;
    private double basicSalary;
    private int salaryPoints;

    // PENDING, APPROVED or REJECTED
    private String status;

    // Constructor
    public HousingRequest(String requestId, Employee employee,
            String quarterType,
            boolean hasExistingQuarter,
            LocalDate applicationDate) {

        this.requestId = requestId;

        // Retrieve details directly from the employee record
        this.employeeId = employee.getEmployeeId();
        this.grade = employee.getGrade();
        this.quarterType = quarterType;
        this.hasExistingQuarter = hasExistingQuarter;

        this.applicationDate = applicationDate;
        this.dateOfJoining = employee.getDateOfJoining();
        this.basicSalary = employee.getBasicSalary();

        // One point for every Rs. 100 of basic salary
        this.salaryPoints = (int) (basicSalary / 100);

        // Every new application starts as pending
        this.status = "PENDING";
    }

    // Getters

    public String getRequestId() {
        return requestId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getGrade() {
        return grade;
    }

    public String getQuarterType() {
        return quarterType;
    }

    public boolean hasExistingQuarter() {
        return hasExistingQuarter;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public int getSalaryPoints() {
        return salaryPoints;
    }

    public String getStatus() {
        return status;
    }

    // HR updates status after processing the application
    public void setStatus(String status) {

        if (status.equals("PENDING") ||
                status.equals("APPROVED") ||
                status.equals("REJECTED")) {

            this.status = status;
        }
    }

    // Compare two housing applications for max-heap priority
    // Positive result means this application has higher priority
    public int comparePriority(HousingRequest other) {

        // Priority 1: Employees without quarters first
        if (this.hasExistingQuarter != other.hasExistingQuarter) {
            return this.hasExistingQuarter ? -1 : 1;
        }

        // Priority 2: Earlier application date
        int dateCompare = this.applicationDate
                .compareTo(other.applicationDate);

        if (dateCompare != 0) {
            return -dateCompare;
        }

        // Priority 3: Earlier date of joining
        int joiningCompare = this.dateOfJoining
                .compareTo(other.dateOfJoining);

        if (joiningCompare != 0) {
            return -joiningCompare;
        }

        // Priority 4: Higher salary points
        if (this.salaryPoints != other.salaryPoints) {
            return Integer.compare(
                    this.salaryPoints, other.salaryPoints);
        }

        // Final tie-breaker: Smaller employee ID first
        return Integer.compare(
                other.employeeId, this.employeeId);
    }

    // Display one application in the HR housing table
    public void display() {

        System.out.printf(
                "%-8s %-8d %-7s %-8s %-12s %-10d %-10s%n",
                requestId,
                employeeId,
                grade,
                quarterType,
                applicationDate,
                salaryPoints,
                status);
    }
}
