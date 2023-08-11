package com.lean.news.service;

import com.lean.news.entity.ProfileImage;
import com.lean.news.exception.MyException;
import com.lean.news.repository.ProfileImageRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Lean
 */
@Service
public class ProfileImageService {

    @Autowired
    private ProfileImageRepository profileImageRepository;

    @Transactional
    public ProfileImage saveImage(MultipartFile imageFile) throws MyException {

        if (imageFile != null) {

            try {
                ProfileImage profileImage = new ProfileImage();

                profileImage.setName(imageFile.getName());

                profileImage.setMime(imageFile.getContentType());

                profileImage.setContent(imageFile.getBytes());

                return profileImageRepository.save(profileImage);

            } catch (IOException ex) {

                System.err.println(ex.getMessage());

            }

        }

        return null;

    }

    public ProfileImage actualizeImage(String idImage, MultipartFile imageFile) throws MyException {

        if (imageFile != null) {

            try {

                ProfileImage profileImage = new ProfileImage();

                if (idImage != null) {

                    Optional<ProfileImage> optionalImage = profileImageRepository.findById(idImage);

                    if (optionalImage.isPresent()) {

                        profileImage = optionalImage.get();

                    }
                }

                profileImage.setName(imageFile.getName());

                profileImage.setMime(imageFile.getContentType());

                profileImage.setContent(imageFile.getBytes());

                return profileImageRepository.save(profileImage);

            } catch (IOException ex) {

                System.err.println(ex.getMessage());

            }

        }

        return null;
    }

    @Transactional(readOnly = true)
    public List<ProfileImage> listarTodos() {
        return profileImageRepository.findAll();
    }

    @Transactional
    public ProfileImage getOne(String id) {
        return profileImageRepository.getOne(id);
    }
}
