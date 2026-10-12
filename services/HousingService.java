
package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import dsa.CustomHashTable;
import dsa.CustomMaxHeap;
import dsa.CustomMinHeap;

import models.Employee;
import models.HousingRequest;
import models.Quarter;

public class HousingService {

    private CustomHashTable<Integer, Employee> employeeTable;

    // Four separate applicant priority queues
    private CustomMaxHeap<HousingRequest> queueA;
    private CustomMaxHeap<HousingRequest> queueB;
    private CustomMaxHeap<HousingRequest> queueC;
    private CustomMaxHeap<HousingRequest> queueD;

    // Available quarters, ordered by quarter ID
    private CustomMinHeap<Quarter> vacantQuarters;

    // Master records
    private ArrayList<Quarter> allQuarters;
    private ArrayList<HousingRequest> allRequests;

    public HousingService(
            CustomHashTable<Integer, Employee> employeeTable) {

        this.employeeTable = employeeTable;

        Comparator<HousingRequest> priorityComparator =
            new Comparator<HousingRequest>() {

                @Override
                public int compare(
                        HousingRequest first,
                        HousingRequest second) {

                    return first.comparePriority(second);
                }
            };

        queueA = new CustomMaxHeap<>(priorityComparator);
        queueB = new CustomMaxHeap<>(priorityComparator);
        queueC = new CustomMaxHeap<>(priorityComparator);
        queueD = new CustomMaxHeap<>(priorityComparator);

        vacantQuarters = new CustomMinHeap<>(
            new Comparator<Quarter>() {

                @Override
                public int compare(Quarter first, Quarter second) {

                    return first.getQuarterId().compareTo(
                        second.getQuarterId()
                    );
                }
            }
        );

        allQuarters = new ArrayList<>();
        allRequests = new ArrayList<>();
    }

    // =====================================================
    // 1. DETERMINE QUARTER TYPE AUTOMATICALLY
    // =====================================================

    public String getEligibleQuarterType(int employeeId) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {
            return null;
        }

        String grade = employee.getGrade();

        if (grade == null) {
            return null;
        }

        grade = grade.trim().toUpperCase();

        int gradeNumber;

        try {

            if (!grade.matches("E[1-9]")) {
                return null;
            }

            gradeNumber = Integer.parseInt(
                grade.substring(1)
            );

        } catch (NumberFormatException exception) {
            return null;
        }

        if (gradeNumber <= 3) {
            return "A";
        }

        if (gradeNumber <= 5) {
            return "B";
        }

        if (gradeNumber <= 7) {
            return "C";
        }

