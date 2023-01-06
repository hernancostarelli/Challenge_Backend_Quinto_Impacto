package com.api.challenge.service.impl;

import com.api.challenge.exception.CourseException;
import com.api.challenge.exception.StudentException;
import com.api.challenge.model.entity.Course;
import com.api.challenge.model.entity.Student;
import com.api.challenge.model.enums.EExceptionMessage;
import com.api.challenge.repository.ICourseRepository;
import com.api.challenge.repository.IStudentRepository;
import com.api.challenge.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements IStudentService {

    private final IStudentRepository repository;
    private final ICourseRepository courseRepository;

    @Override
    public void save(String name, String surname, String dateOfBirth, String story) throws StudentException {
        validate(name, surname, dateOfBirth);
        Student student = new Student();
        student.setName(name);
        student.setSurname(surname);
        student.setDateOfBirth(dateOfBirth);
        student.setStory(story);
        repository.save(student);
    }

    @Override
    public void modify(String id, String name, String surname, String dateOfBirth, String story) throws StudentException {
        Optional<Student> studentOptional = repository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            validate(name, surname, dateOfBirth);
            student.setName(name);
            student.setSurname(surname);
            student.setDateOfBirth(dateOfBirth);
            student.setStory(story);
            student.setModificationDate(new Date());
            repository.save(student);
        } else {
            throw new StudentException(EExceptionMessage.STUDENT_NOT_FOUND.toString());
        }
    }

    @Override
    public void enable(String id) throws StudentException {
        Optional<Student> studentOptional = repository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setDeleted(false);
            student.setModificationDate(new Date());
            repository.save(student);
        } else {
            throw new StudentException(EExceptionMessage.STUDENT_NOT_FOUND.toString());
        }
    }

    @Override
    public void disable(String id) throws StudentException, CourseException {
        Optional<Student> studentOptional = repository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            // REMOVE LIST OF COURSES
            removeListOfCourse(id, student);
            student.setDeleted(true);
            student.setModificationDate(new Date());
            repository.save(student);
        } else {
            throw new StudentException(EExceptionMessage.STUDENT_NOT_FOUND.toString());
        }
    }

    @Override
    public void delete(String id) throws StudentException, CourseException {
        Optional<Student> studentOptional = repository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            // REMOVE LIST OF COURSES
            removeListOfCourse(id, student);
            repository.delete(student);
        } else {
            throw new StudentException(EExceptionMessage.STUDENT_NOT_FOUND.toString());
        }
    }

    @Override
    public Student getById(String id) throws StudentException {
        Optional<Student> studentOptional = repository.findById(id);
        if (studentOptional.isPresent()) {
            return studentOptional.get();
        } else {
            throw new StudentException(EExceptionMessage.STUDENT_NOT_FOUND.toString());
        }
    }

    @Override
    public List<Student> getAll() throws StudentException {
        List<Student> studentList = repository.findAll();
        if (!(studentList.isEmpty())) {
            return studentList;
        } else {
            throw new StudentException(EExceptionMessage.ERROR_WHEN_DISPLAYING_A_LIST_OF_ALL_STUDENTS.toString());
        }
    }

    @Override
    public List<Student> getByValue(String value) throws StudentException {
        if (value == null) {
            value = "";
        }
        List<Student> studentList = repository.getByValue("%" + value + "%");
        if (!(studentList.isEmpty())) {
            return studentList;
        } else {
            throw new StudentException(EExceptionMessage.STUDENT_NOT_FOUND.toString());
        }
    }

    @Override
    public List<Student> getForEnable() throws StudentException {
        List<Student> studentList = repository.getForEnable();
        if (studentList != null) {
            return studentList;
        } else {
            throw new StudentException(EExceptionMessage.ERROR_WHEN_DISPLAYING_ACTIVE_STUDENTS.toString());
        }
    }

    @Override
    public List<Student> getForDisable() throws StudentException {
        List<Student> studentList = repository.getForDisable();
        if (studentList != null) {
            return studentList;
        } else {
            throw new StudentException(EExceptionMessage.ERROR_WHEN_DISPLAYING_INACTIVE_STUDENTS.toString());
        }
    }

    @Override
    public void addCourse(String idStudent, String idCourse) throws StudentException, CourseException {
        Optional<Student> optionalStudent = repository.findById(idStudent);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            Optional<Course> optionalCourse = courseRepository.findById(idCourse);
            if (optionalCourse.isPresent()) {
                Course course = optionalCourse.get();
                List<Course> courseList = student.getCourseList();
                courseList.add(course);
                course.getStudentList().add(student);
                student.setModificationDate(new Date());
                repository.save(student);
            } else {
                throw new CourseException(EExceptionMessage.COURSE_NOT_FOUND.toString());
            }
        } else {
            throw new StudentException(EExceptionMessage.STUDENT_NOT_FOUND.toString());
        }
    }

    @Override
    public void removeCourse(String idStudent, String idCourse) throws StudentException, CourseException {
        Optional<Student> optionalStudent = repository.findById(idStudent);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            Optional<Course> optionalCourse = courseRepository.findById(idCourse);
            if (optionalCourse.isPresent()) {
                Course course = optionalCourse.get();
                List<Course> courseList = student.getCourseList();
                courseList.remove(course);
                course.getStudentList().remove(student);
                student.setModificationDate(new Date());
                repository.save(student);
            } else {
                throw new CourseException(EExceptionMessage.COURSE_NOT_FOUND.toString());
            }
        } else {
            throw new StudentException(EExceptionMessage.STUDENT_NOT_FOUND.toString());
        }
    }

    private void removeListOfCourse(String id, Student student) throws StudentException, CourseException {
        List<Course> courseList = student.getCourseList();
        if (!courseList.isEmpty()) {
            int i = 0;
            while (i < courseList.size()) {
                Course course = courseList.get(i);
                removeCourse(id, course.getId());
            }
        }
    }

    public void validate(String name, String surname, String dateOfBirth) throws StudentException {
        if (name == null || name.isEmpty()) {
            throw new StudentException(EExceptionMessage.THE_STUDENT_NAME_CANNOT_BE_EMPTY_OR_BE_NULL.toString());
        }
        if (surname == null || surname.isEmpty()) {
            throw new StudentException(EExceptionMessage.THE_STUDENT_SURNAME_CANNOT_BE_EMPTY_OR_BE_NULL.toString());
        }
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            throw new StudentException(EExceptionMessage.THE_STUDENT_DATE_OF_BIRTH_CANNOT_BE_EMPTY_OR_BE_NULL.toString());
        }
    }
}