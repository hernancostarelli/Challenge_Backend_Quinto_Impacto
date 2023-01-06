package com.api.challenge.repository;

import com.api.challenge.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICourseRepository extends JpaRepository<Course, String> {

    @Query("SELECT c FROM Course c WHERE c.name LIKE :value OR c.turn LIKE :value OR c.schedule LIKE :value ORDER BY c.name ASC")
    List<Course> getByValue(@Param("value") String value);

    @Query("SELECT c FROM Course c WHERE c.deleted = false ORDER BY c.name ASC")
    List<Course> getForEnable();

    @Query("SELECT c FROM Course c WHERE c.deleted = true ORDER BY c.name ASC")
    List<Course> getForDisable();

    @Query("SELECT c FROM Course c WHERE c.name LIKE :name AND c.deleted = false ORDER BY c.name ASC")
    List<Course> getByName(@Param("name") String name);
}