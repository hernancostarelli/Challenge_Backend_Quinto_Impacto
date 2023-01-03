package com.api.challenge.service;

import com.api.challenge.exception.AdminException;
import com.api.challenge.exception.EmailAlreadyExistException;
import com.api.challenge.model.entity.Admin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface IAdminService {

    @Transactional
    void register(String name, String surname, String email, String password, String password2) throws AdminException, EmailAlreadyExistException;
}