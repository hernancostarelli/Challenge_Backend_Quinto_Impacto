package com.api.challenge.service;

import com.api.challenge.exception.CourseException;
import com.api.challenge.exception.StudentException;
import com.api.challenge.exception.TeacherException;
import com.api.challenge.model.entity.Course;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface ICourseService {

    @Transactional
    void save(String name, String turn, String schedule) throws CourseException;

    @Transactional
    void modify(String id, String name, String turn, String schedule) throws CourseException;

    @Transactional
    void enable(String id) throws CourseException;

    @Transactional
    void disable(String id) throws CourseException, StudentException;

    @Transactional
    void delete(String id) throws CourseException, StudentException, TeacherException;

    @Transactional(readOnly = true)
    Course getById(String id) throws CourseException;

    @Transactional(readOnly = true)
    List<Course> getAll() throws CourseException;

    @Transactional(readOnly = true)
    List<Course> getByValue(String value) throws CourseException;

    @Transactional(readOnly = true)
    List<Course> getByName(String name) throws CourseException;

    @Transactional(readOnly = true)
    List<Course> getForEnable() throws CourseException;

    @Transactional(readOnly = true)
    List<Course> getForDisable() throws CourseException;
}