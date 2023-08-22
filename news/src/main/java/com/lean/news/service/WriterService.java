
package com.lean.news.service;

import com.lean.news.entity.ProfileImage;
import com.lean.news.entity.Writer;
import com.lean.news.enums.Rol;
import com.lean.news.exception.MyException;
import com.lean.news.repository.WriterRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Lean
 */
@Service
public class WriterService {

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private ProfileImageService profileImageService;

    public void registerWriter(String name, String lastName, String email, String password, String password2, MultipartFile imageFile) throws MyException {

        long fileSize = imageFile.getSize();

        validate(name, lastName, email, password, password2, fileSize);

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

    public void actualizeWriter(String id, String name, String lastName,
            String password, String password2, MultipartFile imageFile) throws MyException {

        long fileSize = imageFile.getSize();

        validateActualize(name, lastName, password, password2, fileSize);

        Optional<Writer> optionalWriter = writerRepository.findById(id);

        if (optionalWriter.isPresent()) {
            Writer writer = optionalWriter.get();

            writer.setName(name);
            writer.setLastName(lastName);

            writer.setPassword(new BCryptPasswordEncoder().encode(password));
          

            if (!(imageFile.isEmpty())) { ///Comprueba si el imageFile no está vacio 

                String idImage = writer.getProfileImage().getId(); // idImage toma el valor del id de la imagen existente

                ProfileImage profileImage = profileImageService.actualizeImage(idImage, imageFile);

                writer.setProfileImage(profileImage);
            }

            writerRepository.save(writer);

        }
    }

    private void validate(String name, String lastName, String email, String password, String password2, Long fileSize) throws MyException {
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
        if (fileSize > 2097152) {
            throw new MyException("El tamaño de la imagen supera el máximo de 2mb");
        }

    }

    private void validateActualize(String name, String lastName, String password, String password2, Long fileSize) throws MyException {

        if (name == null || name.isEmpty()) {
            throw new MyException("El nombre no puede ser nulo o estar vacío");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new MyException("El apellido no puede ser nulo o estar vacío");
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
        if (fileSize > 2097152) {
            throw new MyException("El tamaño de la imagen supera el máximo de 2mb");
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
   
    @Transactional
    public Writer getOne(String id) {
        return writerRepository.getOne(id);
    }

}
