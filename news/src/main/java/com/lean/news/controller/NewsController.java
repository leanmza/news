/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lean.news.controller;

import com.lean.news.entity.News;
import com.lean.news.exception.MyException;
import com.lean.news.service.NewsService;
import java.security.Principal;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String postNews(@RequestParam String title, @RequestParam String body,
            @RequestParam String category, @RequestParam boolean subscriberContent, MultipartFile imageFile, Principal principal)
            throws MyException {

        try {

            String writerEmail = principal.getName();

            newsService.createNews(title, body, imageFile, writerEmail, category, subscriberContent);

            return "redirect:/";

        } catch (Exception e) {
            System.out.println("Error en la carga de la noticia" + e.getMessage());
            return "createNews.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_WRITER', 'ROLE_EDITOR')")
    @Transactional
    @GetMapping("/editNews/{id}")
    public String editNews(@PathVariable String id, ModelMap model) {
 
        News news = newsService.getOne(id);
       
        model.addAttribute("news", news);
        return "editNews.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_WRITER', 'ROLE_EDITOR')")
    @Transactional
    @PostMapping("/editNews/{id}")
    public String editNews(@PathVariable String id, @RequestParam String title,
            @RequestParam String category, @RequestParam boolean subscriberContent, @RequestParam String body,
            @RequestParam(required = false) MultipartFile imageFile,  ModelMap model, Principal principal) throws MyException{
     
        try {
         
            String writerEmail = principal.getName();

            newsService.actualizeNews(id, title, body, imageFile, writerEmail, category, subscriberContent);
            
            return "redirect:/";
            
        } catch (Exception e) {
            
            System.out.println("Error al actualizar la noticia");
            
            return "createNews.html";
        }

    }

    @GetMapping("/{id}")
    public String showNews(@PathVariable String id, Model model,  Principal principal) {
//Primero validar si es subscriberContent
//Segundo validar si esta logueado

        News news = newsService.getOne(id); // traigo la noticia
        
        if (news.isSubscriberContent()){ // si subscriberContent es TRUE
            
                   
             
              if ( principal != null ) { //Verifica si hay usuario logueado
                
                  model.addAttribute("news", news);
                
                  return "news.html";
                
                //PONER UN TRY CATCH PARA MANEJAR EL ERROR
            } else {
                  
                model.addAttribute("errorSubscriber", "Contenido exclusivo para suscriptores");
                
                return "redirect:/login?errorSubscriber";
            }
              
        } else {
            
            
            
            model.addAttribute("news", news);
          
            return "news.html";
        }
  
    }
    
//    @GetMapping("/{id}")
//    public String showNews(@PathVariable String id, Model model) {
//        News news = newsService.getOne(id);
//        model.addAttribute("news", news);
//        return "news.html";
//    }

    @Transactional
    @GetMapping("/deleteNews/{id}")
    public String deleteNews(@PathVariable String id) throws MyException {
        System.out.println("controlador " + id);
        newsService.deleteNews(id);
        return "redirect:/home";
    }
}
