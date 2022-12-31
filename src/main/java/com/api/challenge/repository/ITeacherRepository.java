package com.api.challenge.repository;

import com.api.challenge.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITeacherRepository extends JpaRepository<Teacher, String> {
}