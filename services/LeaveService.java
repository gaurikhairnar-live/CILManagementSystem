
package services;

import models.Employee;
import models.LeaveRequest;
import dsa.CustomMaxHeap;
import dsa.CustomHashTable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;

public class LeaveService {

    private ArrayList<LeaveRequest> leaveRequests;

    private CustomHashTable<Integer, Employee> employeeTable;

    private AttendanceService attendanceService;

    // Higher leave priority is processed first.
    // If priority is equal, earlier application comes first.
    private CustomMaxHeap<LeaveRequest> pendingHeap;

    private int nextRequestNumber;

    public LeaveService(
            CustomHashTable<Integer, Employee> employeeTable,
            AttendanceService attendanceService) {

        this.employeeTable = employeeTable;
        this.attendanceService = attendanceService;

        leaveRequests = new ArrayList<>();

        Comparator<LeaveRequest> priorityComparator = (a, b) -> {

            int priority = Integer.compare(
                    a.getPriority(), b.getPriority());

            if (priority != 0) {
                return priority;
            }

            int dateComparison = b.getApplicationDate()
                    .compareTo(a.getApplicationDate());

            if (dateComparison != 0) {
                return dateComparison;
            }

            return b.getRequestId().compareTo(a.getRequestId());
        };

        pendingHeap = new CustomMaxHeap<>(priorityComparator);

        nextRequestNumber = 1;
    }

    // Maximum leave entitlement for each type
    private int getLeaveLimit(String leaveType) {

        switch (leaveType.toLowerCase()) {
            case "sick":
                return 30;
            case "casual":
                return 12;
            case "paid":
                return 15;
            default:
                return -1;
        }
    }

    // Normalize leave type
    private String normalizeLeaveType(String leaveType) {

        if (leaveType == null) {
            return null;
        }

        switch (leaveType.trim().toLowerCase()) {
            case "sick":
                return "Sick";
            case "casual":
                return "Casual";
            case "paid":
                return "Paid";
            default:
                return null;
        }
    }

    // Calculate total approved leave days of a particular type
    public int getUsedLeaveDays(int employeeId, String leaveType) {

        int usedDays = 0;

        for (LeaveRequest request : leaveRequests) {

            if (request.getEmployeeId() == employeeId
                    && request.getLeaveType()
                            .equalsIgnoreCase(leaveType)
                    && request.getStatus()
                            .equalsIgnoreCase("APPROVED")) {

                usedDays += request.getNumberOfDays();
            }
        }

        return usedDays;
    }

    // Calculate remaining leave balance
    public int getRemainingLeave(int employeeId, String leaveType) {

        String type = normalizeLeaveType(leaveType);

        if (type == null) {
            return -1;
        }

        return Math.max(
                0,
                getLeaveLimit(type) - getUsedLeaveDays(employeeId, type));
    }

    // Check whether the leave satisfies the notice period
    private boolean isNoticeValid(String leaveType,
            LocalDate applicationDate,
            LocalDate fromDate) {

        long noticeDays = ChronoUnit.DAYS.between(
                applicationDate, fromDate);

        switch (leaveType.toLowerCase()) {

            case "sick":
                return noticeDays >= 0;

            case "casual":
                return noticeDays >= 1;

            case "paid":
                return noticeDays >= 7;

            default:
                return false;
        }
    }

    // Check for overlapping active leave applications
    private boolean hasOverlappingLeave(int employeeId,
            LocalDate fromDate,
            LocalDate toDate) {

        for (LeaveRequest request : leaveRequests) {

            if (request.getEmployeeId() != employeeId) {
                continue;
            }

            if (request.getStatus().equalsIgnoreCase("REJECTED")) {
                continue;
            }

            boolean overlaps = !fromDate.isAfter(request.getToDate())
                    && !toDate.isBefore(request.getFromDate());

            if (overlaps) {
                return true;
            }
        }

        return false;
    }

