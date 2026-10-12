
package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LeaveRequest {

    // Unique identification of the leave application
    private String requestId;
    private int employeeId;
    private String department;

    // Leave application details
    private String leaveType;
    private LocalDate applicationDate;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;
    private int numberOfDays;

    // PENDING, APPROVED or REJECTED
    private String status;

    // Constructor
    public LeaveRequest(String requestId, int employeeId,
            String department, String leaveType,
            LocalDate applicationDate,
            LocalDate fromDate, LocalDate toDate,
            String reason) {

        this.requestId = requestId;
        this.employeeId = employeeId;
        this.department = department;
        this.leaveType = leaveType;
        this.applicationDate = applicationDate;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;

        // Both starting and ending dates are included
        this.numberOfDays = (int) ChronoUnit.DAYS
                .between(fromDate, toDate) + 1;

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

    public String getDepartment() {
        return department;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public String getReason() {
        return reason;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public String getStatus() {
        return status;
    }

    // HR changes the status after processing the request
    public void setStatus(String status) {
        if (status.equals("PENDING") ||
                status.equals("APPROVED") ||
                status.equals("REJECTED")) {

            this.status = status;
        }
    }

    // Priority used by the custom max-heap
    public int getPriority() {

        if (leaveType.equalsIgnoreCase("SICK")) {
            return 3;
        } else if (leaveType.equalsIgnoreCase("CASUAL")) {
            return 2;
        } else if (leaveType.equalsIgnoreCase("PAID")) {
            return 1;
        }

        return 0;
    }

    // Display one application in the HR/Employee leave table
    public void display() {

        System.out.printf(
                "%-7s %-8d %-9s %-12s %-12s %-5d %-10s%n",
                requestId,
                employeeId,
                leaveType,
                fromDate,
                toDate,
                numberOfDays,
                status);
    }
}
