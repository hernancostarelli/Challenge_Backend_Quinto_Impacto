package com.api.challenge.service;

import com.api.challenge.exception.CourseException;
import com.api.challenge.exception.TeacherException;
import com.api.challenge.model.entity.Teacher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface ITeacherService {

    @Transactional
    void save(String name, String surname) throws TeacherException;

    @Transactional
    void modify(String id, String name, String surname) throws TeacherException;

    @Transactional
    void enable(String id) throws TeacherException;

    @Transactional
    void disable(String id) throws TeacherException, CourseException;

    @Transactional(readOnly = true)
    Teacher getById(String id) throws TeacherException;

    @Transactional(readOnly = true)
    List<Teacher> getAll() throws TeacherException;

    @Transactional(readOnly = true)
    List<Teacher> getByValue(String value) throws TeacherException;

    @Transactional(readOnly = true)
    List<Teacher> getForEnable() throws TeacherException;

    @Transactional(readOnly = true)
    List<Teacher> getForDisable() throws TeacherException;

    @Transactional
    void addCourse(String idStudent, String idCourse) throws TeacherException, CourseException;

    @Transactional
    void removeCourse(String idTeacher, String idCourse) throws TeacherException, CourseException;
}