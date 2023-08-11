package com.lean.news.controller;

import com.lean.news.entity.CustomUser;
import com.lean.news.entity.Reader;
import com.lean.news.exception.MyException;
import com.lean.news.service.ReaderService;
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
@RequestMapping("/reader")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @GetMapping("/register")
    public String registritionReader() {
        return "register_reader.html";
    }

    // ------------------------ REGISTRO DE READER ----------------------------------
    @Transactional
    @PostMapping("/register")
    public String registerReader(@RequestParam String name, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, @RequestParam(required = false) MultipartFile imageFile,
            ModelMap model) throws MyException {

        try {

            readerService.registerReader(name, lastName, email, password, password2, imageFile);

            return "redirect:/login?success=registerSuccess";

        } catch (MyException me) {
            System.out.println("¡Registro de paciente FALLIDO!\n" + me.getMessage());
            model.put("error", me.getMessage());
        }

        model.put("name", name);
        model.put("lastName", lastName);
        model.put("email", email);
        model.put("password", password);

        return "register_reader.html";

    }

    // ------------------------ ACTUALIZACIÓN DE READER ----------------------------------
    @Transactional
    @GetMapping("/profile")
    public String actualizeReader(HttpSession session, ModelMap model) {

       CustomUser reader = (CustomUser) session.getAttribute("userSession"); ///   CustomUser ES LA CLASE PADRE

        model.put("reader", reader);
 
              System.out.println("session actualize " + session.getAttribute("userSession"));
        return "edit_reader.html";
    }

    @Transactional
    @PostMapping("/profile/{id}")
    public String actualizeReader(@PathVariable String id, @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName, @RequestParam(required = false) String password,
            @RequestParam(required = false) String password2, @RequestParam(required = false) MultipartFile imageFile,
            ModelMap modelo) throws MyException {

        try {

            readerService.actualizeReader(id, name, lastName, password, password2, imageFile);

            return "redirect:/home";

        } catch (MyException me) {
            System.out.println("¡Registro de paciente FALLIDO!\n" + me.getMessage());
            modelo.put("error", me.getMessage());
        }

        modelo.put("name", name);
        modelo.put("lastName", lastName);
        modelo.put("password", password);

        return "register_reader.html";

    }
}
