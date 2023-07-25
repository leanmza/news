/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lean.news.service;

import com.lean.news.entity.ProfileImage;
import com.lean.news.entity.Writer;
import com.lean.news.enums.Rol;
import com.lean.news.exception.MyException;
import com.lean.news.repository.WriterRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Lean
 */
@Service
public class WriterService implements UserDetailsService {

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private ProfileImageService profileImageService;

    public void registerWriter(String name, String lastName, String email, String password, String password2, MultipartFile imageFile) throws MyException {

        validate(name, lastName, email, password, password2);

        Writer writer = new Writer();

        writer.setName(name);
        writer.setLastName(lastName);
        writer.setEmail(email);
        writer.setPassword(new BCryptPasswordEncoder().encode(password));
        writer.setRol(Rol.WRITER);

        if (imageFile != null) {

            ProfileImage profileImage = profileImageService.saveImage(imageFile);
           writer.setProfileImage(profileImage);
        }

        writerRepository.save(writer);

    }

    private void validate(String name, String lastName, String email, String password, String password2) throws MyException {
        if (emailChecker(email) == true) {
            throw new MyException("El email " + email + " ya se encuentra registrado");
        }
        if (name == null || name.isEmpty()) {
            throw new MyException("El nombre no puede ser nulo o estar vacío");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new MyException("El apellido no puede ser nulo o estar vacío");
        }
        if (email == null || email.isEmpty()) {
            throw new MyException("El email no puede ser nulo o estar vacío");
        }
        if (password == null || password.isEmpty()) {
            throw new MyException("La contraseña no pude ser nula o estar vacía");
        }
        if (password.length() < 8) {
            throw new MyException("La contraseña debe tener al menos 8 caracteres");
        }
        if (passwordHasNumber(password) == false) {
            throw new MyException("La contraseña debe tener al menos 1 número");
        }
        if (passwordHasUpperCase(password) == false) {
            throw new MyException("La contraseña debe tener al menos una mayúscula");
        }
        if (passwordHasLowerCase(password) == false) {
            throw new MyException("La contraseña debe tener al menos una minúscula");
        }

        if (password2 == null || password2.isEmpty()) {
            throw new MyException("La contraseña no pude ser nula o estar vacía");
        }
        if (!(password.equals(password2))) {
            throw new MyException("Las contraseñas no coinciden");
        }

    }

    private boolean emailChecker(String email) { // Checks if the email already exists in the DB
        boolean check = false;
        Writer writer = writerRepository.findWriterByEmail(email);
        if (writer != null) {
            check = true;
        }
        return check;
    }

    private boolean passwordHasNumber(String password) {
        boolean hasNumber = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                hasNumber = true;
                break;
            }
        }
        return hasNumber;
    }

    private boolean passwordHasUpperCase(String password) {
        boolean hasUpperCase = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                hasUpperCase = true;
                break;
            }
        }
        return hasUpperCase;
    }

    private boolean passwordHasLowerCase(String password) {
        boolean hasLowerCase = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                hasLowerCase = true;
                break;
            }
        }
        return hasLowerCase;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("email " + email);
        Writer writer = writerRepository.findWriterByEmail(email);
        System.out.println("writer " + writer);

        if (writer != null) {
            List<GrantedAuthority> permissions = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + writer.getRol().toString());

            permissions.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("writerSession", writer);

            return new User(writer.getEmail(), writer.getPassword(), permissions);

        } else {
            throw new UsernameNotFoundException("Usuario no encontrado con el correo: " + email);

        }
    }
}
