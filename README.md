# University Management System — Frontend Documentation

## Overview

The frontend is built with **JavaFX** and uses **FXML** for layout and **Java controller classes** for logic. It provides a graphical desktop interface for managing students, courses, enrollments, and grades. The UI communicates directly with the service layer (`StudentService`, `CourseService`, `EnrollmentService`, `UserService`) — there is no network layer.

---

## Technology Stack

| Technology | Purpose |
|---|---|
| JavaFX | UI framework (widgets, tables, dialogs) |
| FXML | Declarative XML layout files |
| Java (Controller classes) | Event handling, input validation, service calls |
| CSS-in-FXML inline styles | Colours, fonts, spacing, border radius |

---

## Frontend File Structure

```
src/
└── main/
    ├── java/com/nelly/education_based/
    │   ├── HelloApplication.java       # App entry point, scene switching
    │   ├── LoginController.java        # Login screen logic
    │   └── MainController.java         # Main screen logic (all tabs)
    │
    └── resources/com/nelly/education_based/
        ├── login-view.fxml             # Login screen layout
        └── main-view.fxml              # Main screen layout (4 tabs)
```

---

## Screens

### 1. Login Screen (`login-view.fxml` + `LoginController.java`)

A centered card on a blue background (`#1565C0`).

**Fields**
- `usernameField` — text input for username
- `passwordField` — masked password input

**Behaviour**
- Pressing **Enter** in the username field moves focus to the password field.
- Pressing **Enter** in the password field submits the form.
- On success the app navigates to the main screen.
- On failure the password field is cleared and an inline error is shown in red.

**Default accounts**

| Username | Password | Role |
|---|---|---|
| admin | admin123 | ADMIN |
| nelly | nelly123 | ADMIN |
| instructor | inst123 | INSTRUCTOR |
| student | stud123 | STUDENT |

**Validations**
- Both fields must be non-empty before submission is attempted.

---

### 2. Main Screen (`main-view.fxml` + `MainController.java`)

A `BorderPane` with three regions:

| Region | Content |
|---|---|
| Top | Blue header bar — app title, welcome label, Logout button |
| Center | `TabPane` with four tabs |
| Bottom | Status bar — shows success (green) or error (red) messages |

---

#### Tab 1 — Students

**Form fields**

| Field | Validation rules |
|---|---|
| Student ID | Required · min 2 chars · letters and digits only (no spaces or symbols) |
| First Name | Required · min 2 chars · letters only · hyphens and apostrophes allowed · no digits |
| Last Name | Required · min 2 chars · letters only · hyphens and apostrophes allowed · no digits |
| Email | Required · must match `name@domain.tld` format |
| Major | Required · min 2 chars · letters, spaces, and hyphens only · no digits |

**Buttons**
- **Add Student** — validates all fields, then calls `StudentService.registerStudent()`.
- **Delete Selected** — shows a confirmation dialog, then calls `StudentService.removeStudent()`.
- **Clear** — resets all form fields.

**Table columns**

| Column | Source |
|---|---|
| Student ID | `Student.getId()` |
| First Name | `Student.getFirstName()` |
| Last Name | `Student.getLastName()` |
| Email | `Student.getEmail()` |
| Major | `Student.getMajor()` |
| GPA | `Student.getGpa()` (formatted to 2 decimal places) |

---

#### Tab 2 — Courses

**Form fields**

| Field | Validation rules |
|---|---|
| Course Code | Required · min 2 chars · letters, digits, and hyphens only · no spaces |
| Course Name | Required · min 3 chars · must start with a letter · letters, digits, spaces, and common punctuation allowed |
| Credit Hours | Required · whole number · must be between 1 and 12 |
| Max Capacity | Required · whole number · must be between 1 and 500 |

**Buttons**
- **Add Course** — validates all fields, then calls `CourseService.registerCourse()`.
- **Delete Selected** — shows a confirmation dialog, then calls `CourseService.removeCourse()`.
- **Clear** — resets all form fields.

**Table columns**

| Column | Source |
|---|---|
| Code | `Course.getCourseCode()` |
| Course Name | `Course.getCourseName()` |
| Credits | `Course.getCreditHours()` |
| Capacity | `Course.getMaxCapacity()` |
| Available | `Course.getAvailableSeats()` |
| Enrolled | `Course.getEnrollments().size()` |

---

#### Tab 3 — Enrollments

**Form fields**

| Field | Validation rules |
|---|---|
| Student ID | Required · letters and digits only |
| Course Code | Required · letters, digits, and hyphens only · no spaces |

**Buttons**
- **Enroll Student** — validates fields, then calls `EnrollmentService.enrollStudent()`. The service also checks: student exists, course exists, student not already enrolled, course not full.
- **Drop Selected** — shows a confirmation dialog, then calls `EnrollmentService.dropEnrollment()`.

**Table columns**

| Column | Source |
|---|---|
| Student ID | `Enrollment.getStudent().getId()` |
| Student Name | `Enrollment.getStudent().getFullName()` |
| Course Code | `Enrollment.getCourse().getCourseCode()` |
| Course Name | `Enrollment.getCourse().getCourseName()` |
| Enrolled On | `Enrollment.getEnrollmentDate()` |
| Status | `Enrollment.getStatus()` — `ACTIVE` or `COMPLETED` |

