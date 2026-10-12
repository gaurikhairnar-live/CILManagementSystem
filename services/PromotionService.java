
package services;

import models.Employee;

import dsa.CustomHashTable;
import dsa.CustomMaxHeap;
import dsa.OrganizationGraph;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;

public class PromotionService {

    private CustomHashTable<Integer, Employee> employeeTable;
    private OrganizationGraph organizationGraph;

    private CustomMaxHeap<Employee> promotionHeap;

    // Stores promotion history
    private ArrayList<PromotionRecord> promotionHistory;

    // Simplified project eligibility rules
    private static final int MIN_EXPERIENCE = 3;
    private static final double MIN_RATING = 4.0;
    private static final int MIN_POINTS = 75;

    public PromotionService(
            CustomHashTable<Integer, Employee> employeeTable,
            OrganizationGraph organizationGraph) {

        this.employeeTable = employeeTable;
        this.organizationGraph = organizationGraph;

        promotionHistory = new ArrayList<>();

        promotionHeap = new CustomMaxHeap<>(
                getPromotionComparator());
    }

    // Represents one promotion-history entry
    public static class PromotionRecord {

        private int employeeId;
        private String oldGrade;
        private String newGrade;
        private String oldPost;
        private String newPost;
        private LocalDate promotionDate;

        public PromotionRecord(int employeeId,
                String oldGrade,
                String newGrade,
                String oldPost,
                String newPost,
                LocalDate promotionDate) {

            this.employeeId = employeeId;
            this.oldGrade = oldGrade;
            this.newGrade = newGrade;
            this.oldPost = oldPost;
            this.newPost = newPost;
            this.promotionDate = promotionDate;
        }

        public int getEmployeeId() {
            return employeeId;
        }

        public LocalDate getPromotionDate() {
            return promotionDate;
        }

        public void display() {

            System.out.println("Employee ID   : " + employeeId);
            System.out.println("Old Grade     : " + oldGrade);
            System.out.println("New Grade     : " + newGrade);
            System.out.println("Old Post      : " + oldPost);
            System.out.println("New Post      : " + newPost);
            System.out.println("Promotion Date: " + promotionDate);
        }
    }

    // Extract numeric grade level, e.g. E5 -> 5
    private int getGradeLevel(String grade) {

        if (grade == null || !grade.matches("E\\d+")) {
            return -1;
        }

        return Integer.parseInt(grade.substring(1));
    }

    // Calculate years of service
    public int getExperienceYears(Employee employee) {

        if (employee == null) {
            return 0;
        }

        return Math.max(
                0,
                Period.between(
                        employee.getDateOfJoining(),
                        LocalDate.now()).getYears());
    }

    // Check employee promotion eligibility
    public boolean isEligible(Employee employee) {

        if (employee == null) {
            return false;
        }

        int gradeLevel = getGradeLevel(employee.getGrade());

        if (gradeLevel < 1 || gradeLevel >= 9) {
            return false;
        }

        if (getExperienceYears(employee) < MIN_EXPERIENCE) {
            return false;
        }

        if (employee.getPerformanceRating() < MIN_RATING) {
            return false;
        }

        if (employee.getPerformancePoints() < MIN_POINTS) {
            return false;
        }

        if (employee.getBasicSalary() <= 0) {
            return false;
        }

        return true;
    }

    // Comparator for promotion priority
    private Comparator<Employee> getPromotionComparator() {

        return (a, b) -> {

            // Higher performance points first
            int pointsComparison = Integer.compare(
                    a.getPerformancePoints(),
                    b.getPerformancePoints());

            if (pointsComparison != 0) {
                return pointsComparison;
            }

            // Higher performance rating first
            int ratingComparison = Double.compare(
                    a.getPerformanceRating(),
                    b.getPerformanceRating());

            if (ratingComparison != 0) {
                return ratingComparison;
            }

            // More experience first
            int experienceComparison = Integer.compare(
                    getExperienceYears(a),
                    getExperienceYears(b));

            if (experienceComparison != 0) {
                return experienceComparison;
            }

            // Higher salary first
            int salaryComparison = Double.compare(
                    a.getBasicSalary(),
                    b.getBasicSalary());

            if (salaryComparison != 0) {
                return salaryComparison;
            }

            // Lower EIS first
            return Integer.compare(
                    b.getEmployeeId(),
                    a.getEmployeeId());
        };
    }

    // Build priority heap using eligible employees
    public void buildPromotionPriorityQueue() {

        promotionHeap = new CustomMaxHeap<>(
                getPromotionComparator());

        // Initial dataset has EIS numbers 1001 to 1030
        for (int id = 1001; id <= 1030; id++) {

            Employee employee = employeeTable.get(id);

            if (isEligible(employee)) {
                promotionHeap.insert(employee);
            }
        }
    }

    // Get highest-priority eligible employee
    public Employee getTopPromotionCandidate() {

        buildPromotionPriorityQueue();

        return promotionHeap.peek();
    }

    // Display promotion eligibility for an employee
    public void displayPromotionEligibility(int employeeId) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("\n=== PROMOTION ELIGIBILITY ===");

        System.out.println("Employee ID       : "
                + employee.getEmployeeId());

        System.out.println("Name              : "
                + employee.getName());

        System.out.println("Current Grade     : "
                + employee.getGrade());

        System.out.println("Experience        : "
                + getExperienceYears(employee) + " years");

        System.out.println("Basic Salary      : Rs. "
                + employee.getBasicSalary());

        System.out.println("Performance Rating: "
                + employee.getPerformanceRating());

