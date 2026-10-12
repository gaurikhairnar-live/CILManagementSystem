

package data;

import java.time.LocalDate;

import models.Employee;

import models.AttendanceRecord;

import models.LeaveRequest;

import models.PayrollRecord;

import models.HousingRequest;

import models.Quarter;

import models.Appraisal;

import dsa.EmployeeLinkedList;

import dsa.CustomHashTable;

import dsa.OrganizationGraph;

import services.AttendanceService;

import services.LeaveService;

import services.PayrollService;

import services.HousingService;

import services.PromotionService;

import services.AppraisalService;

import services.MedicalService;

public class DataInitializer {

    private EmployeeLinkedList employeeList;

    private CustomHashTable<Integer, Employee> employeeTable;

    private OrganizationGraph organizationGraph;

    private AttendanceService attendanceService;

    private LeaveService leaveService;

    private PayrollService payrollService;

    private HousingService housingService;

    private PromotionService promotionService;

    private AppraisalService appraisalService;

    private MedicalService medicalService;

    public DataInitializer(

            EmployeeLinkedList employeeList,

            CustomHashTable<Integer, Employee> employeeTable,

            OrganizationGraph organizationGraph,

            AttendanceService attendanceService,

            LeaveService leaveService,

            PayrollService payrollService,

            HousingService housingService,

            PromotionService promotionService,

            AppraisalService appraisalService,

            MedicalService medicalService) {

        this.employeeList = employeeList;

        this.employeeTable = employeeTable;

        this.organizationGraph = organizationGraph;

        this.attendanceService = attendanceService;

        this.leaveService = leaveService;

        this.payrollService = payrollService;

        this.housingService = housingService;

        this.promotionService = promotionService;

        this.appraisalService = appraisalService;

        this.medicalService = medicalService;

    }

    // Initialize the entire dataset

    public void initializeData() {

        initializeEmployees();

        initializeAttendance();

        initializePayroll();

        initializeQuarters();

        initializeHousingRequests();

        initializeLeaveRequests();

        initializePromotionHistory();

        initializeAppraisals();

        initializeMedicalProfiles();

        System.out.println("CIL sample data initialized successfully.");

    }

    // Helper to add an employee

    private void addEmployee(

            int id,

            String name,

            String department,

            String post,

            int supervisorId,

            String grade,

            long mobile,

            String joiningDate,

            double salary,

            double rating,

            int points) {

        Employee employee = new Employee(

                id,

                "cil@" + id,

                name,

                department,

                post,

                supervisorId,

                grade,

                mobile,

                LocalDate.parse(joiningDate),

                salary,

                rating,

                points);

        employeeList.addEmployee(employee);

        employeeTable.put(id, employee);

        organizationGraph.addEmployee(id);

        if (supervisorId != -1) {

            organizationGraph.addReportingRelation(

                    supervisorId, id);

        }

    }

    // 30 employees in 5 departments