---

#### Tab 4 — Grades

**Form fields**

| Field | Validation rules |
|---|---|
| Student ID | Required · letters and digits only |
| Course Code | Required · letters, digits, and hyphens only · no spaces |
| Grade | Required · numeric value · must be between 0.0 and 5.0 |

**Grade scale**

| Numeric range | Letter grade |
|---|---|
| 4.5 – 5.0 | A |
| 4.0 – 4.4 | A- |
| 3.5 – 3.9 | B+ |
| 3.0 – 3.4 | B |
| 2.5 – 2.9 | B- |
| 2.0 – 2.4 | C+ |
| 1.5 – 1.9 | C |
| 1.0 – 1.4 | C- |
| 0.5 – 0.9 | D |
| 0.0 – 0.4 | F |

**Buttons**
- **Assign Grade** — validates all fields, then calls `EnrollmentService.assignGrade()`. Also refreshes the student GPA in the Students table.
- **View Student Grades** — filters the table to show only graded enrollments for the entered Student ID.
- **Show All** — resets the table to show all graded enrollments.

**Table columns**

| Column | Source |
|---|---|
| Student ID | `Enrollment.getStudent().getId()` |
| Student Name | `Enrollment.getStudent().getFullName()` |
| Course Code | `Enrollment.getCourse().getCourseCode()` |
| Course Name | `Enrollment.getCourse().getCourseName()` |
| Grade | `Enrollment.getGrade()` (formatted to 2 decimal places, or `N/A`) |
| Letter | `Enrollment.getLetterGrade()` |

---

## Status Bar

Every user action ends with a call to `setStatus(message, isError)` at the bottom of the screen:
- **Green** (`#2E7D32`) — operation succeeded.
- **Red** (`#C62828`) — validation failed or a service exception was thrown.

---

## Scene Switching

`HelloApplication` owns the primary `Stage` and exposes two static methods:

```java
HelloApplication.showLoginView();   // navigate to login screen
HelloApplication.showMainView();    // navigate to main screen after login
```

`LoginController` calls `showMainView()` on success. `MainController` calls `showLoginView()` on logout.

---

## Prerequisites

| Requirement | Version |
|---|---|
| Java (JDK) | 21 or higher |
| Maven | 3.8 or higher |
| IDE (recommended) | IntelliJ IDEA |

---

## Dependencies

Declared in `pom.xml`:

| Library | Version | Purpose |
|---|---|---|
| javafx-controls | 21 | Core UI widgets (Button, TextField, TableView, etc.) |
| javafx-fxml | 21 | FXML loader for layout files |
| javafx-media | 21 | Media support |
| controlsfx | 11.2.1 | Extended JavaFX controls |
| formsfx-core | 11.6.0 | Form building utilities |
| bootstrapfx-core | 0.4.0 | Bootstrap-inspired CSS styles |
| junit-jupiter-api | 5.10.2 | Unit testing (test scope) |
| junit-jupiter-engine | 5.10.2 | Unit test runner (test scope) |

---

## How to Run

**Option 1 — IntelliJ IDEA**
1. Open the project folder in IntelliJ IDEA.
2. Let Maven download dependencies automatically.
3. Run `HelloApplication.java` (right-click → Run).

**Option 2 — Maven command line**
```bash
mvn clean javafx:run
```

**Option 3 — Maven wrapper (no Maven installation required)**
```bash
# Windows
mvnw.cmd clean javafx:run

# Linux / Mac
./mvnw clean javafx:run
```

---

## Backend File Structure

```
src/main/java/com/nelly/education_based/
│
├── entities/
│   ├── Person.java          # Abstract base class (id, name, email)
│   ├── Student.java         # Extends Person — major, GPA, enrollments
│   ├── Instructor.java      # Extends Person — assigned courses
│   ├── Course.java          # Course code, name, credits, capacity
│   ├── Enrollment.java      # Links Student ↔ Course, holds grade & status
│   └── User.java            # Login user (username, password, role)
│
├── services/
│   ├── GenericRepository.java   # In-memory generic store (add, find, remove)
│   ├── Repository.java          # Repository interface
│   ├── StudentService.java      # Register, get, remove, filter students
│   ├── CourseService.java       # Register, get, remove, filter courses
│   ├── EnrollmentService.java   # Enroll, drop, assign grade, query enrollments
│   └── UserService.java         # Login, logout, current user
│
└── exceptions/
    ├── UniversitySystemException.java    # Base exception
    ├── StudentNotFoundException.java
    ├── CourseNotFoundException.java
    ├── DuplicateEnrollmentException.java
    ├── InvalidGradeException.java
    └── NotEnrolledException.java
```

---

## Data Flow

```
FXML Layout  →  Controller (event handler)  →  Service  →  Repository (in-memory)
                     ↑ validation here            ↑ business rules here
```

All data is stored in memory — there is no database. Data resets every time the application is restarted.
