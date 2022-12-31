package com.api.challenge.repository;

import com.api.challenge.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICourseRepository extends JpaRepository<Course, String> {
}