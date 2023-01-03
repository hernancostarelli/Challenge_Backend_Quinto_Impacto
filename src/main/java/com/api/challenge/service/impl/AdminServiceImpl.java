package com.api.challenge.service.impl;

import com.api.challenge.exception.AdminException;
import com.api.challenge.exception.EmailAlreadyExistException;
import com.api.challenge.model.entity.Admin;
import com.api.challenge.model.enums.EExceptionMessage;
import com.api.challenge.repository.IAdminRepository;
import com.api.challenge.service.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements IAdminService, UserDetailsService {

    private final IAdminRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void register(String name, String surname, String email, String password, String confirmPassword) throws AdminException, EmailAlreadyExistException {
        validate(name, surname, email, password, confirmPassword);
        Admin admin = new Admin();
        admin.setName(name);
        admin.setSurname(surname);
        admin.setEmail(email);
        String encryptedPassword = passwordEncoder.encode(password);
        admin.setPassword(encryptedPassword);
        repository.save(admin);
    }

    public void validate(String name, String surname, String email, String password, String confirmPassword) throws AdminException, EmailAlreadyExistException {
        if (name == null || name.isEmpty()) {
            throw new AdminException(EExceptionMessage.THE_ADMIN_NAME_CANNOT_BE_EMPTY_OR_BE_NULL.toString());
        }
        if (surname == null || surname.isEmpty()) {
            throw new AdminException(EExceptionMessage.THE_ADMIN_SURNAME_CANNOT_BE_EMPTY_OR_BE_NULL.toString());
        }
        if (email == null || email.isEmpty()) {
            throw new AdminException(EExceptionMessage.THE_ADMIN_EMAIL_CANNOT_BE_EMPTY_OR_BE_NULL.toString());
        }
        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyExistException(EExceptionMessage.EMAIL_ALREADY_EXISTS.toString());
        }
        if (password == null || password.isEmpty()) {
            throw new AdminException(EExceptionMessage.THE_ADMIN_PASSWORD_CANNOT_BE_EMPTY_OR_BE_NULL.toString());
        }
        if (password.length() <= 6) {
            throw new AdminException(EExceptionMessage.THE_ADMIN_PASSWORD_CANNOT_BE_SHORTER_THAN_6_CHARACTERS.toString());
        }
        if (!(password.equals(confirmPassword))) {
            throw new AdminException(EExceptionMessage.THE_ENTERED_PASSWORDS_DO_NOT_MATCH.toString());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = repository.findUserByEmail(email);
        if (admin != null) {
            List<GrantedAuthority> permissions = new ArrayList<>();
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + admin.getRole());
            permissions.add(grantedAuthority);
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attributes.getRequest().getSession(true);
            session.setAttribute("userSession", admin);
            return new User(admin.getEmail(), admin.getPassword(), permissions);
        } else {
            return null;
        }
    }
}
