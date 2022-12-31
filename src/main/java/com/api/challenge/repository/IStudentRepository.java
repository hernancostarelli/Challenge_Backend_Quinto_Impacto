package com.api.challenge.repository;


import com.api.challenge.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IStudentRepository extends JpaRepository<Student, String> {

    @Query("SELECT s FROM Student s WHERE s.name LIKE :value OR s.surname LIKE :value AND s.deleted = false " +
            "ORDER BY s.surname ASC")
    List<Student> getByValue(@Param("value")String value);

    @Query("SELECT s FROM Student s WHERE s.deleted = false ORDER BY s.name ASC")
    List<Student> getForEnable();

    @Query("SELECT s FROM Student s WHERE s.deleted = true ORDER BY s.name ASC")
    List<Student> getForDisable();

    @Query("SELECT s FROM Student s WHERE s.name LIKE :name AND s.deleted = false ORDER BY s.name ASC")
    List<Student> getByName(@Param("name")String name);

    @Query("SELECT s FROM Student s INNER JOIN s.courseList courseList WHERE courseList.name = :name")
    List<Student> getByCourse(@Param("name") String name);
}