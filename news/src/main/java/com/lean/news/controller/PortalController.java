package com.lean.news.controller;

import com.lean.news.entity.CustomUser;
import com.lean.news.entity.News;

import com.lean.news.service.NewsService;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Transactional
    @GetMapping("/")
    public String index(Model model) {

        News latestNews = newsService.newsList().get(0); //Tomo la noticia del primer lugar de la List

        model.addAttribute("latestNews", latestNews);

        List<News> newsList = newsService.newsList();

        newsList.remove(0); //remuevo la noticia del primer lugar de la List

        model.addAttribute("news", newsList);
        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String errorSubscriber, ModelMap model,
            @RequestParam(required = false) String success) throws UsernameNotFoundException {

        if ("registerSuccess".equals(success)) {
            model.put("registerSucces", "¡Gracias por registrarte en nuestra aplicación! "
                    + "Ahora puedes comenzar a utilizar nuestros servicios");
        }

        if (error != null) {

            model.put("error", "Usuario y/o Contraseña incorrecto, intente nuevamente");
        }

        if (errorSubscriber != null) {
            model.put("error", "Contenido exclusivo para suscriptores");
        }

        return "login.html";
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_READER', 'ROLE_WRITER')")
    @GetMapping("/home")
    public String home(HttpSession session, ModelMap model) {

        News latestNews = newsService.newsList().get(0); //Tomo la noticia del primer lugar de la List

        model.addAttribute("latestNews", latestNews);

        List<News> newsList = newsService.newsList();

        newsList.remove(0); //remuevo la noticia del primer lugar de la List

        model.addAttribute("news", newsList);

        if ((session.getAttribute("userSession") != null)) {

            CustomUser logged = (CustomUser) session.getAttribute("userSession"); /// CustomUser ES LA CLASE PADRE 

            model.put("userSession", logged);

        }

        return "index.html";
    }

    @Transactional
    @GetMapping("/category/{category}")
    public String category(@PathVariable String category, Model model) {

        List<News> newsList = newsService.categoryList(category);

        model.addAttribute("news", newsList);

        return "category.html";
    }

    @Transactional
    @GetMapping("/search")
    public String searchNewsByTitle(@RequestParam("word") String word, Model model) {

        List<News> newsList = newsService.findNewsByTitle(word);

        model.addAttribute("news", newsList);

        return "category.html";
    }

    @Transactional
    @GetMapping("/writer/{id}")
    public String filterByWriter(@PathVariable String id, Model model) {

        List<News> newsList = newsService.findNewsByWriter(id);

        model.addAttribute("news", newsList);

        return "category.html";
    }

}