        return "D";
    }

    // =====================================================
    // 2. SELECT CORRECT PRIORITY QUEUE
    // =====================================================

    private CustomMaxHeap<HousingRequest> getQueue(
            String quarterType) {

        if (quarterType == null) {
            return null;
        }

        switch (quarterType.toUpperCase()) {

            case "A":
                return queueA;

            case "B":
                return queueB;

            case "C":
                return queueC;

            case "D":
                return queueD;

            default:
                return null;
        }
    }

    // =====================================================
    // 3. ADD QUARTERS DURING INITIALIZATION
    // =====================================================

    public void addQuarter(Quarter quarter) {

        if (quarter == null) {
            return;
        }

        for (Quarter existing : allQuarters) {

            if (existing.getQuarterId().equalsIgnoreCase(
                    quarter.getQuarterId())) {

                System.out.println(
                    "Duplicate quarter ID: "
                    + quarter.getQuarterId()
                );

                return;
            }
        }

        allQuarters.add(quarter);

        if (!quarter.isOccupied()) {
            vacantQuarters.insert(quarter);
        }
    }

    // =====================================================
    // 4. ADD EXISTING HOUSING APPLICATIONS
    // =====================================================

    public void addExistingRequest(HousingRequest request) {

        if (request == null) {
            return;
        }

        for (HousingRequest existing : allRequests) {

            if (existing.getRequestId().equalsIgnoreCase(
                    request.getRequestId())) {

                System.out.println(
                    "Duplicate housing request ID."
                );

                return;
            }
        }

        int employeeId = request.getEmployeeId();

        String eligibleType =
            getEligibleQuarterType(employeeId);

        if (eligibleType == null) {

            System.out.println(
                "Invalid employee or grade in housing request."
            );

            return;
        }

        if (!eligibleType.equalsIgnoreCase(
                request.getQuarterType())) {

            System.out.println(
                "Housing request rejected during initialization: "
                + "quarter type does not match employee grade."
            );

            return;
        }

        if ("PENDING".equalsIgnoreCase(request.getStatus())) {

            if (hasPendingApplication(employeeId)) {

                System.out.println(
                    "Duplicate pending application for EIS "
                    + employeeId
                );

                return;
            }
        }

        allRequests.add(request);

        if ("PENDING".equalsIgnoreCase(request.getStatus())) {

            CustomMaxHeap<HousingRequest> queue =
                getQueue(request.getQuarterType());

            queue.insert(request);
        }
    }

    // =====================================================
    // 5. APPLY FOR HOUSING - AUTOMATIC QUARTER TYPE
    // =====================================================

    public void applyForHousing(int employeeId) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        String quarterType =
            getEligibleQuarterType(employeeId);

        if (quarterType == null) {

            System.out.println(
                "Invalid employee grade. Cannot determine eligibility."
            );

            return;
        }

        if (hasPendingApplication(employeeId)) {

            System.out.println(
                "You already have a pending housing application."
            );

            return;
        }

        if (findEmployeeQuarter(employeeId) != null) {

            System.out.println(
                "You already have an allotted quarter. "
                + "Vacate it before applying for another."
            );

            return;
        }

        String requestId = generateRequestId();

        HousingRequest request = new HousingRequest(
            requestId,
            employee,
            quarterType,
            false,
            LocalDate.now()
        );

        allRequests.add(request);

        CustomMaxHeap<HousingRequest> queue =
            getQueue(quarterType);

        queue.insert(request);

        System.out.println(
            "\n========== HOUSING APPLICATION =========="
        );

        System.out.println(
            "Application ID : " + requestId
        );

        System.out.println(
            "Employee EIS   : " + employeeId
        );

        System.out.println(
            "Employee Name  : " + employee.getName()
        );

        System.out.println(
            "Employee Grade : " + employee.getGrade()
        );

        System.out.println(
            "Quarter Type   : " + quarterType
        );

        System.out.println(
            "Status         : PENDING"
        );

        System.out.println(
            "\nApplication submitted successfully!"
        );
    }

    // Backward-compatible method for the old portal.
    // The selected type is ignored intentionally.
    public void applyForHousing(
            int employeeId,
            String ignoredQuarterType) {

        applyForHousing(employeeId);
    }

    // =====================================================
    // 6. DISPLAY ALL FOUR PRIORITY QUEUES
    // =====================================================

    public void displayAllPriorityQueues() {

        displayPriorityQueue("A");
        displayPriorityQueue("B");
        displayPriorityQueue("C");
        displayPriorityQueue("D");
    }

    public void displayPriorityQueue(String quarterType) {

        CustomMaxHeap<HousingRequest> queue =
            getQueue(quarterType);

        if (queue == null) {
            System.out.println("Invalid quarter type.");
            return;
        }

        System.out.println(
            "\n========== TYPE "
            + quarterType.toUpperCase()
            + " PRIORITY QUEUE =========="
        );

        ArrayList<HousingRequest> elements =
            queue.getAllElements();

        if (elements.isEmpty()) {

            System.out.println(
                "No pending applications."
            );

            return;
        }

        // Sort a copy according to the same priority rule
        elements.sort(
            new Comparator<HousingRequest>() {

                @Override
                public int compare(
                        HousingRequest first,
                        HousingRequest second) {

                    return second.comparePriority(first);
                }
            }
        );

        int position = 1;

        for (HousingRequest request : elements) {

            System.out.println(
                "\nPriority Position: " + position
            );

            request.display();

            position++;
        }
    }

    // =====================================================
    // 7. DISPLAY PENDING APPLICATIONS
    // =====================================================

    public void displayPendingApplications() {

        System.out.println(
            "\n========== PENDING HOUSING APPLICATIONS =========="
        );

        displayAllPriorityQueues();
    }

    // =====================================================
    // 8. DISPLAY ALL APPLICATIONS
    // =====================================================

    public void displayAllApplications() {

        System.out.println(
            "\n========== ALL HOUSING APPLICATIONS =========="
        );

        if (allRequests.isEmpty()) {

            System.out.println(
                "No housing applications found."
            );

            return;
        }

        for (HousingRequest request : allRequests) {
            request.display();
        }
    }

    // =====================================================
    // 9. DISPLAY EMPLOYEE APPLICATIONS
    // =====================================================

    public void displayEmployeeApplications(int employeeId) {

        System.out.println(
            "\n========== MY HOUSING APPLICATIONS =========="
        );

        boolean found = false;

        for (HousingRequest request : allRequests) {

            if (request.getEmployeeId() == employeeId) {

                request.display();
                found = true;
            }
        }

        if (!found) {

            System.out.println(
                "You have not submitted any housing applications."
            );
        }
    }

    // =====================================================
    // 10. FIND EMPLOYEE'S ALLOTTED QUARTER
    // =====================================================

    private Quarter findEmployeeQuarter(int employeeId) {

        for (Quarter quarter : allQuarters) {

            if (quarter.isOccupied()
                    && quarter.getOccupantEmployeeId() == employeeId) {

                return quarter;
            }
        }

        return null;
    }

    public void displayEmployeeQuarter(int employeeId) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println(
            "\n========== MY ALLOTTED QUARTER =========="
        );

        Quarter quarter = findEmployeeQuarter(employeeId);

        if (quarter == null) {

            System.out.println(
                "No quarter is currently allotted."
            );

            System.out.println(
                "Eligible Quarter Type: "
                + getEligibleQuarterType(employeeId)
            );

            return;
        }

        quarter.display();
    }

    // =====================================================
    // 11. DISPLAY VACANT QUARTERS
    // =====================================================

    public void displayVacantQuarters() {

        System.out.println(
            "\n========== VACANT QUARTERS =========="
        );

        boolean found = false;

        for (Quarter quarter : allQuarters) {

            if (!quarter.isOccupied()) {

                quarter.display();
                found = true;
            }
        }

        if (!found) {

            System.out.println(
                "No vacant quarters available."
            );
        }
    }

    // =====================================================
    // 12. DISPLAY COLONY-WISE VACANCIES
    // =====================================================

    public void displayColonyVacancies() {

        System.out.println(
            "\n========== COLONY-WISE VACANCIES =========="
        );

        ArrayList<String> colonies = new ArrayList<>();

        for (Quarter quarter : allQuarters) {

            if (!colonies.contains(quarter.getColony())) {
                colonies.add(quarter.getColony());
            }
        }

        for (String colony : colonies) {

            System.out.println(
                "\nColony: " + colony
            );

            int total = 0;
            int vacant = 0;

            for (Quarter quarter : allQuarters) {

                if (quarter.getColony().equals(colony)) {

                    total++;

                    if (!quarter.isOccupied()) {

                        vacant++;
                        quarter.display();
                    }
                }
            }

            System.out.println(
                "Total Quarters : " + total
            );

            System.out.println(
                "Vacant Quarters: " + vacant
            );
        }
    }

    // =====================================================
    // 13. DISPLAY ALL QUARTERS
    // =====================================================

    public void displayAllQuarters() {

        System.out.println(
            "\n========== ALL QUARTERS =========="
        );

        if (allQuarters.isEmpty()) {

            System.out.println(
                "No quarters available."
            );

            return;
        }

        for (Quarter quarter : allQuarters) {
            quarter.display();
        }
    }

    // =====================================================
    // 14. FIND AVAILABLE QUARTER BY TYPE
    // =====================================================

    private Quarter findVacantQuarter(String quarterType) {

        // Scan the inventory for a matching vacant quarter.
        // Choose the smallest quarter ID for deterministic allotment.
        Quarter selected = null;

        for (Quarter quarter : allQuarters) {

            if (!quarter.isOccupied()
                    && quarter.getQuarterType().equalsIgnoreCase(
                        quarterType)) {

                if (selected == null
                        || quarter.getQuarterId().compareTo(
                            selected.getQuarterId()) < 0) {

                    selected = quarter;
                }
            }
        }

        return selected;
    }

    // =====================================================
    // 15. AUTOMATIC QUARTER ALLOTMENT BY EMPLOYEE EIS
    // =====================================================

    public boolean autoAllotQuarter(int employeeId) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {

            System.out.println("Employee not found.");
            return false;
        }

        String eligibleType =
            getEligibleQuarterType(employeeId);

        if (eligibleType == null) {

            System.out.println(
                "Invalid employee grade."
            );

            return false;
        }

        if (findEmployeeQuarter(employeeId) != null) {

            System.out.println(
                "Employee already occupies a quarter."
            );

            return false;
        }

        CustomMaxHeap<HousingRequest> queue =
            getQueue(eligibleType);

        if (queue == null || queue.isEmpty()) {

            System.out.println(
                "No pending applications in Type "
                + eligibleType
                + " queue."
            );

            return false;
        }

        HousingRequest highestPriority =
            queue.peek();

        if (highestPriority == null) {
            return false;
        }

        if (highestPriority.getEmployeeId() != employeeId) {

            System.out.println(
                "\nAllotment blocked: Priority queue order must be followed."
            );

            System.out.println(
                "Highest-priority applicant EIS: "
                + highestPriority.getEmployeeId()
            );

            return false;
        }

        Quarter selectedQuarter =
            findVacantQuarter(eligibleType);

        if (selectedQuarter == null) {

            System.out.println(
                "No vacant Type "
                + eligibleType
                + " quarter is available."
            );

            return false;
        }

        boolean allotted =
            selectedQuarter.allotQuarter(employeeId);

        if (!allotted) {

            System.out.println(
                "Quarter allotment failed."
            );

            return false;
        }

        highestPriority.setStatus("APPROVED");

        // Remove the approved application from its queue
        queue.extractMax();

        // Rebuild the vacancy heap to remove the occupied quarter
        rebuildVacancyHeap();

        System.out.println(
            "\n========== AUTOMATIC QUARTER ALLOTMENT =========="
        );

        System.out.println(
            "Employee EIS  : " + employeeId
        );

        System.out.println(
            "Employee Name : " + employee.getName()
        );

        System.out.println(
            "Grade         : " + employee.getGrade()
        );

        System.out.println(
            "Quarter Type  : " + eligibleType
        );

        System.out.println(
            "Quarter ID    : " + selectedQuarter.getQuarterId()
        );

        System.out.println(
            "Colony        : " + selectedQuarter.getColony()
        );

        System.out.println(
            "Status        : ALLOTTED"
        );

        System.out.println(
            "\nQuarter allotted successfully!"
        );

        return true;
    }

    // =====================================================
    // 16. BACKWARD-COMPATIBLE ALLOTMENT METHOD
    // =====================================================

    public void allotQuarter(
            int employeeId,
            String quarterType,
            String quarterId) {

        // The old interface accepted a manually selected
        // quarter type and quarter ID.
        // Both are intentionally ignored now.
        autoAllotQuarter(employeeId);
    }

    // =====================================================
    // 17. REJECT APPLICATION
    // =====================================================

    public void rejectApplication(
            int employeeId,
            String requestId) {

        HousingRequest selected = null;

        for (HousingRequest request : allRequests) {

            if (request.getEmployeeId() == employeeId
                    && request.getRequestId().equalsIgnoreCase(
                        requestId)
                    && "PENDING".equalsIgnoreCase(
                        request.getStatus())) {

                selected = request;
                break;
            }
        }

        if (selected == null) {

            System.out.println(
                "Pending application not found."
            );

            return;
        }

        selected.setStatus("REJECTED");

        rebuildApplicationQueues();

        System.out.println(
            "Housing application rejected successfully."
        );
    }

    // =====================================================
    // 18. EMPLOYEE VACATES OWN QUARTER
    // =====================================================

    public boolean vacateEmployeeQuarter(int employeeId) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {

            System.out.println("Employee not found.");
            return false;
        }

        Quarter quarter = findEmployeeQuarter(employeeId);

        if (quarter == null) {

            System.out.println(
                "No allotted quarter found for this employee."
            );

            return false;
        }

        String oldQuarterId = quarter.getQuarterId();

        boolean vacated = quarter.vacateQuarter();

        if (!vacated) {

            System.out.println(
                "Unable to vacate quarter."
            );

            return false;
        }

        rebuildVacancyHeap();

        System.out.println(
            "\n========== QUARTER VACATED =========="
        );

        System.out.println(
            "Employee EIS : " + employeeId
        );

        System.out.println(
            "Employee Name: " + employee.getName()
        );

        System.out.println(
            "Quarter ID   : " + oldQuarterId
        );

        System.out.println(
            "Status       : VACANT"
        );

        System.out.println(
            "\nQuarter is now available for allotment."
        );

        return true;
    }

    // =====================================================
    // 19. HR VACATES QUARTER USING EMPLOYEE EIS
    // =====================================================

    public boolean hrVacateEmployeeQuarter(int employeeId) {

        return vacateEmployeeQuarter(employeeId);
    }

    // =====================================================
    // 20. EXISTING VACATE METHOD BY QUARTER ID
    // =====================================================

    public void vacateQuarter(String quarterId) {

        Quarter quarter = null;

        for (Quarter existing : allQuarters) {

            if (existing.getQuarterId().equalsIgnoreCase(
                    quarterId)) {

                quarter = existing;
                break;
            }
        }

        if (quarter == null) {

            System.out.println(
                "Quarter not found."
            );

            return;
        }

        if (!quarter.isOccupied()) {

            System.out.println(
                "Quarter is already vacant."
            );

            return;
        }

        vacateEmployeeQuarter(
            quarter.getOccupantEmployeeId()
        );
    }

    // =====================================================
    // 21. CHECK FOR PENDING APPLICATION
    // =====================================================

    private boolean hasPendingApplication(int employeeId) {

        for (HousingRequest request : allRequests) {

            if (request.getEmployeeId() == employeeId
                    && "PENDING".equalsIgnoreCase(
                        request.getStatus())) {

                return true;
            }
        }

        return false;
    }

    // =====================================================
    // 22. GENERATE UNIQUE REQUEST ID
    // =====================================================

    private String generateRequestId() {

        int number = 1;

        while (true) {

            String requestId =
                String.format("HQ%03d", number);

            boolean exists = false;

            for (HousingRequest request : allRequests) {

                if (request.getRequestId().equalsIgnoreCase(
                        requestId)) {

                    exists = true;
                    break;
                }
            }

            if (!exists) {
                return requestId;
            }

            number++;
        }
    }

    // =====================================================
    // 23. REBUILD ALL APPLICATION PRIORITY QUEUES
    // =====================================================

    private void rebuildApplicationQueues() {

        Comparator<HousingRequest> comparator =
            new Comparator<HousingRequest>() {

                @Override
                public int compare(
                        HousingRequest first,
                        HousingRequest second) {

                    return first.comparePriority(second);
                }
            };

        queueA = new CustomMaxHeap<>(comparator);
        queueB = new CustomMaxHeap<>(comparator);
        queueC = new CustomMaxHeap<>(comparator);
        queueD = new CustomMaxHeap<>(comparator);

        for (HousingRequest request : allRequests) {

            if ("PENDING".equalsIgnoreCase(
                    request.getStatus())) {

                CustomMaxHeap<HousingRequest> queue =
                    getQueue(request.getQuarterType());

                if (queue != null) {
                    queue.insert(request);
                }
            }
        }
    }

    // =====================================================
    // 24. REBUILD AVAILABLE QUARTER MIN HEAP
    // =====================================================

    private void rebuildVacancyHeap() {

        vacantQuarters = new CustomMinHeap<>(
            new Comparator<Quarter>() {

                @Override
                public int compare(
                        Quarter first,
                        Quarter second) {

                    return first.getQuarterId().compareTo(
                        second.getQuarterId()
                    );
                }
            }
        );

        for (Quarter quarter : allQuarters) {

            if (!quarter.isOccupied()) {
                vacantQuarters.insert(quarter);
            }
        }
    }
}