        System.out.println("Performance Points: "
                + employee.getPerformancePoints());

        System.out.println("--------------------------------");

        if (isEligible(employee)) {

            System.out.println("Status: ELIGIBLE");

            System.out.println(
                    "Proposed Grade: E"
                            + (getGradeLevel(employee.getGrade()) + 1));

        } else {

            System.out.println("Status: NOT ELIGIBLE");

            if (getExperienceYears(employee) < MIN_EXPERIENCE) {
                System.out.println(
                        "- Minimum 3 years of service required.");
            }

            if (employee.getPerformanceRating() < MIN_RATING) {
                System.out.println(
                        "- Minimum performance rating of 4.0 required.");
            }

            if (employee.getPerformancePoints() < MIN_POINTS) {
                System.out.println(
                        "- Minimum 75 performance points required.");
            }

            if (employee.getBasicSalary() <= 0) {
                System.out.println(
                        "- A valid basic salary is required.");
            }

            if (getGradeLevel(employee.getGrade()) < 1
                    || getGradeLevel(employee.getGrade()) >= 9) {

                System.out.println(
                        "- Employee grade must be between E1 and E8.");
            }
        }
    }

    // HR views eligible employees in priority order
    public void displayPromotionPriorityList() {

        buildPromotionPriorityQueue();

        if (promotionHeap.isEmpty()) {
            System.out.println("No eligible employees found.");
            return;
        }

        // Temporary heap so displaying does not alter main heap
        CustomMaxHeap<Employee> tempHeap = new CustomMaxHeap<>(getPromotionComparator());

        for (Employee employee : promotionHeap.getAllElements()) {
            tempHeap.insert(employee);
        }

        System.out.println("\n=== PROMOTION PRIORITY LIST ===");

        System.out.printf(
                "%-6s %-8s %-20s %-7s %-10s %-10s%n",
                "Rank", "EIS", "Name", "Grade", "Rating", "Points");

        int rank = 1;

        while (!tempHeap.isEmpty()) {

            Employee employee = tempHeap.extractMax();

            System.out.printf(
                    "%-6d %-8d %-20s %-7s %-10.1f %-10d%n",
                    rank++,
                    employee.getEmployeeId(),
                    employee.getName(),
                    employee.getGrade(),
                    employee.getPerformanceRating(),
                    employee.getPerformancePoints());
        }
    }

    // HR promotes an eligible employee
    public boolean promoteEmployee(int employeeId,
            String newPost) {

        Employee employee = employeeTable.get(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
            return false;
        }

        if (!isEligible(employee)) {
            System.out.println(
                    "Employee does not meet promotion eligibility.");
            return false;
        }

        if (newPost == null || newPost.trim().isEmpty()) {
            System.out.println("New designation is required.");
            return false;
        }

        String oldGrade = employee.getGrade();
        String oldPost = employee.getPost();

        String newGrade = "E" +
                (getGradeLevel(oldGrade) + 1);

        // Update employee details
        employee.setGrade(newGrade);
        employee.setPost(newPost.trim());

        // Record the promotion
        PromotionRecord record = new PromotionRecord(
                employeeId,
                oldGrade,
                newGrade,
                oldPost,
                newPost.trim(),
                LocalDate.now());

        promotionHistory.add(record);

        buildPromotionPriorityQueue();

        System.out.println(
                "Employee promoted successfully from "
                        + oldGrade + " to " + newGrade);

        return true;
    }

    // Add predefined promotion history
    public void addPromotionHistory(PromotionRecord record) {

        if (record != null) {
            promotionHistory.add(record);
        }
    }

    // Employee views own promotion history
    public void displayEmployeePromotionHistory(int employeeId) {

        boolean found = false;

        System.out.println("\n=== MY PROMOTION HISTORY ===");

        for (PromotionRecord record : promotionHistory) {

            if (record.getEmployeeId() == employeeId) {

                record.display();
                System.out.println("----------------------------");

                found = true;
            }
        }

        if (!found) {
            System.out.println("No promotion history found.");
        }
    }

    // HR views all promotion records
    public void displayAllPromotionHistory() {

        if (promotionHistory.isEmpty()) {
            System.out.println("No promotion history found.");
            return;
        }

        System.out.println("\n=== ALL PROMOTION RECORDS ===");

        for (PromotionRecord record : promotionHistory) {

            record.display();
            System.out.println("----------------------------");
        }
    }

    // HR views organizational hierarchy using BFS
    public void displayHierarchyBFS(int startingEmployeeId) {

        int[] result = organizationGraph.bfs(
                startingEmployeeId);

        System.out.println("\n=== ORGANIZATION BFS ===");

        for (int id : result) {

            Employee employee = employeeTable.get(id);

            if (employee != null) {

                System.out.println(
                        employee.getEmployeeId() + " - "
                                + employee.getName() + " ("
                                + employee.getPost() + ")");
            }
        }
    }

    // HR views organizational hierarchy using DFS
    public void displayHierarchyDFS(int startingEmployeeId) {

        int[] result = organizationGraph.dfs(
                startingEmployeeId);

        System.out.println("\n=== ORGANIZATION DFS ===");

        for (int id : result) {

            Employee employee = employeeTable.get(id);

            if (employee != null) {

                System.out.println(
                        employee.getEmployeeId() + " - "
                                + employee.getName() + " ("
                                + employee.getPost() + ")");
            }
        }
    }

    public int getPromotionHistoryCount() {
        return promotionHistory.size();
    }
}