    // Submit a new leave application
    public LeaveRequest applyForLeave(int employeeId,
            String leaveType,
            LocalDate fromDate,
            LocalDate toDate,
            String reason) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return null;
        }

        String type = normalizeLeaveType(leaveType);

        if (type == null) {
            System.out.println("Invalid leave type.");
            return null;
        }

        if (fromDate == null || toDate == null
                || fromDate.isAfter(toDate)) {

            System.out.println("Invalid leave dates.");
            return null;
        }

        if (reason == null || reason.trim().isEmpty()) {
            System.out.println("Leave reason is required.");
            return null;
        }

        LocalDate today = LocalDate.now();

        if (!isNoticeValid(type, today, fromDate)) {
            System.out.println(
                    "Notice period not satisfied for " + type + " leave.");
            return null;
        }

        int requestedDays = (int) ChronoUnit.DAYS.between(
                fromDate, toDate) + 1;

        int remainingDays = getRemainingLeave(employeeId, type);

        // Also reserve days from pending requests
        int pendingDays = 0;

        for (LeaveRequest request : leaveRequests) {

            if (request.getEmployeeId() == employeeId
                    && request.getLeaveType().equalsIgnoreCase(type)
                    && request.getStatus().equalsIgnoreCase("PENDING")) {

                pendingDays += request.getNumberOfDays();
            }
        }

        if (requestedDays > remainingDays - pendingDays) {

            System.out.println(
                    "Insufficient leave balance.");

            return null;
        }

        if (hasOverlappingLeave(employeeId, fromDate, toDate)) {

            System.out.println(
                    "An active leave application already overlaps these dates.");

            return null;
        }

        String requestId = String.format(
                "LV%03d", nextRequestNumber++);

        LeaveRequest request = new LeaveRequest(
                requestId,
                employeeId,
                employee.getDepartment(),
                type,
                today,
                fromDate,
                toDate,
                reason.trim());

        leaveRequests.add(request);
        pendingHeap.insert(request);

        System.out.println(
                "Leave application submitted successfully. ID: "
                        + requestId);

        return request;
    }

    // Load predefined leave applications
    public boolean addExistingRequest(LeaveRequest request) {

        if (request == null
                || employeeTable.get(request.getEmployeeId()) == null) {
            return false;
        }

        for (LeaveRequest existing : leaveRequests) {

            if (existing.getRequestId()
                    .equalsIgnoreCase(request.getRequestId())) {
                return false;
            }
        }

        leaveRequests.add(request);

        if (request.getStatus().equalsIgnoreCase("PENDING")) {
            pendingHeap.insert(request);
        }

        // Prevent IDs from clashing with predefined requests
        String id = request.getRequestId();

        if (id.matches("LV\\d+")) {
            int number = Integer.parseInt(id.substring(2));
            nextRequestNumber = Math.max(
                    nextRequestNumber, number + 1);
        }

        return true;
    }

    // Count employees belonging to a department
    private int getDepartmentEmployeeCount(String department) {

        int count = 0;

        // Employee IDs in the finalized dataset: 1001 to 1030
        for (int id = 1001; id <= 1030; id++) {

            Employee employee = employeeTable.get(id);

            if (employee != null
                    && employee.getDepartment()
                            .equalsIgnoreCase(department)) {

                count++;
            }
        }

        return count;
    }

    // Department can have at most 20% employees on leave
    public int getDepartmentLeaveCapacity(String department) {

        int totalEmployees = getDepartmentEmployeeCount(department);

        if (totalEmployees == 0) {
            return 0;
        }

        return Math.max(1, (int) Math.floor(totalEmployees * 0.20));
    }

    // Check concurrent approved leave on every requested date
    private boolean isDepartmentCapacityAvailable(
            LeaveRequest newRequest) {

        int capacity = getDepartmentLeaveCapacity(
                newRequest.getDepartment());

        if (capacity == 0) {
            return false;
        }

        LocalDate date = newRequest.getFromDate();

        while (!date.isAfter(newRequest.getToDate())) {

            int employeesOnLeave = 0;

            for (LeaveRequest request : leaveRequests) {

                if (request == newRequest) {
                    continue;
                }

                if (!request.getDepartment().equalsIgnoreCase(
                        newRequest.getDepartment())) {
                    continue;
                }

                if (!request.getStatus().equalsIgnoreCase("APPROVED")) {
                    continue;
                }

                if (!date.isBefore(request.getFromDate())
                        && !date.isAfter(request.getToDate())) {

                    employeesOnLeave++;
                }
            }

            if (employeesOnLeave >= capacity) {
                return false;
            }

            date = date.plusDays(1);
        }

        return true;
    }

    // Find a leave application using employee ID and request ID
    public LeaveRequest findRequest(int employeeId, String requestId) {

        if (requestId == null) {
            return null;
        }

        for (LeaveRequest request : leaveRequests) {

            if (request.getEmployeeId() == employeeId
                    && request.getRequestId()
                            .equalsIgnoreCase(requestId)) {

                return request;
            }
        }

        return null;
    }

    // HR approves a leave request
    public boolean approveLeave(int employeeId, String requestId) {

        LeaveRequest request = findRequest(employeeId, requestId);

        if (request == null) {
            System.out.println("Leave request not found.");
            return false;
        }

        if (!request.getStatus().equalsIgnoreCase("PENDING")) {
            System.out.println("Request is already processed.");
            return false;
        }

        int remainingDays = getRemainingLeave(
                employeeId, request.getLeaveType());

        if (request.getNumberOfDays() > remainingDays) {
            System.out.println("Insufficient remaining leave balance.");
            return false;
        }

        if (!isDepartmentCapacityAvailable(request)) {
            System.out.println(
                    "Cannot approve: department leave capacity exceeded.");
            return false;
        }

        request.setStatus("APPROVED");

        // Only convert already-recorded absences into approved leave.
        // Future leave does not automatically change attendance totals.
        if (!request.getFromDate().isAfter(LocalDate.now())) {

            System.out.println(
                    "Leave approved. Update attendance separately if "
                            + "these dates were previously marked absent.");
        }

        rebuildPendingHeap();

        System.out.println("Leave request approved.");
        return true;
    }

    // HR rejects a leave request
    public boolean rejectLeave(int employeeId, String requestId) {

        LeaveRequest request = findRequest(employeeId, requestId);

        if (request == null) {
            System.out.println("Leave request not found.");
            return false;
        }

        if (!request.getStatus().equalsIgnoreCase("PENDING")) {
            System.out.println("Request is already processed.");
            return false;
        }

        request.setStatus("REJECTED");

        rebuildPendingHeap();

        System.out.println("Leave request rejected.");
        return true;
    }

    // Rebuild heap after approval or rejection
    private void rebuildPendingHeap() {

        Comparator<LeaveRequest> comparator = (a, b) -> {

            int priority = Integer.compare(
                    a.getPriority(), b.getPriority());

            if (priority != 0) {
                return priority;
            }

            int dateComparison = b.getApplicationDate()
                    .compareTo(a.getApplicationDate());

            if (dateComparison != 0) {
                return dateComparison;
            }

            return b.getRequestId().compareTo(a.getRequestId());
        };

        pendingHeap = new CustomMaxHeap<>(comparator);

        for (LeaveRequest request : leaveRequests) {

            if (request.getStatus().equalsIgnoreCase("PENDING")) {
                pendingHeap.insert(request);
            }
        }
    }

    // Display pending applications in priority order
    public void displayPendingRequests() {

        ArrayList<LeaveRequest> pending = pendingHeap.getAllElements();

        if (pending.isEmpty()) {
            System.out.println("No pending leave applications.");
            return;
        }

        // Use a temporary heap to display in actual priority order
        CustomMaxHeap<LeaveRequest> tempHeap = new CustomMaxHeap<>((a, b) -> {

            int priority = Integer.compare(
                    a.getPriority(), b.getPriority());

            if (priority != 0) {
                return priority;
            }

            int dateComparison = b.getApplicationDate()
                    .compareTo(a.getApplicationDate());

            if (dateComparison != 0) {
                return dateComparison;
            }

            return b.getRequestId().compareTo(a.getRequestId());
        });

        for (LeaveRequest request : pending) {
            tempHeap.insert(request);
        }

        System.out.println("\n=== PENDING LEAVE REQUESTS ===");

        while (!tempHeap.isEmpty()) {
            LeaveRequest request = tempHeap.extractMax();
            request.display();
            System.out.println("----------------------------");
        }
    }

    // Display every leave application for HR
    public void displayAllRequests() {

        if (leaveRequests.isEmpty()) {
            System.out.println("No leave applications found.");
            return;
        }

        System.out.println("\n=== ALL LEAVE REQUESTS ===");

        for (LeaveRequest request : leaveRequests) {
            request.display();
            System.out.println("----------------------------");
        }
    }

    // Display an employee's leave history
    public void displayEmployeeRequests(int employeeId) {

        boolean found = false;

        for (LeaveRequest request : leaveRequests) {

            if (request.getEmployeeId() == employeeId) {
                request.display();
                System.out.println("----------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No leave applications found.");
        }
    }

    // Display employee leave balances
    public void displayLeaveBalance(int employeeId) {

        System.out.println("\n=== LEAVE BALANCE ===");

        System.out.println(
                "Sick Leave   : " + getRemainingLeave(employeeId, "Sick")
                        + " / 30");

        System.out.println(
                "Casual Leave : " + getRemainingLeave(employeeId, "Casual")
                        + " / 12");

        System.out.println(
                "Paid Leave   : " + getRemainingLeave(employeeId, "Paid")
                        + " / 15");
    }

    public int getTotalRequests() {
        return leaveRequests.size();
    }
}
