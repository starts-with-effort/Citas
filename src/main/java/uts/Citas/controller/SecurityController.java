package uts.Citas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/login")
    public String showLoginForm() {
        // Devuelve el nombre de la plantilla Thymeleaf (sin la extensión .html)
        // Spring Boot buscará en src/main/resources/templates/login.html
        return "login";
    }
}
