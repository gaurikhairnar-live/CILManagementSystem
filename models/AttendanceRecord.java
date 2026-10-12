
package models;

public class AttendanceRecord {

    // Employee identification
    private int employeeId;

    // Attendance details
    private int totalWorkingDays;
    private int presentDays;
    private int absentDays;
    private int approvedLeaveDays;

    // Constructor
    public AttendanceRecord(int employeeId, int totalWorkingDays,
            int presentDays, int absentDays,
            int approvedLeaveDays) {

        if (employeeId <= 0 ||
                totalWorkingDays < 0 ||
                presentDays < 0 ||
                absentDays < 0 ||
                approvedLeaveDays < 0 ||
                presentDays + absentDays + approvedLeaveDays != totalWorkingDays) {

            throw new IllegalArgumentException(
                    "Invalid attendance record");
        }

        this.employeeId = employeeId;
        this.totalWorkingDays = totalWorkingDays;
        this.presentDays = presentDays;
        this.absentDays = absentDays;
        this.approvedLeaveDays = approvedLeaveDays;
    }

    // Getters

    public int getEmployeeId() {
        return employeeId;
    }

    public int getTotalWorkingDays() {
        return totalWorkingDays;
    }

    public int getPresentDays() {
        return presentDays;
    }

    public int getAbsentDays() {
        return absentDays;
    }

    public int getApprovedLeaveDays() {
        return approvedLeaveDays;
    }

    // Approved leave days are excluded from attendance calculation
    public double calculateAttendancePercentage() {

        int effectiveWorkingDays = totalWorkingDays - approvedLeaveDays;

        if (effectiveWorkingDays == 0) {
            return 0.0;
        }

        return (presentDays * 100.0) / effectiveWorkingDays;
    }

    // Update attendance after HR approves leave
    public boolean addApprovedLeave(int days) {

        // New leave days are transferred from absent days
        if (days <= 0 || days > absentDays) {
            return false;
        }

        absentDays -= days;
        approvedLeaveDays += days;

        return true;
    }

    // Display attendance details in Employee Portal
    public void display() {

        System.out.println("\n========== MY ATTENDANCE ==========");

        System.out.println("Employee ID          : " + employeeId);
        System.out.println("Total Working Days   : " + totalWorkingDays);
        System.out.println("Present Days         : " + presentDays);
        System.out.println("Absent Days          : " + absentDays);
        System.out.println("Approved Leave Days  : " + approvedLeaveDays);

        System.out.println("-----------------------------------");

        System.out.printf(
                "Attendance Percentage: %.2f%%%n",
                calculateAttendancePercentage());

        System.out.println("===================================");
    }
}
