/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lean.news.cotroller;

import com.lean.news.entity.News;
import com.lean.news.service.NewsService;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Lean
 */
@Controller
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/createNews")
    public String createNews() {
        return "createNews.html";
    }

    @GetMapping("/{id}")
    public String showNews(@PathVariable String id, Model model) {

        News news = newsService.getOne(id);
        model.addAttribute("news", news);
//        return "news.html";
        return "index.html";
    }

    @PostMapping("/postNews")
    public String postNews(@RequestParam String title, @RequestParam String body, @RequestParam(required = false) MultipartFile imageFile) {
        try {
            newsService.createNews(title, body, imageFile);
            return "index.html";

        } catch (Exception e) {
            System.out.println("Error en la carga de la noticia");
            return "createNews.html";
        }

    }

    @PutMapping("/editNews/{id}")
    public String editNews(@PathVariable String id, @RequestParam String title, @RequestParam String body, @RequestParam(required = false) MultipartFile imageFile) {
        try {
            newsService.actualizeNews(id, title, body, imageFile);
            return "index.html";
        } catch (Exception e) {
            System.out.println("Error al actualizar la noticia");
            return "createNews.html";
        }

    }
}
