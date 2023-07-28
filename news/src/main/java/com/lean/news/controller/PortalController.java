package com.lean.news.controller;

import com.lean.news.entity.News;
import com.lean.news.entity.Reader;
import com.lean.news.exception.MyException;
import com.lean.news.service.NewsService;
import com.lean.news.service.ReaderService;
import com.lean.news.service.WriterService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    
    @Autowired
    private WriterService writerService;

    @Transactional
    @GetMapping("/")
    public String index(Model model) {
        List<News>newsList = newsService.newsList();
        model.addAttribute("news", newsList);
        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model, 
            @RequestParam(required = false) String success) throws UsernameNotFoundException {

        if ("registerSuccess".equals(success)) {
            model.put("registerSucces", "¡Gracias por registrarte en nuestra aplicación! "
                    + "Ahora puedes comenzar a utilizar nuestros servicios");
        }

        if (error != null) {

            model.put("error", "Usuario y/o Contraseña incorrecto, intente nuevamente");
        }

        return "login.html";
    }


    @GetMapping("/register")
    public String registritionReader() {
        return "register_reader.html";
    }

    @Transactional
    @PostMapping("/registerReader")
    public String registerReader(@RequestParam String name, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, @RequestParam(required = false) MultipartFile imageFile,
            ModelMap modelo) throws MyException {

        try {

            readerService.registerReader(name, lastName, email, password, password2, imageFile);

            return "redirect:/login?success=registerSuccess";

        } catch (MyException me) {
            System.out.println("¡Registro de paciente FALLIDO!\n" + me.getMessage());
            modelo.put("error", me.getMessage());
        }

        modelo.put("name", name);
        modelo.put("lastName", lastName);
        modelo.put("email", email);
        modelo.put("password", password);

        return "register_reader.html";

    }
    
    
        @GetMapping("/registerWriter")
    public String registritionWriter() {
        return "register_writer.html";
    }

    @Transactional
    @PostMapping("/registerWriter")
    public String registerWriter(@RequestParam String name, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password,
            @RequestParam String password2,@RequestParam(required = false) MultipartFile imageFile,
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

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_READER', 'ROLE_WRITER')")
    @GetMapping("/home")
    public String home(HttpSession session, ModelMap model) {
        
        List<News> newsList = newsService.newsList();
        
        model.addAttribute("news", newsList);
        
        if (( (session.getAttribute("readerSession") != null))  || (session.getAttribute("writerSession") != null)) { //REVISAR ACA, FALTA MANEJAR WRITER
            
            Reader logged = (Reader) session.getAttribute("readerSession");
            model.put("readerSession", logged);

            if (logged.getRol().toString().equals("EDITOR")) {

                return "redirect:/admin/dashboard";
            }
        }
        

        return "home.html";
    }
    
    @Transactional
    @GetMapping("/category/{category}")
    public String category(@PathVariable String category, ModelMap model){
        
        List<News> newsList = newsService.categoryList(category);
        
        model.addAttribute("news", newsList);
        
        return "category.html";
    }
}
