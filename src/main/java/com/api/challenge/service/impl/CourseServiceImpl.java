package com.api.challenge.service.impl;

import com.api.challenge.exception.CourseException;
import com.api.challenge.exception.StudentException;
import com.api.challenge.model.entity.Course;
import com.api.challenge.model.entity.Student;
import com.api.challenge.model.enums.EExceptionMessage;
import com.api.challenge.repository.ICourseRepository;
import com.api.challenge.repository.IStudentRepository;
import com.api.challenge.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {

    private final ICourseRepository repository;
    private final IStudentRepository studentRepository;

    @Override
    public void save(String name, String turn, String schedule) throws CourseException {
        validate(name, turn, schedule);
        Course course = new Course();
        course.setName(name);
        course.setTurn(turn);
        course.setSchedule(schedule);
        repository.save(course);
    }

    @Override
    public void modify(String id, String name, String turn, String schedule) throws CourseException {
        Optional<Course> optionalCourse = repository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            validate(name, turn, schedule);
            course.setName(name);
            course.setTurn(turn);
            course.setSchedule(schedule);
            course.setModificationDate(new Date());
            repository.save(course);
        } else {
            throw new CourseException(EExceptionMessage.COURSE_NOT_FOUND.toString());
        }
    }

    @Override
    public void enable(String id) throws CourseException {
        Optional<Course> optionalCourse = repository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            course.setDeleted(false);
            course.setModificationDate(new Date());
            repository.save(course);
        } else {
            throw new CourseException(EExceptionMessage.COURSE_NOT_FOUND.toString());
        }
    }

    @Override
    public void disable(String id) throws CourseException, StudentException {
        Optional<Course> optionalCourse = repository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            // REMOVE TEACHER
            course.setTeacher(null);
            // REMOVE LIST OF STUDENTS
            removeStudentList(course);
            course.setDeleted(true);
            course.setModificationDate(new Date());
            repository.save(course);
        } else {
            throw new CourseException(EExceptionMessage.COURSE_NOT_FOUND.toString());
        }
    }

    @Override
    public void delete(String id) throws CourseException, StudentException {
        Optional<Course> optionalCourse = repository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            // REMOVE TEACHER
            course.setTeacher(null);
            // REMOVE LIST OF STUDENTS
            removeStudentList(course);
            repository.delete(course);
        } else {
            throw new CourseException(EExceptionMessage.COURSE_NOT_FOUND.toString());
        }
    }

    @Override
    public Course getById(String id) throws CourseException {
        Optional<Course> optionalCourse = repository.findById(id);
        if (optionalCourse.isPresent()) {
            return optionalCourse.get();
        } else {
            throw new CourseException(EExceptionMessage.COURSE_NOT_FOUND.toString());
        }
    }

    @Override
    public List<Course> getAll() throws CourseException {
        List<Course> courseList = repository.findAll();
        if (!(courseList.isEmpty())) {
            return courseList;
        } else {
            throw new CourseException(EExceptionMessage.ERROR_WHEN_DISPLAYING_A_LIST_OF_ALL_COURSES.toString());
        }
    }

    @Override
    public List<Course> getByValue(String value) throws CourseException {
        if (value == null) {
            value = "";
        }
        List<Course> courseList = repository.getByValue("%" + value + "%");
        if (!(courseList.isEmpty())) {
            return courseList;
        } else {
            throw new CourseException(EExceptionMessage.COURSE_NOT_FOUND.toString());
        }
    }

    @Override
    public List<Course> getForEnable() throws CourseException {
        List<Course> courseList = repository.getForEnable();
        if (courseList != null) {
            return courseList;
        } else {
            throw new CourseException(EExceptionMessage.ERROR_WHEN_DISPLAYING_ACTIVE_COURSES.toString());
        }
    }

    @Override
    public List<Course> getForDisable() throws CourseException {
        List<Course> courseList = repository.getForDisable();
        if (courseList != null) {
            return courseList;
        } else {
            throw new CourseException(EExceptionMessage.ERROR_WHEN_DISPLAYING_INACTIVE_COURSES.toString());
        }
    }

    private void removeStudentList(Course course) throws CourseException, StudentException {
        List<Student> studentList = course.getStudentList();
        if (!(studentList.isEmpty())) {
            int i = 0;
            while (i < studentList.size()) {
                Student student = studentList.get(i);
                removeStudentListToCourse(course.getId(), student.getId());
            }
        }
    }

    @Transactional
    public void removeStudentListToCourse(String idCourse, String idStudent) throws CourseException, StudentException {
        Optional<Course> optionalCourse = repository.findById(idCourse);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            Optional<Student> optionalStudent = studentRepository.findById(idStudent);
            if (optionalStudent.isPresent()) {
                Student student = optionalStudent.get();
                List<Student> studentList = course.getStudentList();
                studentList.remove(student);
                student.getCourseList().clear();
                repository.save(course);
            } else {
                throw new StudentException(EExceptionMessage.STUDENT_NOT_FOUND.toString());
            }
        } else {
            throw new CourseException(EExceptionMessage.COURSE_NOT_FOUND.toString());
        }
    }

    public void validate(String name, String turn, String schedule) throws CourseException {
        if (name == null || name.isEmpty()) {
            throw new CourseException(EExceptionMessage.THE_COURSE_NAME_CANNOT_BE_EMPTY_OR_BE_NULL.toString());
        }
        if (turn == null || turn.isEmpty()) {
            throw new CourseException(EExceptionMessage.THE_COURSE_TURN_CANNOT_BE_EMPTY_OR_NULL.toString());
        }
        if (schedule == null || schedule.isEmpty()) {
            throw new CourseException(EExceptionMessage.THE_COURSE_TIMETABLE_CANNOT_BE_EMPTY_OR_NULL.toString());
        }
    }
}
