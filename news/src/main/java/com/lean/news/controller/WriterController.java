/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lean.news.controller;

import com.lean.news.entity.CustomUser;
import com.lean.news.entity.Reader;
import com.lean.news.entity.Writer;
import com.lean.news.exception.MyException;
import com.lean.news.service.WriterService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/writer")
public class WriterController {

    @Autowired
    private WriterService writerService;

    // ------------------------ REGISTRO DE WRITER ----------------------------------
    @GetMapping("/register")
    public String registritionWriter() {
        return "register_writer.html";
    }

    @Transactional
    @PostMapping("/register")
    public String registerWriter(@RequestParam String name, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, @RequestParam(required = false) MultipartFile imageFile,
            ModelMap modelo) throws MyException {

        try {

            writerService.registerWriter(name, lastName, email, password, password2, imageFile);

            return "redirect:/login?success=registerSuccess";

        } catch (MyException me) {
            System.out.println("¡Registro de paciente FALLIDO!\n" + me.getMessage());
            modelo.put("error", me.getMessage());
        }

        modelo.put("name", name);
        modelo.put("lastName", lastName);
        modelo.put("email", email);
        modelo.put("password", password);

        return "register_writer.html";

    }

    // ------------------------ ACTUALIZACIÓN DE WRITER ----------------------------------
    @Transactional
    @GetMapping("/profile")
    public String actualizeReader(HttpSession session, ModelMap model) {

        
      CustomUser writer = (CustomUser) session.getAttribute("userSession"); ///  CustomUser ES LA CLASE PADRE 

        model.put("writer", writer);

        System.out.println("session actualize " + session.getAttribute("userSession"));
        
        return "edit_writer.html";
    }

    @Transactional
    @PostMapping("/profile/{id}")
    public String actualizeReader(@PathVariable String id, @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName, @RequestParam(required = false) String password,
            @RequestParam(required = false) String password2, @RequestParam(required = false) MultipartFile imageFile,
            ModelMap modelo) throws MyException {

        try {

            writerService.actualizeWriter(id, name, lastName, password, password2, imageFile);

            return "redirect:/home";

        } catch (MyException me) {
            System.out.println("¡Registro de paciente FALLIDO!\n" + me.getMessage());
            modelo.put("error", me.getMessage());
        }

        modelo.put("name", name);
        modelo.put("lastName", lastName);
        modelo.put("password", password);

        return "register_writer.html";

    }
}
