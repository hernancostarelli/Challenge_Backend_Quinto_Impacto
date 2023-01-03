package com.api.challenge.repository;

import com.api.challenge.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITeacherRepository extends JpaRepository<Teacher, String> {

    @Query("SELECT t FROM Teacher t WHERE t.name LIKE :value OR t.surname LIKE :value AND t.deleted = false " +
            "ORDER BY t.surname ASC")
    List<Teacher> getByValue(@Param("value")String value);

    @Query("SELECT t FROM Teacher t WHERE t.deleted = false ORDER BY t.name ASC")
    List<Teacher> getForEnable();

    @Query("SELECT t FROM Teacher t WHERE t.deleted = true ORDER BY t.name ASC")
    List<Teacher> getForDisable();

    @Query("SELECT t FROM Teacher t WHERE t.name LIKE :name AND t.deleted = false ORDER BY t.name ASC")
    List<Teacher> getByName(@Param("name")String name);
}