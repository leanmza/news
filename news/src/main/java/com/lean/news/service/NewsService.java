/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lean.news.service;

import com.lean.news.entity.Image;
import com.lean.news.entity.News;
import com.lean.news.entity.Writer;
import com.lean.news.exception.MyException;
import com.lean.news.repository.NewsRepository;
import com.lean.news.repository.WriterRepository;
import java.time.LocalDateTime;
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
    private WriterRepository writerRepository;

    @Autowired
    private ImageService imageService;

    @Transactional
    public void createNews(String title, String body, MultipartFile imageFile, String writerEmail)
            throws MyException {

        validate(title, body, writerEmail);

        News news = new News();

        news.setTitle(title);
        news.setBody(body);
        news.setDateLog(LocalDateTime.now());

        if (imageFile != null) {
            System.out.println("hay imagen");
            Image image = imageService.saveImage(imageFile);
            news.setImage(image);
        }

        Writer writer = writerRepository.findWriterByEmail(writerEmail);

        news.setWriter(writer);

        newsRepository.save(news);
    }

    @Transactional
    public void actualizeNews(String id, String title, String body, MultipartFile imageFile, String writer)
            throws MyException {

        validate(title, body, writer);

        Optional<News> optionalNews = newsRepository.findById(id);

        if (optionalNews.isPresent()) {
            News news = optionalNews.get();

            news.setTitle(title);
            news.setBody(body);

            if (!(imageFile.isEmpty())) { ///Comprueba si el imageFile no está vacio 

                String idImage = news.getImage().getId(); // idImage toma el valor del id de la imagen existente

                Image image = imageService.actualizeImage(idImage, imageFile); // Actualiza la imagen en su repo con el id existente y el nuevo archivo

                news.setImage(image); //Establece la imagen nueva

            }

            newsRepository.save(news);
        }
    }

    @Transactional(readOnly = true)
    public List<News> newsList() {
        List<News> newsList = new ArrayList();
        newsList = newsRepository.listOrderedNews();
        return newsList;
//              List<News> newsList = new ArrayList();
//        newsList = newsRepository.findAll();
//        return newsList;
    }

    @Transactional
    public News getOne(String id) {
        return newsRepository.getOne(id);
    }

    private void validate(String title, String body, String writerEmail) throws MyException {
        if (title.isEmpty() || title == null) {
            throw new MyException("El título no puede estar vacío o ser nulo");
        }

        if (title.isEmpty() || body == null) {
            throw new MyException("El cuerpo de la noticia no puede estar vacío o ser nulo");
        }

        if (writerEmail.isEmpty() || writerEmail == null) {
            throw new MyException("El id del autor no puede estar vacío o ser nulo");
        }
    }

    @Transactional
    public void deleteNews(String id) throws MyException {

        System.out.println("id News " + id);
        Optional<News> optionalNews = newsRepository.findById(id);
        if (optionalNews.isPresent()) {
            News news = optionalNews.get();
            newsRepository.delete(news);
        }
    }
}
