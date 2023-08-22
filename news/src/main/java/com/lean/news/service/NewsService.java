/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lean.news.service;

import com.lean.news.entity.Image;
import com.lean.news.entity.News;

import com.lean.news.entity.Writer;
import com.lean.news.enums.Category;
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
    public void createNews(String title, String body, MultipartFile imageFile, String writerEmail, String category, boolean subscriberContent)
            throws MyException {

        validate(title, body, writerEmail, category);

        News news = new News();

        news.setTitle(title);
        news.setBody(body);
        news.setDateLog(LocalDateTime.now());

        if (imageFile != null) {

            Image image = imageService.saveImage(imageFile);
            news.setImage(image);
        }

        Writer writer = writerRepository.findWriterByEmail(writerEmail);

        news.setWriter(writer);

        news.setCategory(Category.valueOf(category));

        if (subscriberContent == true) {

            news.setSubscriberContent(true);
        }

        newsRepository.save(news);
    }

    @Transactional
    public void actualizeNews(String id, String title, String body, MultipartFile imageFile, String writerEmail, String category, boolean subscriberContent)
            throws MyException {

        validate(title, body, writerEmail, category);

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

            news.setCategory(Category.valueOf(category));

            if (subscriberContent == false) {

                news.setSubscriberContent(false);
            } else if (subscriberContent == true) {

                news.setSubscriberContent(true);
            }

            newsRepository.save(news);
        }
    }

    private void validate(String title, String body, String writerEmail, String category) throws MyException {
        if (title.isEmpty() || title == null) {
            throw new MyException("El título no puede estar vacío o ser nulo");
        }

        if (title.isEmpty() || body == null) {
            throw new MyException("El cuerpo de la noticia no puede estar vacío o ser nulo");
        }

        if (writerEmail.isEmpty() || writerEmail == null) {
            throw new MyException("El id del autor no puede estar vacío o ser nulo");
        }

        if (checkCategory(category) == false) {
            throw new MyException("La categoría ingresada no es válida");
        }

    }

    private boolean checkCategory(String category) {

        boolean check = false;

        Category[] listCategorys = Category.values();

        for (int i = 0; i < listCategorys.length; i++) {

            if (category.equals(listCategorys[i].toString())) {
                check = true;

                break;

            }
        }
        return check;
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

    @Transactional(readOnly = true)
    public List<News> newsList() { //Muestra la noticia más nueva primero
        List<News> newsList = new ArrayList();

        newsList = newsRepository.listOrderedNews();

        return newsList;

    }

    @Transactional
    public News getOne(String id) {
        return newsRepository.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<News> findNewsByTitle(String word) { //Muestra las noticias con la palabra buscada
        List<News> newsList = new ArrayList();
        newsList = newsRepository.findTitleByWord(word);
        return newsList;
    }

    @Transactional(readOnly = true)
    public List<News> categoryList(String category) { //Muestra muestra las noticias de la category con la noticia más nueva primero

        List<News> newsList = new ArrayList();

        Category categoryEnum = Category.valueOf(category.toUpperCase());// Category viene en minúsculas del HTML, lo paso a mayúsculas 

        newsList = newsRepository.listNewsByCategory(categoryEnum);

        return newsList;
    }

    @Transactional(readOnly = true)
    public List<News> findNewsByWriter(String id) { //Muestra las noticias con la palabra buscada
        Writer writer = writerRepository.findById(id).get();

        List<News> newsList = new ArrayList();
        newsList = newsRepository.listNewsByWriter(writer);
        return newsList;
    }

    @Transactional(readOnly = true)
    public List<News> newsListAsc() { //Muestra la noticia más nueva primero
        List<News> newsList = new ArrayList();

        newsList = newsRepository.listOrderedNewsAsc();

        return newsList;
    }

    @Transactional(readOnly = true)
    public List<News> newsListWriterAZ() { //Muestra la noticia más nueva primero
        List<News> newsList = new ArrayList();

        newsList = newsRepository.orderByWriterDesc();

        return newsList;
    }

    @Transactional(readOnly = true)
    public List<News> newsListWriterZA() { //Muestra la noticia más nueva primero
        List<News> newsList = new ArrayList();

        newsList = newsRepository.orderByWriterAsc();

        return newsList;
    }

    @Transactional(readOnly = true)
    public List<News> newsListTitleDesc() { //Muestra la noticia más nueva primero
        List<News> newsList = new ArrayList();

        newsList = newsRepository.orderByTitleDesc();

        return newsList;
    }

    @Transactional(readOnly = true)
    public List<News> newsListTitleAsc() { //Muestra la noticia más nueva primero
        List<News> newsList = new ArrayList();

        newsList = newsRepository.orderByTitleAsc();

        return newsList;
    }
}
