
package models;

import java.time.LocalDate;

public class Employee {

    // Unique employee identification and login details
    private int employeeId;
    private String password;

    // Personal and organizational information
    private String name;
    private String department;
    private String post;
    private int supervisorId;
    private String grade;
    private long mobileNumber;
    private LocalDate dateOfJoining;

    // Salary and performance details
    private double basicSalary;
    private double performanceRating;
    private int performancePoints;

    // Constructor to initialize an employee record
    public Employee(int employeeId, String password, String name,
            String department, String post, int supervisorId,
            String grade, long mobileNumber, LocalDate dateOfJoining,
            double basicSalary, double performanceRating,
            int performancePoints) {

        this.employeeId = employeeId;
        this.password = password;
        this.name = name;
        this.department = department;
        this.post = post;
        this.supervisorId = supervisorId;
        this.grade = grade;
        this.mobileNumber = mobileNumber;
        this.dateOfJoining = dateOfJoining;
        this.basicSalary = basicSalary;
        this.performanceRating = performanceRating;
        this.performancePoints = performancePoints;
    }

    // Getters provide controlled access to employee information

    public int getEmployeeId() {
        return employeeId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getPost() {
        return post;
    }

    public int getSupervisorId() {
        return supervisorId;
    }

    public String getGrade() {
        return grade;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getPerformanceRating() {
        return performanceRating;
    }

    public int getPerformancePoints() {
        return performancePoints;
    }

    // Setters allow authorized HR operations to update records

    public void setPost(String post) {
        this.post = post;
    }

    public void setSupervisorId(int supervisorId) {
        this.supervisorId = supervisorId;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public void setPerformanceRating(double performanceRating) {
        this.performanceRating = performanceRating;
    }

    public void setPerformancePoints(int performancePoints) {
        this.performancePoints = performancePoints;
    }
}
