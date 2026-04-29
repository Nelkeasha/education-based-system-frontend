package com.nelly.education_based.services;


import com.nelly.education_based.entities.Course;
import com.nelly.education_based.exceptions.CourseNotFoundException;
import com.nelly.education_based.exceptions.UniversitySystemException;

import java.util.List;
import java.util.Optional;

public class CourseService {

    private GenericRepository<Course> courseRepo;

    public CourseService() {
        this.courseRepo = new GenericRepository<>(Course::getCourseCode);
    }

    public void registerCourse(Course course) {
        if (courseRepo.exists(course.getCourseCode())) {
            throw new UniversitySystemException(
                    "Course with code '" + course.getCourseCode() + "' already exists",
                    "DUPLICATE_COURSE");
        }
        courseRepo.add(course);
    }

    public Course getCourse(String courseCode) {
        Optional<Course> course = courseRepo.findById(courseCode);
        if (!course.isPresent()) throw new CourseNotFoundException(courseCode);
        return course.get();
    }

    public void removeCourse(String courseCode) {
        if (!courseRepo.exists(courseCode)) throw new CourseNotFoundException(courseCode);
        courseRepo.remove(courseCode);
    }

    public List<Course> getAllCourses()        { return courseRepo.findAll(); }
    public List<Course> getAvailableCourses()  { return courseRepo.findWhere(c -> !c.isFull()); }
    public int getTotalCourses()               { return courseRepo.count(); }
}

