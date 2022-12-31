package com.api.challenge.service;

import com.api.challenge.exception.CourseException;
import com.api.challenge.exception.StudentException;
import com.api.challenge.model.entity.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface IStudentService {

    @Transactional
    void save(String name, String surname, String dateOfBirth, String story) throws StudentException;

    @Transactional
    void modify(String id, String name, String surname, String dateOfBirth, String story) throws StudentException;

    @Transactional
    void delete(String id) throws StudentException, CourseException;

    @Transactional
    void enable(String id) throws StudentException, CourseException;

    @Transactional
    void disable(String id) throws StudentException;

    @Transactional(readOnly = true)
    Student getById(String id) throws StudentException;

    @Transactional(readOnly = true)
    List<Student> getAll() throws StudentException;

    @Transactional(readOnly = true)
    List<Student> getByValue(String value) throws StudentException;

    @Transactional(readOnly = true)
    List<Student> getByName(String name) throws StudentException;

    @Transactional(readOnly = true)
    List<Student> getForEnable() throws StudentException;

    @Transactional(readOnly = true)
    List<Student> getForDisable() throws StudentException;

    @Transactional
    void addCourse(String idStudent, String idCourse) throws StudentException, CourseException;

    @Transactional
    void removeCourse(String idStudent, String idCourse) throws StudentException, CourseException;
}