package com.nelly.education_based.services;


import com.nelly.education_based.entities.*;
import com.nelly.education_based.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class

EnrollmentService {

    private StudentService studentService;
    private CourseService courseService;
    private List<Enrollment> enrollments;

    public EnrollmentService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollments = new ArrayList<>();
    }

    public void enrollStudent(String studentId, String courseCode) {
        Student student = studentService.getStudent(studentId);
        Course course   = courseService.getCourse(courseCode);

        for (Enrollment e : enrollments) {
            if (e.getStudent().getId().equals(studentId) &&
                    e.getCourse().getCourseCode().equals(courseCode)) {
                throw new DuplicateEnrollmentException(studentId, courseCode);
            }
        }

        if (course.isFull()) {
            throw new UniversitySystemException(
                    "Cannot enroll: course '" + courseCode + "' is at full capacity",
                    "COURSE_FULL");
        }

        Enrollment enrollment = new Enrollment(student, course);
        enrollments.add(enrollment);
        student.addEnrollment(enrollment);
        course.addEnrollment(enrollment);
    }

    public void assignGrade(String studentId, String courseCode, double grade) {
        if (grade < 0.0 || grade > 5.0) throw new InvalidGradeException(grade);

        Enrollment enrollment = findEnrollment(studentId, courseCode);
        if (enrollment == null) throw new NotEnrolledException(studentId, courseCode);

        enrollment.assignGrade(grade);
        enrollment.getStudent().recalculateGPA();
    }

    public void dropEnrollment(String studentId, String courseCode) {
        Enrollment enrollment = findEnrollment(studentId, courseCode);
        if (enrollment == null) throw new NotEnrolledException(studentId, courseCode);
        enrollments.remove(enrollment);
        enrollment.getStudent().getEnrollments().remove(enrollment);
        enrollment.getCourse().getEnrollments().remove(enrollment);
        enrollment.getStudent().recalculateGPA();
    }

    private Enrollment findEnrollment(String studentId, String courseCode) {
        for (Enrollment e : enrollments) {
            if (e.getStudent().getId().equals(studentId) &&
                    e.getCourse().getCourseCode().equals(courseCode)) return e;
        }
        return null;
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getStudent().getId().equals(studentId)) result.add(e);
        }
        return result;
    }

    public List<Enrollment> getEnrollmentsByCourse(String courseCode) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getCourse().getCourseCode().equals(courseCode)) result.add(e);
        }
        return result;
    }

    public List<Enrollment> getAllEnrollments() { return new ArrayList<>(enrollments); }
}