    private void initializeEmployees() {

        // Operations: 10 employees

        addEmployee(1001, "Rajesh Kumar", "Operations",

                "General Manager", -1, "E8", 9876501001L,

                "2010-01-15", 125000, 4.8, 96);

        addEmployee(1002, "Amit Verma", "Operations",

                "Deputy General Manager", 1001, "E7", 9876501002L,

                "2012-03-10", 105000, 4.6, 92);

        addEmployee(1003, "Priya Singh", "Operations",

                "Operations Manager", 1002, "E6", 9876501003L,

                "2015-07-21", 85000, 4.5, 90);

        addEmployee(1004, "Suresh Yadav", "Operations",

                "Senior Operations Officer", 1003, "E5", 9876501004L,

                "2017-02-12", 69000, 4.2, 84);

        addEmployee(1005, "Neha Gupta", "Operations",

                "Operations Officer", 1003, "E4", 9876501005L,

                "2019-08-19", 56000, 4.1, 82);

        addEmployee(1006, "Vikas Patel", "Operations",

                "Operations Officer", 1004, "E4", 9876501006L,

                "2020-06-11", 54000, 3.9, 76);

        addEmployee(1007, "Anjali Mehta", "Operations",

                "Junior Operations Officer", 1004, "E3", 9876501007L,

                "2022-01-17", 45000, 4.3, 85);

        addEmployee(1008, "Rohit Das", "Operations",

                "Junior Operations Officer", 1005, "E3", 9876501008L,

                "2023-04-03", 43000, 3.8, 72);

        addEmployee(1009, "Pooja Nair", "Operations",

                "Operations Assistant", 1005, "E2", 9876501009L,

                "2024-07-22", 35000, 4.0, 78);

        addEmployee(1010, "Karan Joshi", "Operations",

                "Operations Trainee", 1006, "E1", 9876501010L,

                "2025-08-04", 28000, 3.7, 70);

        // Mining: 8 employees

        addEmployee(1011, "Manoj Mishra", "Mining",

                "Mining Manager", 1001, "E7", 9876501011L,

                "2013-05-14", 102000, 4.7, 94);

        addEmployee(1012, "Deepak Roy", "Mining",

                "Senior Mining Engineer", 1011, "E6", 9876501012L,

                "2016-09-09", 82000, 4.4, 88);

        addEmployee(1013, "Sneha Kulkarni", "Mining",

                "Mining Engineer", 1012, "E5", 9876501013L,

                "2018-11-20", 68000, 4.5, 90);

        addEmployee(1014, "Arjun Reddy", "Mining",

                "Mining Engineer", 1012, "E5", 9876501014L,

                "2019-02-18", 66000, 4.0, 79);

        addEmployee(1015, "Nikhil Shah", "Mining",

                "Assistant Mining Engineer", 1013, "E4", 9876501015L,

                "2021-06-25", 53000, 4.2, 83);

        addEmployee(1016, "Kavita Rao", "Mining",

                "Assistant Mining Engineer", 1013, "E4", 9876501016L,

                "2022-03-15", 51000, 4.1, 81);

        addEmployee(1017, "Harsh Vardhan", "Mining",

                "Junior Mining Engineer", 1014, "E3", 9876501017L,

                "2023-08-12", 42000, 3.9, 74);

        addEmployee(1018, "Riya Kapoor", "Mining",

                "Mining Trainee", 1015, "E1", 9876501018L,

                "2025-07-01", 29000, 4.0, 77);

        // Finance: 4 employees

        addEmployee(1019, "Sanjay Agarwal", "Finance",

                "Finance Manager", 1001, "E7", 9876501019L,

                "2014-04-11", 100000, 4.6, 91);

        addEmployee(1020, "Meera Iyer", "Finance",

                "Senior Accounts Officer", 1019, "E5", 9876501020L,

                "2018-08-06", 67000, 4.3, 86);

        addEmployee(1021, "Aditya Jain", "Finance",

                "Accounts Officer", 1020, "E4", 9876501021L,

                "2021-09-13", 52000, 4.0, 80);

        addEmployee(1022, "Simran Kaur", "Finance",

                "Accounts Assistant", 1020, "E2", 9876501022L,

                "2024-01-08", 36000, 4.2, 82);

        // HR: 4 employees

        addEmployee(1023, "Anil Deshmukh", "HR",

                "HR Manager", 1001, "E7", 9876501023L,

                "2013-12-09", 98000, 4.5, 90);

        addEmployee(1024, "Rahul Sharma", "HR",

                "HR Officer", 1023, "E5", 9876501024L,

                "2018-06-12", 65000, 4.3, 86);

        addEmployee(1025, "Isha Malhotra", "HR",

                "HR Executive", 1024, "E3", 9876501025L,

                "2022-07-18", 44000, 4.1, 80);

        addEmployee(1026, "Varun Bhat", "HR",

                "HR Assistant", 1024, "E2", 9876501026L,

                "2024-09-05", 34000, 3.8, 73);

        // IT: 4 employees

        addEmployee(1027, "Vivek Menon", "IT",

                "IT Manager", 1001, "E7", 9876501027L,

                "2015-01-20", 97000, 4.7, 93);

        addEmployee(1028, "Tanvi Shah", "IT",

                "Senior Software Engineer", 1027, "E5", 9876501028L,

                "2019-05-16", 72000, 4.6, 92);

        addEmployee(1029, "Yash Patil", "IT",

                "Software Engineer", 1028, "E4", 9876501029L,

                "2021-11-08", 55000, 4.2, 84);

        addEmployee(1030, "Ayesha Khan", "IT",

                "Junior Software Engineer", 1028, "E2", 9876501030L,

                "2024-06-17", 39000, 4.4, 87);

    }

