package com.workintech.spring17challenge.validation;

import com.workintech.spring17challenge.entity.Course;
import com.workintech.spring17challenge.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class CourseValidations {

    public static void isNameValid(String name){
        if (name == null || name.trim().isEmpty()) {
            throw new ApiException("Name is not valid: " + name, HttpStatus.BAD_REQUEST);
        }
    }

    public static  void isIdValid(Integer id){
        if (id == null || id < 0) {
            throw new ApiException("ID is not valid: " + id, HttpStatus.BAD_REQUEST);
        }
    }
    public static void checkCourseNameExistence(List<Course> courses, String name, boolean shouldBeExist){
        boolean exists = false;

        for (Course course: courses) {
            if (course.getName().equalsIgnoreCase(name)) {
                exists = true;
                break;
            }
        }

        if (shouldBeExist && !exists) {
            throw new ApiException("Course name doesn't exist :" + name, HttpStatus.NOT_FOUND);
        } else if (!shouldBeExist && exists) {
            throw new ApiException("Course name already exist : " + name, HttpStatus.BAD_REQUEST);
        }
    }

    public static void isCreditValid(Integer credit) {
        if (credit == null || credit < 0 || credit > 4) {
            throw new ApiException("Credit is not valid: " + credit, HttpStatus.BAD_REQUEST);
        }
    }

    public static void checkCourseIdExistence(List<Course> courses, Integer id, boolean shouldBeExist) {
        boolean exists = false;

        for (Course course: courses) {
            if(course.getId().equals(id)) {
                exists = true;
                break;
            }
        }

        if (shouldBeExist && !exists) {
            throw new ApiException("Course id doesn't exist :" + id, HttpStatus.NOT_FOUND);
        } else if (!shouldBeExist && exists) {
            throw new ApiException("Course id already exist : " + id, HttpStatus.BAD_REQUEST);
        }

    }
}
