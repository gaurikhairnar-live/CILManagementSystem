
package services;

import models.AttendanceRecord;

public class AttendanceService {

    // Array to store attendance records for 30 employees
    private AttendanceRecord[] attendanceRecords;
    private int count;

    // Constructor
    public AttendanceService() {
        attendanceRecords = new AttendanceRecord[30];
        count = 0;
    }

    // Add an employee's attendance record
    public boolean addAttendanceRecord(AttendanceRecord record) {

        if (record == null || count >= attendanceRecords.length) {
            return false;
        }

        // Prevent duplicate attendance records
        if (getAttendance(record.getEmployeeId()) != null) {
            return false;
        }

        attendanceRecords[count] = record;
        count++;

        return true;
    }

    // Search for an employee's attendance using EIS number
    public AttendanceRecord getAttendance(int employeeId) {

        for (int i = 0; i < count; i++) {

            if (attendanceRecords[i].getEmployeeId() == employeeId) {
                return attendanceRecords[i];
            }
        }

        return null;
    }

    // Calculate attendance percentage for an employee
    public double calculatePercentage(int employeeId) {

        AttendanceRecord record = getAttendance(employeeId);

        if (record == null) {
            return 0.0;
        }

        return record.calculateAttendancePercentage();
    }

    // Update attendance after HR approves a leave application
    public boolean recordApprovedLeave(int employeeId, int days) {

        AttendanceRecord record = getAttendance(employeeId);

        if (record == null || days <= 0) {
            return false;
        }

        return record.addApprovedLeave(days);
    }

    // Display an employee's attendance details
    public void displayAttendance(int employeeId) {

        AttendanceRecord record = getAttendance(employeeId);

        if (record == null) {
            System.out.println("Attendance record not found.");
            return;
        }

        record.display();
    }

    // Return the total number of attendance records
    public int size() {
        return count;
    }
}