    // Attendance for 30 employees

    private void initializeAttendance() {

        for (int id = 1001; id <= 1030; id++) {

            int total = 200;

            int present = 160 + ((id - 1001) * 7) % 31;

            int approvedLeave = 5 + ((id - 1001) % 11);

            int absent = total - present - approvedLeave;

            // Locked sample record for Rahul Sharma

            if (id == 1024) {

                present = 175;

                approvedLeave = 10;

                absent = 15;

            }

            AttendanceRecord record = new AttendanceRecord(

                    id,

                    total,

                    present,

                    absent,

                    approvedLeave);

            attendanceService.addAttendanceRecord(record);

        }

    }

    // 30 employees x 3 months = 90 payroll records

    private void initializePayroll() {

        for (int id = 1001; id <= 1030; id++) {

            Employee employee = employeeTable.get(id);

            for (int month = 7; month <= 9; month++) {

                PayrollRecord record = new PayrollRecord(

                        id,

                        month,

                        2026,

                        employee.getBasicSalary());

                payrollService.addPayrollRecord(record);

            }

        }

    }

    // Helper to add a quarter

    private void addQuarter(

            String quarterId,

            String quarterType,

            String colony,

            int houseNumber,

            int occupantId) {

        Quarter quarter = new Quarter(

                quarterId,

                quarterType,

                colony,

                houseNumber);

        if (occupantId != -1) {

            quarter.allotQuarter(occupantId);

        }

        housingService.addQuarter(quarter);

    }

    // 24 quarters: 15 occupied and 9 vacant.
    // Project mapping: E1-E3 -> A, E4-E5 -> B,
    // E6-E7 -> C, E8-E9 -> D.
    private void initializeQuarters() {
        // Type A: 8 quarters, 6 occupied, 2 vacant
        addQuarter("A1", "A", "Ashoka Colony", 1, 1007);
        addQuarter("A2", "A", "Ashoka Colony", 2, 1008);
        addQuarter("A3", "A", "Ashoka Colony", 3, 1009);
        addQuarter("A4", "A", "Ashoka Colony", 4, 1017);
        addQuarter("A5", "A", "Ashoka Colony", 5, 1018);
        addQuarter("A6", "A", "Ashoka Colony", 6, 1022);
        addQuarter("A7", "A", "Ashoka Colony", 7, -1);
        addQuarter("A8", "A", "Ashoka Colony", 8, -1);

        // Type B: 7 quarters, 5 occupied, 2 vacant
        addQuarter("B1", "B", "Bharat Colony", 1, 1004);
        addQuarter("B2", "B", "Bharat Colony", 2, 1005);
        addQuarter("B3", "B", "Bharat Colony", 3, 1013);
        addQuarter("B4", "B", "Bharat Colony", 4, 1020);
        addQuarter("B5", "B", "Bharat Colony", 5, 1024);
        addQuarter("B6", "B", "Bharat Colony", 6, -1);
        addQuarter("B7", "B", "Bharat Colony", 7, -1);

        // Type C: 5 quarters, 3 occupied, 2 vacant
        addQuarter("C1", "C", "Central Colony", 1, 1002);
        addQuarter("C2", "C", "Central Colony", 2, 1003);
        addQuarter("C3", "C", "Central Colony", 3, 1011);
        addQuarter("C4", "C", "Central Colony", 4, -1);
        addQuarter("C5", "C", "Central Colony", 5, -1);

        // Type D: 4 quarters, 1 occupied, 3 vacant
        addQuarter("D1", "D", "Dhanbad Colony", 1, 1001);
        addQuarter("D2", "D", "Dhanbad Colony", 2, -1);
        addQuarter("D3", "D", "Dhanbad Colony", 3, -1);
        addQuarter("D4", "D", "Dhanbad Colony", 4, -1);
    }

