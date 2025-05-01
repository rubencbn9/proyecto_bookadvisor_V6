package bookadvisor.bookadvisor.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import bookadvisor.bookadvisor.Service.UsuarioService;
import bookadvisor.bookadvisor.domain.Rol;
import bookadvisor.bookadvisor.domain.Usuario;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"/",""})
    public String listarUsuarios(Model model,Authentication authentication) {
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        model.addAttribute("isAuthenticated", authentication != null);
        model.addAttribute("usuarios", usuarios);
        return "usuarioList";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeCreacion(Model model) {
        Usuario usuario = new Usuario();
        usuario.setRol(Rol.USER);
        model.addAttribute("usuarioForm",  usuario);
         model.addAttribute("roles", Rol.values());
        return "newUsuarioView";
    }

    @PostMapping("/nuevo/submit")
    public String crearUsuario(@Valid Usuario usuarioForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "newUsuarioView"; // Vuelve a mostrar el formulario con los errores
        }
        usuarioService.añadir(usuarioForm);
        return "redirect:/usuario/";
    }
    

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario != null) {
            model.addAttribute("usuarioForm", usuario);  // Usar 'usuarioForm' como atributo en el modelo
            model.addAttribute("roles", Rol.values());    // Pasa los roles para el selector
            return "editUsuarioView"; // Vista para editar usuario
        } else {
            return "redirect:/usuario/";  // Redirige si no se encuentra el usuario
        }
    }
    
    @PostMapping("/editar/submit")
    public String editarUsuario(@Valid @ModelAttribute("usuarioForm") Usuario usuarioForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editUsuarioView";  // Si hay errores, vuelve a mostrar el formulario
        }
        usuarioService.editar(usuarioForm);  // Llama al servicio para actualizar el usuario
        return "redirect:/usuario/";  // Redirige a la lista de usuarios
    }
    
    

    @PostMapping("/borrar/{id}")
    public String borrarUsuario(@PathVariable Long id) {
        usuarioService.borrar(id);
        return "redirect:/usuario";
    }

    @PostMapping("/upload")
    public String subirArchivo(@RequestParam("file") MultipartFile file, @RequestParam("titulo") String titulo) {
        usuarioService.store(file, titulo);
        return "redirect:/usuario";
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public Resource descargarArchivo(@PathVariable String filename) {
        return usuarioService.loadAsResource(filename);
    }
}
