package bookadvisor.bookadvisor.controllers;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public")
public class HomeController {

    @GetMapping({"/", "/home", "/index"})
    public String primeravista(Model model) {
        int anhoActual = LocalDate.now().getYear();
        model.addAttribute("anhoActual", anhoActual);
        return "indexView";
    }

    @GetMapping("/bienvenido")
    public String AnhoActual(Model model) {
        int anhoActual = LocalDate.now().getYear();
        model.addAttribute("anhoActual", anhoActual);
        return "bienvenido";
    }

    @GetMapping("/quienesSomos")
    public String quinenesSomos(Model model) {
        int anhoActual = LocalDate.now().getYear();
        model.addAttribute("anhoActual", anhoActual);
        return "quienesSomos";
    }

    @GetMapping("/proximamente")
    public String Proximo(Model model) {
        return "proximamente";
    }
}
