package com.lean.news.cotroller;

import com.lean.news.entity.News;
import com.lean.news.entity.Reader;

import com.lean.news.service.NewsService;

import com.lean.news.service.ReaderService;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Lean
 */
@Controller
@RequestMapping("/")
public class PortalController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private ReaderService readerService;

    @GetMapping("/")
    public String index() {

        return "index.html";
    }

    @GetMapping("/login")
    public String login() {

        return "login.html";
    }

    @Transactional
    @GetMapping("/list")
    public String list(Model model) {
        List<News> newsList = newsService.newsList();
        model.addAttribute("news", newsList);
        return "list.html";
    }

    @GetMapping("/register")
    public String registrition() {
        return "register_reader.html";
    }

    @Transactional
    @PostMapping("/registerReader")
    public String registerReader(@RequestParam String name, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password,
            @RequestParam String password2) {

        try {

            readerService.registerReader(name, lastName, email, password, password2);

            return "login.html";

        } catch (Exception e) {
            System.out.println("Error al crear usuario" + e);
            return "register_reader.html";
        }

    }

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_READER', 'ROLE_EDITOR')")
    @GetMapping("/home")
    public String home(HttpSession session, ModelMap modelo) {

        if (session.getAttribute("readerSession") != null) {
            Reader logged = (Reader) session.getAttribute("readerSession");
            modelo.put("readerSession", logged);

            if (logged.getRol().toString().equals("ADMINISTRADOR")) {

                return "redirect:/admin/dashboard";
            }
        }

        return "home.html";
    }
}