    // Six pending housing applications, all from employees
    // who do not already occupy quarters.
    private void initializeHousingRequests() {
        int[] employeeIds = {1006, 1010, 1014, 1016, 1025, 1029};
        String[] applicationDates = {
            "2026-09-01", "2026-09-03", "2026-09-05",
            "2026-09-08", "2026-09-10", "2026-09-12"
        };

        for (int i = 0; i < employeeIds.length; i++) {
            Employee employee = employeeTable.get(employeeIds[i]);
            String quarterType = housingService.getEligibleQuarterType(employeeIds[i]);
            if (employee == null || quarterType == null) {
                throw new IllegalStateException("Invalid housing applicant: " + employeeIds[i]);
            }
            HousingRequest request = new HousingRequest(
                String.format("HQ%03d", i + 1),
                employee,
                quarterType,
                false,
                LocalDate.parse(applicationDates[i])
            );
            housingService.addExistingRequest(request);
        }
    }

    // Eight leave applications

    private void initializeLeaveRequests() {

        int[] employeeIds = {

                1005, 1008, 1013, 1017,

                1021, 1024, 1026, 1030

        };

        String[] types = {

                "Sick", "Casual", "Paid", "Sick",

                "Casual", "Paid", "Sick", "Casual"

        };

        String[] fromDates = {

                "2026-10-15",

                "2026-10-18",

                "2026-10-20",

                "2026-10-22",

                "2026-10-25",

                "2026-10-27",

                "2026-11-02",

                "2026-11-05"

        };

        for (int i = 0; i < employeeIds.length; i++) {

            Employee employee = employeeTable.get(employeeIds[i]);

            LocalDate from = LocalDate.parse(fromDates[i]);

            LeaveRequest request = new LeaveRequest(

                    String.format("LV%03d", i + 1),

                    employee.getEmployeeId(),

                    employee.getDepartment(),

                    types[i],

                    LocalDate.of(2026, 10, 10),

                    from,

                    from.plusDays(1),

                    "Personal leave requirement");

            leaveService.addExistingRequest(request);

        }

    }

    // Twelve predefined promotion-history records

    private void initializePromotionHistory() {

        int[] ids = {

                1001, 1002, 1003, 1011,

                1012, 1013, 1019, 1020,

                1023, 1024, 1027, 1028

        };

        for (int i = 0; i < ids.length; i++) {

            Employee employee = employeeTable.get(ids[i]);

            int currentGrade = Integer.parseInt(

                    employee.getGrade().substring(1));

            String oldGrade = "E" + (currentGrade - 1);

            PromotionService.PromotionRecord record = new PromotionService.PromotionRecord(

                    employee.getEmployeeId(),

                    oldGrade,

                    employee.getGrade(),

                    "Previous Designation",

                    employee.getPost(),

                    LocalDate.of(

                            2023 + (i % 3),

                            1 + (i % 12),

                            10));

            promotionService.addPromotionHistory(record);

        }

    }

    // Six predefined self-appraisals

    private void initializeAppraisals() {

        int[] ids = {

                1005, 1013, 1020, 1024, 1028, 1029

        };

        for (int id : ids) {

            Employee employee = employeeTable.get(id);

            Appraisal appraisal = new Appraisal(

                    employee,

                    "Completed assigned work within deadlines",

                    "Supported team members during projects",

                    "Managed workload and deadlines",

                    "Improved communication and technical skills",

                    "Take on greater responsibilities");

            appraisalService.addExistingAppraisal(appraisal);

        }

    }

    // Thirty fictional medical profiles

    private void initializeMedicalProfiles() {

        String[] bloodGroups = {

                "A+", "B+", "O+", "AB+",

                "A-", "B-", "O-", "AB-"

        };

        for (int id = 1001; id <= 1030; id++) {

            MedicalService.MedicalProfile profile = new MedicalService.MedicalProfile(

                    id,

                    bloodGroups[(id - 1001) % bloodGroups.length],

                    "MED" + id,

                    "CIL Central Hospital");

            // Demonstration dependent

            profile.addDependent(

                    new MedicalService.Dependent(

                            "Dependent " + id,

                            "Spouse",

                            30 + (id % 10)));

            // Demonstration medical referral

            if (id % 3 == 0) {

                profile.addReferral(

                        new MedicalService.MedicalReferral(

                                "REF" + id,

                                "CIL Central Hospital",

                                "General Medicine",

                                "2026-08-15",

                                "Completed"));

            }

            // Demonstration medical history

            profile.addHistory(

                    new MedicalService.MedicalHistory(

                            "2026-07-10",

                            "CIL Central Hospital",

                            "Routine health checkup"));

            medicalService.addMedicalProfile(profile);

        }

    }

}
