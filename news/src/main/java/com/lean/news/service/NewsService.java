/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lean.news.service;

import com.lean.news.entity.Image;
import com.lean.news.entity.News;
import com.lean.news.exception.MyException;
import com.lean.news.repository.NewsRepository;
import java.time.LocalDate;
import java.util.ArrayList;
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
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;
    
    @Autowired
    private ImageService imageService;

    @Transactional
    public void createNews(String title, String body, MultipartFile imageFile) throws MyException {

        validate(title, body);

        News news = new News();
        
        news.setTitle(title);
        news.setBody(body);
        news.setDateLog(LocalDate.now());
        
        if(imageFile !=null){
            System.out.println("hay imagen");
            Image image = imageService.saveImage(imageFile);
            news.setImage(image);      
        }
        
        newsRepository.save(news);
    }

    @Transactional
    public void actualizeNews(String id, String title, String body,  MultipartFile imageFile) throws MyException {

        validate(title, body);

        Optional<News> optionalNews = newsRepository.findById(id);

        if (optionalNews.isPresent()) {
            News news = optionalNews.get();

            news.setTitle(title);
            news.setBody(body);
             
            //QUEDE QUE CAMBIE LA IMAGEN SOLO SI VIENE UN ARCHIVO NUEVO, SINO DEJA LA QUE ESTABA
             if (imageFile.getSize() != 0) {
                news.setImage(null);
            } else {
                String idImage = null;
                if (news.getImage() != null) {
                    idImage = news.getImage().getId();
                }
                Image image = imageService.actualizeImage(idImage, imageFile);
                news.setImage(image);
            }
            newsRepository.save(news);
        }
    }

    @Transactional(readOnly = true)
    public List<News> newsList() {
        List<News> newsList = new ArrayList();
        newsList = newsRepository.findAll();
        return newsList;
    }

    @Transactional
    public News getOne(String id) {
        return newsRepository.getOne(id);
    }

    private void validate(String title, String body) throws MyException {
        if (title.isEmpty() || title == null) {
            throw new MyException("El título no puede estar vacío o ser nulo");
        }

        if (title.isEmpty() || title == null) {
            throw new MyException("El cuerpo de la noticia no puede estar vacío o ser nulo");
        }
    }

}
