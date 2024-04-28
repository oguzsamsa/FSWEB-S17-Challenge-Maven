package com.workintech.spring17challenge.controller;

import com.workintech.spring17challenge.dto.ApiResponse;
import com.workintech.spring17challenge.entity.Course;
import com.workintech.spring17challenge.entity.CourseGpa;
import com.workintech.spring17challenge.validation.CourseValidations;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/courses")
public class CourseController {
    private List<Course> courses;
    private final CourseGpa lowCourseGpa;
    private final CourseGpa mediumCourseGpa;
    private final CourseGpa highCourseGpa;

    public CourseController(@Qualifier("lowCourseGpa") CourseGpa lowCourseGpa,
                            @Qualifier("mediumCourseGpa") CourseGpa mediumCourseGpa,
                            @Qualifier("highCourseGpa") CourseGpa highCourseGpa) {
        this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }

    @PostConstruct
    public void init(){
        this.courses = new ArrayList<>();
    }

    @GetMapping
    public List<Course> getCourses(){
        return courses;
    }

    @GetMapping("/{name}")
    public Course getCourseByName(@PathVariable String name){
        CourseValidations.isNameValid(name);
        CourseValidations.checkCourseNameExistence(courses, name, true);

        for (Course course: courses) {
            if (course.getName().equalsIgnoreCase(name)) {
                return course;
            }
        }

        return null;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addCourse(@RequestBody Course course) {
        CourseValidations.isNameValid(course.getName());
        CourseValidations.isCreditValid(course.getCredit());

        Integer totalGpa = getTotalGpa(course);
        courses.add(course);
        ApiResponse apiResponse = new ApiResponse(course, totalGpa);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCourse(@PathVariable Integer id, @RequestBody Course course){

        CourseValidations.isIdValid(id);
        CourseValidations.isCreditValid(course.getCredit());
        CourseValidations.isNameValid(course.getName());

        course.setId(id);
        Course existingCourse = getExistingCourseById(id);
        Integer totalGpa = getTotalGpa(course);
        int indexOfExisting = courses.indexOf(existingCourse);
        courses.set(indexOfExisting, course);

        ApiResponse apiResponse = new ApiResponse(course, totalGpa);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Integer id) {

        CourseValidations.isIdValid(id);
        CourseValidations.checkCourseIdExistence(courses, id, true);
        Course existingCourse = getExistingCourseById(id);
        courses.remove(existingCourse);
    }

    private Integer getTotalGpa(Course course) {
        Integer totalGpa = null;
        if (course.getCredit() <= 2) {
            totalGpa = course.getGrade().getCoefficient() * course.getCredit() * lowCourseGpa.getGpa();
        } else if (course.getCredit() == 3) {
            totalGpa = course.getGrade().getCoefficient() * course.getCredit() * mediumCourseGpa.getGpa();
        } else {
            totalGpa = course.getGrade().getCoefficient() * course.getCredit() * highCourseGpa.getGpa();
        }
        return totalGpa;
    }

    private Course getExistingCourseById(Integer id) {
        return courses.stream().filter(c -> c.getId().equals(id)).toList().get(0);
    }


}
