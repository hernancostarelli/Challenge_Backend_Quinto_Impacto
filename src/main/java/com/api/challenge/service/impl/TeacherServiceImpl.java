package com.api.challenge.service.impl;

import com.api.challenge.exception.CourseException;
import com.api.challenge.exception.TeacherException;
import com.api.challenge.model.entity.Course;
import com.api.challenge.model.entity.Teacher;
import com.api.challenge.model.enums.EExceptionMessage;
import com.api.challenge.repository.ICourseRepository;
import com.api.challenge.repository.ITeacherRepository;
import com.api.challenge.service.ITeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements ITeacherService {

    private final ITeacherRepository repository;
    private final ICourseRepository courseRepository;

    @Override
    public void save(String name, String surname) throws TeacherException {
        validate(name, surname);
        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setSurname(surname);
        repository.save(teacher);
    }

    @Override
    public void modify(String id, String name, String surname) throws TeacherException {
        Optional<Teacher> optionalTeacher = repository.findById(id);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            validate(name, surname);
            teacher.setName(name);
            teacher.setSurname(surname);
            teacher.setModificationDate(new Date());
            repository.save(teacher);
        }
    }

    @Override
    public void enable(String id) throws TeacherException {
        Optional<Teacher> optionalTeacher = repository.findById(id);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            teacher.setDeleted(false);
            teacher.setModificationDate(new Date());
            repository.save(teacher);
        } else {
            throw new TeacherException(EExceptionMessage.TEACHER_NOT_FOUND.toString());
        }
    }

    @Override
    public void disable(String id) throws TeacherException, CourseException {
        Optional<Teacher> optionalTeacher = repository.findById(id);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            // REMOVE LIST OF COURSES
            removeListOfCourse(teacher);
            teacher.setDeleted(true);
            teacher.setModificationDate(new Date());
            repository.save(teacher);
        } else {
            throw new TeacherException(EExceptionMessage.TEACHER_NOT_FOUND.toString());
        }
    }

    @Override
    public void delete(String id) throws TeacherException, CourseException {
        Optional<Teacher> optionalTeacher = repository.findById(id);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            // REMOVE LIST OF COURSES
            removeListOfCourse(teacher);
            repository.delete(teacher);
        } else {
            throw new TeacherException(EExceptionMessage.TEACHER_NOT_FOUND.toString());
        }
    }

    @Override
    public Teacher getById(String id) throws TeacherException {
        Optional<Teacher> optionalTeacher = repository.findById(id);
        if (optionalTeacher.isPresent()) {
            return optionalTeacher.get();
        } else {
            throw new TeacherException(EExceptionMessage.TEACHER_NOT_FOUND.toString());
        }
    }

    @Override
    public List<Teacher> getAll() throws TeacherException {
        List<Teacher> teacherList = repository.findAll();
        if (!(teacherList.isEmpty())) {
            return teacherList;
        } else {
            throw new TeacherException(EExceptionMessage.ERROR_WHEN_DISPLAYING_A_LIST_OF_ALL_TEACHER.toString());
        }
    }

    @Override
    public List<Teacher> getByValue(String value) throws TeacherException {
        if (value == null) {
            value = "";
        }
        List<Teacher> teacherList = repository.getByValue("%" + value + "%");
        if (!(teacherList.isEmpty())) {
            return teacherList;
        } else {
            throw new TeacherException(EExceptionMessage.TEACHER_NOT_FOUND.toString());
        }
    }

    @Override
    public List<Teacher> getForEnable() throws TeacherException {
        List<Teacher> teacherList = repository.getForEnable();
        if (teacherList != null) {
            return teacherList;
        } else {
            throw new TeacherException(EExceptionMessage.ERROR_WHEN_DISPLAYING_ACTIVE_TEACHERS.toString());
        }
    }

    @Override
    public List<Teacher> getForDisable() throws TeacherException {
        List<Teacher> teacherList = repository.getForDisable();
        if (teacherList != null) {
            return teacherList;
        } else {
            throw new TeacherException(EExceptionMessage.ERROR_WHEN_DISPLAYING_INACTIVE_TEACHERS.toString());
        }
    }

    @Override
    public void addCourse(String idStudent, String idCourse) throws TeacherException, CourseException {
        Optional<Teacher> optionalTeacher = repository.findById(idStudent);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            Optional<Course> optionalCourse = courseRepository.findById(idCourse);
            if (optionalCourse.isPresent()) {
                Course course = optionalCourse.get();
                if (course.getTeacher() == null) {
                    List<Course> courseList = teacher.getCourseList();
                    courseList.add(course);
                    course.setTeacher(teacher);
                    teacher.setModificationDate(new Date());
                    repository.save(teacher);

                } else {
                    throw new CourseException(EExceptionMessage.THE_COURSE_ALREADY_HAS_A_TEACHER.toString());
                }

            } else {
                throw new CourseException(EExceptionMessage.COURSE_NOT_FOUND.toString());
            }
        } else {
            throw new TeacherException(EExceptionMessage.TEACHER_NOT_FOUND.toString());
        }
    }

    @Override
    public void removeCourse(String idTeacher, String idCourse) throws TeacherException, CourseException {
        Optional<Teacher> optionalTeacher = repository.findById(idTeacher);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            Optional<Course> optionalCourse = courseRepository.findById(idCourse);
            if (optionalCourse.isPresent()) {
                Course course = optionalCourse.get();
                List<Course> courseList = teacher.getCourseList();
                courseList.remove(course);
                course.setTeacher(null);
                teacher.setModificationDate(new Date());
                repository.save(teacher);
            } else {
                throw new CourseException(EExceptionMessage.COURSE_NOT_FOUND.toString());
            }
        } else {
            throw new TeacherException(EExceptionMessage.TEACHER_NOT_FOUND.toString());
        }
    }

    private void removeListOfCourse(Teacher teacher) throws TeacherException, CourseException {
        List<Course> courseList = teacher.getCourseList();
        if (!courseList.isEmpty()) {
            int i = 0;
            while (i < courseList.size()) {
                Course course = courseList.get(i);
                removeCourse(teacher.getId(), course.getId());
            }
        }
    }

    public void validate(String name, String surname) throws TeacherException {
        if (name == null || name.isEmpty()) {
            throw new TeacherException(EExceptionMessage.THE_TEACHER_NAME_CANNOT_BE_EMPTY_OR_BE_NULL.toString());
        }
        if (surname == null || surname.isEmpty()) {
            throw new TeacherException(EExceptionMessage.THE_TEACHER_SURNAME_CANNOT_BE_EMPTY_OR_BE_NULL.toString());
        }
    }
}