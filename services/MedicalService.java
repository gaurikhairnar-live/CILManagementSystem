
package services;

import models.Employee;
import dsa.CustomHashTable;

import java.util.ArrayList;

public class MedicalService {

    private CustomHashTable<Integer, Employee> employeeTable;

    // Medical records stored using EIS number as the key
    private CustomHashTable<Integer, MedicalProfile> medicalProfiles;

    // Represents a dependent eligible for medical benefits
    public static class Dependent {

        private String name;
        private String relationship;
        private int age;

        public Dependent(String name, String relationship, int age) {
            this.name = name;
            this.relationship = relationship;
            this.age = age;
        }

        public void display() {
            System.out.printf(
                    "%-20s %-15s %-5d%n",
                    name, relationship, age);
        }
    }

    // Represents a medical referral
    public static class MedicalReferral {

        private String referralId;
        private String hospital;
        private String specialization;
        private String referralDate;
        private String status;

        public MedicalReferral(String referralId,
                String hospital,
                String specialization,
                String referralDate,
                String status) {

            this.referralId = referralId;
            this.hospital = hospital;
            this.specialization = specialization;
            this.referralDate = referralDate;
            this.status = status;
        }

        public void display() {

            System.out.println("Referral ID    : " + referralId);
            System.out.println("Hospital       : " + hospital);
            System.out.println("Specialization : " + specialization);
            System.out.println("Referral Date  : " + referralDate);
            System.out.println("Status         : " + status);
        }
    }

    // Represents a medical history entry
    public static class MedicalHistory {

        private String visitDate;
        private String hospital;
        private String treatment;

        public MedicalHistory(String visitDate,
                String hospital,
                String treatment) {

            this.visitDate = visitDate;
            this.hospital = hospital;
            this.treatment = treatment;
        }

        public void display() {

            System.out.println("Visit Date : " + visitDate);
            System.out.println("Hospital   : " + hospital);
            System.out.println("Treatment  : " + treatment);
        }
    }

    // Medical profile containing all medical-related details
    public static class MedicalProfile {

        private int employeeId;
        private String bloodGroup;
        private String medicalCardNumber;
        private String designatedHospital;

        private ArrayList<Dependent> dependents;
        private ArrayList<MedicalReferral> referrals;
        private ArrayList<MedicalHistory> history;

        public MedicalProfile(int employeeId,
                String bloodGroup,
                String medicalCardNumber,
                String designatedHospital) {

            this.employeeId = employeeId;
            this.bloodGroup = bloodGroup;
            this.medicalCardNumber = medicalCardNumber;
            this.designatedHospital = designatedHospital;

            dependents = new ArrayList<>();
            referrals = new ArrayList<>();
            history = new ArrayList<>();
        }

        public int getEmployeeId() {
            return employeeId;
        }

        public void addDependent(Dependent dependent) {

            if (dependent != null) {
                dependents.add(dependent);
            }
        }

        public void addReferral(MedicalReferral referral) {

            if (referral != null) {
                referrals.add(referral);
            }
        }

        public void addHistory(MedicalHistory record) {

            if (record != null) {
                history.add(record);
            }
        }

        public void displayProfile() {

            System.out.println("Employee ID        : " + employeeId);
            System.out.println("Blood Group        : " + bloodGroup);
            System.out.println("Medical Card Number: " + medicalCardNumber);
            System.out.println("Designated Hospital: " + designatedHospital);
        }

        public void displayDependents() {

            if (dependents.isEmpty()) {
                System.out.println("No registered dependents.");
                return;
            }

            System.out.printf(
                    "%-20s %-15s %-5s%n",
                    "Name", "Relationship", "Age");

            System.out.println("--------------------------------------------");

            for (Dependent dependent : dependents) {
                dependent.display();
            }
        }

        public void displayReferrals() {

            if (referrals.isEmpty()) {
                System.out.println("No medical referrals found.");
                return;
            }

            for (MedicalReferral referral : referrals) {

                referral.display();
                System.out.println("------------------------------");
            }
        }

        public void displayHistory() {

            if (history.isEmpty()) {
                System.out.println("No medical history found.");
                return;
            }

            for (MedicalHistory record : history) {

                record.display();
                System.out.println("------------------------------");
            }
        }
    }

    // Constructor
    public MedicalService(
            CustomHashTable<Integer, Employee> employeeTable) {

        this.employeeTable = employeeTable;
        medicalProfiles = new CustomHashTable<>();
    }

    // Load a predefined medical profile
    public boolean addMedicalProfile(MedicalProfile profile) {

        if (profile == null) {
            return false;
        }

        int employeeId = profile.getEmployeeId();

        if (employeeTable.get(employeeId) == null) {
            return false;
        }

        if (medicalProfiles.containsKey(employeeId)) {
            return false;
        }

        medicalProfiles.put(employeeId, profile);
        return true;
    }

    // Retrieve a medical profile using employee ID
    public MedicalProfile getMedicalProfile(int employeeId) {

        return medicalProfiles.get(employeeId);
    }

    // Employee: View personal medical profile
    public void displayMedicalProfile(int employeeId) {

        MedicalProfile profile = getMedicalProfile(employeeId);

        if (profile == null) {
            System.out.println("Medical profile not found.");
            return;
        }

        Employee employee = employeeTable.get(employeeId);

        System.out.println("\n=== MY MEDICAL PROFILE ===");

        if (employee != null) {
            System.out.println("Employee Name      : "
                    + employee.getName());
        }

        profile.displayProfile();
    }

    // Employee: View dependent medical benefits
    public void displayDependentBenefits(int employeeId) {

        MedicalProfile profile = getMedicalProfile(employeeId);

        if (profile == null) {
            System.out.println("Medical profile not found.");
            return;
        }

        System.out.println("\n=== DEPENDENT MEDICAL BENEFITS ===");

        profile.displayDependents();
    }

    // Employee: View medical referrals
    public void displayMedicalReferrals(int employeeId) {

        MedicalProfile profile = getMedicalProfile(employeeId);

        if (profile == null) {
            System.out.println("Medical profile not found.");
            return;
        }

        System.out.println("\n=== MEDICAL REFERRALS ===");

        profile.displayReferrals();
    }

    // Employee: View medical history
    public void displayMedicalHistory(int employeeId) {

        MedicalProfile profile = getMedicalProfile(employeeId);

        if (profile == null) {
            System.out.println("Medical profile not found.");
            return;
        }

        System.out.println("\n=== MEDICAL HISTORY ===");

        profile.displayHistory();
    }

    // HR: View an employee's complete medical record
    public void displayCompleteMedicalRecord(int employeeId) {

        MedicalProfile profile = getMedicalProfile(employeeId);

        if (profile == null) {
            System.out.println("Medical record not found.");
            return;
        }

        displayMedicalProfile(employeeId);
        displayDependentBenefits(employeeId);
        displayMedicalReferrals(employeeId);
        displayMedicalHistory(employeeId);
    }
}
