package com.nelly.education_based.services;


import com.nelly.education_based.entities.Student;
import com.nelly.education_based.exceptions.StudentNotFoundException;
import com.nelly.education_based.exceptions.UniversitySystemException;

import java.util.List;
import java.util.Optional;

public class StudentService {

    private GenericRepository<Student> studentRepo;

    public StudentService() {
        this.studentRepo = new GenericRepository<>(Student::getId);
    }

    public void registerStudent(Student student) {
        if (studentRepo.exists(student.getId())) {
            throw new UniversitySystemException(
                    "Student with ID '" + student.getId() + "' already exists",
                    "DUPLICATE_STUDENT");
        }
        studentRepo.add(student);
    }

    public Student getStudent(String studentId) {
        Optional<Student> student = studentRepo.findById(studentId);
        if (!student.isPresent()) throw new StudentNotFoundException(studentId);
        return student.get();
    }

    public void removeStudent(String studentId) {
        if (!studentRepo.exists(studentId)) throw new StudentNotFoundException(studentId);
        studentRepo.remove(studentId);
    }

    public List<Student> getAllStudents()                       { return studentRepo.findAll(); }
    public List<Student> getStudentsByMajor(String major)      { return studentRepo.findWhere(s -> s.getMajor().equalsIgnoreCase(major)); }
    public List<Student> getHighPerformers(double threshold)   { return studentRepo.findWhere(s -> s.getGpa() >= threshold); }
    public int getTotalStudents()                              { return studentRepo.count(); }
}
