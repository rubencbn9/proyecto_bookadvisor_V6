package bookadvisor.bookadvisor.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import bookadvisor.bookadvisor.Service.LibroService;
import bookadvisor.bookadvisor.Service.UsuarioService;
import bookadvisor.bookadvisor.Service.ValoracionService;
import bookadvisor.bookadvisor.domain.Libro;
import bookadvisor.bookadvisor.domain.Usuario;
import bookadvisor.bookadvisor.domain.Valoracion;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/valoracion")

public class ValoracionController {

    @Autowired
    ValoracionService valoracionService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    LibroService libroService;

    @GetMapping({"/",""})
    public String getAllValoraciones(Model model,Authentication authentication) {
        List<Valoracion> valoraciones = valoracionService.obtenerValoraciones();
        model.addAttribute("isAuthenticated", authentication != null);
        model.addAttribute("valoraciones", valoraciones);
        return "valoracionesView";
    }

    @GetMapping("/nueva")
    public String nuevaValoracionForm(@RequestParam Long libroId, Model model, Principal principal) {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(principal.getName());
        Libro libro = libroService.obtenerPorId(libroId);
        
    Valoracion valoracion = new Valoracion();
    valoracion.setLibro(libro);
        model.addAttribute("valoracionForm", valoracion);
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("libro", libro); // ✅ esto faltaba
        return "newValoracionView";
    }

    @GetMapping("/{idLibro}")
    public String valorarPorId(@PathVariable Long idLibro, Model model, Principal principal) {
        Libro libro = libroService.obtenerPorId(idLibro);
        Valoracion valoracion = new Valoracion();
        valoracion.setLibro(libro); // Preconfiguramos el libro en la valoración
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(principal.getName());
        model.addAttribute("valoracionForm", new Valoracion());
        //model.addAttribute("usuarios", usuarioService.obtenerTodos());
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("libros", libroService.obtenerTodos());
        model.addAttribute("valoracionForm", valoracion);
        model.addAttribute("libro", libro); // Por si lo necesitas en la vista
    
        return "valoracionesView"; 
    }
    

//  @PostMapping("/nueva/submit")
// public String createValoracion(@ModelAttribute Valoracion valoracionForm, Model model, Principal principal) {
//     // Fetch the Usuario and Libro objects based on their IDs
//    // Usuario usuario = usuarioService.obtenerPorId(valoracion.getUsuario().getId().intValue());
//     Libro libro = libroService.obtenerPorId(valoracionForm.getLibro().getId());

//     // Set the fetched objects in the Valoracion object
//     valoracionForm.setUsuario(usuarioService.getUsuarioByNombre(principal.getName()));
//     valoracionForm.setLibro(libro);

//     // Save the Valoracion object
//     valoracionService.crearValoracion(valoracionForm);

//     return "redirect:/valoraciones";
// }

@PostMapping("/nueva/submit")
public String createValoracion(@ModelAttribute Valoracion valoracionForm, Model model, Principal principal) {
    Libro libro = libroService.obtenerPorId(valoracionForm.getLibro().getId());
    valoracionForm.setUsuario(usuarioService.getUsuarioByNombre(principal.getName()));
    valoracionForm.setLibro(libro);
    valoracionService.crearValoracion(valoracionForm);

    return "redirect:/libro/" + libro.getId(); // Redirige al libro, si prefieres
}


@PostMapping("/{id}")
public String borrarValoracion(@PathVariable Long id, Principal principal) {
    Usuario usuarioActual = usuarioService.getUsuarioByNombre(principal.getName());
    
    try {
        // Transformar el rol para hacerlo comparable
        String rol = usuarioActual.getRol().toString().replace("ROLE_", "");

        switch (rol) {
            case "ADMINISTRADOR": // El usuario es ADMIN
                valoracionService.eliminarValoracion(id, usuarioActual);
                break;
            case "USER": // El usuario es USER
                valoracionService.eliminarValoracion(id, usuarioActual);
                break;
            default:
                throw new SecurityException("No tienes permiso para realizar esta acción");
        }
    } catch (SecurityException e) {
        return "redirec:/public/accesserror"; 
    }

    return "redirect:/valoraciones";
}
     
}
