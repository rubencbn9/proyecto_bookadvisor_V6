package bookadvisor.bookadvisor.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import bookadvisor.bookadvisor.Service.GeneroService;
import bookadvisor.bookadvisor.domain.Genero;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/genero")
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    // Obtener todos los géneros y mostrarlos en la vista
    @GetMapping("/")
    public String listarGeneros(Model model, Authentication authentication) {
        List<Genero> generos = generoService.obtenerGeneros();
        model.addAttribute("isAuthenticated", authentication != null);
        model.addAttribute("generos", generos);
        return "listaGeneros";
    }

    // Mostrar formulario de creación de un nuevo género
    @GetMapping("/nuevo")
    public String mostrarFormularioDeCreacion(Model model) {
        model.addAttribute("generoForm", new Genero());
        return "formularioGenero";
    }

    // Crear un nuevo género
    @PostMapping("/nuevo/submit")
    public String crearGenero(@Valid Genero generoForm , BindingResult bindingResult) {
        generoService.crearGenero(generoForm);
        return "redirect:/genero/";
    }

    // Obtener un género por ID y mostrar el formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable Long id, Model model) {
        Optional<Genero> generoOpt = generoService.obtenerGeneroPorId(id);
        if (generoOpt.isPresent()) {
            model.addAttribute("genero", generoOpt.get());
            return "genero/formularioGenero";
        } else {
            return "redirect:/generos";
        }
    }

    // Actualizar un género existente
    @PostMapping("/editar")
    public String editarGenero(@ModelAttribute Genero genero) {
        generoService.actualizarGenero(genero.getId(), genero);
        return "redirect:/genero/";
    }

    // Eliminar un género por ID
    @PostMapping("/borrar/{id}")
    public String borrarGenero(@PathVariable Long id) {
        generoService.deleteGenero(id);
        return "redirect:/genero/";
    }
}

