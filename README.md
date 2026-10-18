# CIL Employee & HR Management System

A **Java-based, console-driven Employee and HR Management System** developed as a Data Structures and Algorithms (DSA) mini-project. Inspired by administrative workflows at **Coal India Limited (CIL)**, it provides separate Employee and HR portals for managing common workplace processes.

> **Academic project disclaimer:** This is a student-built prototype inspired by CIL workflows. It is not an official Coal India Limited application or an affiliated service.

## Problem Statement

Managing employee records, attendance, leave, payroll, housing requests, performance reviews, and promotions involves frequent searches, updates, and priority-based decisions. This project brings those operations together in a Java console application while demonstrating the practical use of data structures and algorithms.

## Features

### Employee Portal

- Employee login and profile viewing
- Attendance summary and leave balance
- Leave applications and request status
- Payroll details and history
- Housing/quarter applications, status, and vacancy information
- Performance details and promotion eligibility
- Self-appraisal submission and HR feedback
- Medical profile, dependents, referrals, and history
- Transfer history and insurance-related menu options (where implemented)

### HR Portal

- HR login and employee record management
- Employee listing, searching, and sorting
- Attendance management and leave approval
- Payroll processing
- Housing application queues, allotment, vacancy tracking, and quarter release
- Performance ratings, appraisals, and promotion ranking
- Organization hierarchy display using graph traversal
- Employee medical information management

## Data Structures and Algorithms

| Concept | Application |
|---|---|
| Singly Linked List | Employee record organization |
| Custom Hash Table (Separate Chaining) | Employee ID-based lookup |
| Linear Search | Searching employee and service records |
| Quick Sort | Sorting and ranking employee data |
| Max Heap / Priority Queue | Housing request priority and promotion ranking |
| Min Heap | Housing vacancy management |
| Graph and Depth-First Search (DFS) | Organization/reporting hierarchy traversal |
| Arrays and ArrayLists | Storing application records and supporting data |

The project also includes custom DSA utility classes. Their presence does not necessarily mean every utility is used in each feature.

## Technology Stack

- **Language:** Java
- **Interface:** Command-line / console
- **Development Environment:** Visual Studio Code (or any Java IDE)
- **Dependencies:** Java Standard Library

## Project Structure

```text
CILManagementSystem/
├── Main.java
├── models/              # Employee, leave, payroll, housing, and other data models
├── dsa/                 # Custom data structures and algorithms
├── services/            # Business logic for each management module
├── portals/             # EmployeePortal.java and HRPortal.java
├── data/                # Initial sample data
└── README.md
```

## Getting Started

### Prerequisites

Install a Java Development Kit (JDK) and make sure `java` and `javac` are available in your terminal.

### Run the Application

1. Clone the repository :

   ```bash
   git clone <https://github.com/gaurikhairnar-live/CILManagementSystem>
   cd CILManagementSystem
   ```

2. Compile from the project root:

   ```bash
   javac -sourcepath . Main.java
   ```

3. Run:

   ```bash
   java Main
   ```

4. Follow the console menu to select the Employee or HR portal.

> **Note:** The compile and run commands assume the repository uses the package structure shown above and that `Main.java` is the entry point.

## How It Works

1. The program loads sample employee and organization data.
2. Users select the Employee or HR portal and authenticate.
3. The selected portal displays the available operations.
4. Service classes handle the business rules; custom DSA classes support searching, sorting, queues, and hierarchy traversal.
5. Users return to the menu to perform additional actions or exit.

## Learning Outcomes

- Applying linked lists, hashing, heaps, sorting, and graph traversal in a practical Java project
- Separating models, business logic, and console menus into packages
- Implementing priority-based request handling
- Building an interactive menu-driven application
- Working collaboratively on a multi-file Java codebase

## Future Improvements

- Persistent database storage instead of relying only on in-memory sample data
- GUI or web-based interfaces
- More detailed access control and secure credential storage
- Exportable reports and audit logs
- Automated testing and input validation

## Contributors

Developed as a team academic DSA mini-project.

_Add team member names and GitHub profile links here._

## License

No license has been specified yet. Add a `LICENSE` file if you want to define reuse permissions.
