/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lean.news.controller;

import com.lean.news.entity.News;
import com.lean.news.service.NewsService;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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

    @PreAuthorize("hasAnyRole('ROLE_WRITER', 'ROLE_EDITOR')")
    @PostMapping("/postNews")
    public String postNews(@RequestParam String title, @RequestParam String body, MultipartFile imageFile, HttpSession session) {
        try {
            String idWriter = session.getId();
            System.out.println("controlador id writer + "+ idWriter );
            newsService.createNews(title, body, imageFile, idWriter);
            return "redirect:/";

        } catch (Exception e) {
            System.out.println("Error en la carga de la noticia");
            return "createNews.html";
        }

    }



    @PreAuthorize("hasAnyRole('ROLE_WRITER', 'ROLE_EDITOR')")
    @Transactional
    @GetMapping("/editNews/{id}")
    public String editNews (@PathVariable String id, ModelMap model){
         System.out.println("id "+ id);
         News news = newsService.getOne(id);
         System.out.println("news " + news);
        model.addAttribute("news", news);
        return "editNews.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_WRITER', 'ROLE_EDITOR')")
    @Transactional
    @PostMapping("/editNews/{id}")
    public String editNews(@PathVariable String id, @RequestParam String title, @RequestParam String body, @RequestParam(required = false) MultipartFile imageFile, ModelMap model, HttpSession session) {
        try {
            String idWriter = session.getId();
       
            newsService.actualizeNews(id, title, body, imageFile, idWriter);
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("Error al actualizar la noticia");
            return "createNews.html";
        }

    }
    
        @GetMapping("/{id}")
    public String showNews(@PathVariable String id, Model model) {

        News news = newsService.getOne(id);
        model.addAttribute("news", news);
        return "news.html";
    }
}



