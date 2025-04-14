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
        model.addAttribute("usuarioForm", new Usuario());
         model.addAttribute("roles", Rol.values());
        return "newUsuarioView";
    }

    @PostMapping("/nuevo/submit")
    public String crearUsuario(@Valid Usuario usuarioForm, BindingResult BindingResult) {
        usuarioService.añadir(usuarioForm);
        return "redirect:/usuario/";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable int id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        model.addAttribute("usuario", usuario);
        return "newUsuarioView";
    }

    @PostMapping("/editar/submit")
    public String editarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.editar(usuario);
        return "redirect:/usuario";
    }

    @PostMapping("/borrar/{id}")
    public String borrarUsuario(@PathVariable int id) {
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
