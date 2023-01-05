package com.api.challenge.repository;

import com.api.challenge.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAdminRepository extends JpaRepository<Admin, String> {
    //Admin findUserByEmail(String email);

    boolean existsByEmail(String email);

    Admin findUserByEmail(String email);
}