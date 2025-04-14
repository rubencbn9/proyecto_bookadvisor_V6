package bookadvisor.bookadvisor.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import bookadvisor.bookadvisor.Service.FileStorageService;
import bookadvisor.bookadvisor.Service.LibroService;
import bookadvisor.bookadvisor.domain.Genero;
import bookadvisor.bookadvisor.domain.Libro;
import bookadvisor.bookadvisor.domain.LibroDTO;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    LibroService libroService;

    @Autowired
    FileStorageService fileStorageService;

    @GetMapping({ "/list" })
    public String showBookList(@RequestParam(required = false) Integer numMsg,
            @RequestParam(required = false) String genero,
            Model model) {
        if (numMsg != null) {
            switch (numMsg) {
                case 1 -> model.addAttribute("msg", "Libro no encontrado");
                case 2 -> model.addAttribute("msg", "Formulario incorrecto");
            }
        }

        // Obtenemos la lista de libros DTO
        List<LibroDTO> listaLibros = libroService.convertLibroToDto(libroService.obtenerTodos());
        model.addAttribute("listaLibros", listaLibros);
        model.addAttribute("generoSeleccionado", null);

        // Calculamos la media de valoración para cada libro
        Map<Long, Double> medias = new HashMap<>();
        for (LibroDTO libro : listaLibros) {
            Double media = libroService.obtenerMediaValoracion(libro.getId());
            medias.put(libro.getId(), media);
        }

        // Añadimos las medias al modelo
        model.addAttribute("medias", medias);
        model.addAttribute("findForm", new Libro());

        return "bookListView";
    }

    @GetMapping("/{id}")
    public String showElement(@PathVariable long id, Model model) {
        try {
            Libro libro = libroService.obtenerPorId(id);
            Map<Long, Double> medias = new HashMap<>();
            medias.put(libro.getId(), libroService.obtenerMediaValoracion(id));

            model.addAttribute("libro", libro);
            model.addAttribute("medias", medias);

            return "bookView";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "bookListView";
        }
    }

    @GetMapping("/nuevo")
    public String showNew(Model model) {
        model.addAttribute("libroForm", new Libro());
        return "newBookView";
    }

    @PostMapping("/nuevo/submit")
    public String showNewSubmit(@Valid Libro libroForm,
            BindingResult bindingResult,
            @RequestParam MultipartFile portada) {
        if (bindingResult.hasErrors()) {
            return "redirect:/?numMsg=2";
        }
        try {
            Libro libro = libroService.añadir(libroForm, portada);
            if (libro == null) {
                return "redirect:/?numMsg=2";
            }
            return "redirect:/libro/list";
        } catch (RuntimeException e) {
            return "redirect:/?numMsg=3";
        }
    }

    // @PostMapping("/nuevo/submit")
    // public String showNewSubmit(@Valid Libro libroForm,
    // BindingResult bindingResult,
    // @RequestParam("portada") MultipartFile portada,
    // Model model) {
    // if (bindingResult.hasErrors()) {
    // return "redirect:/?numMsg=2";
    // }

    // if (!portada.isEmpty()) {
    // try {
    // String fileName = portada.getOriginalFilename();
    // String uploadDir = "src/main/resources/static/images/"; // Asegúrate de que
    // esta carpeta sea accesible
    // Path path = Paths.get(uploadDir + fileName);
    // Files.copy(portada.getInputStream(), path,
    // StandardCopyOption.REPLACE_EXISTING);
    // libroForm.setRutaFoto("/images/" + fileName); // Guardamos la ruta de la
    // imagen en la base de datos
    // } catch (IOException e) {
    // e.printStackTrace();
    // model.addAttribute("error", "Error al cargar la imagen: " + e.getMessage());
    // return "newBookView"; // Vuelve a la vista con el error
    // }
    // }

    // try {
    // Libro libro = libroService.añadir(libroForm);
    // if (libro == null) {
    // return "redirect:/?numMsg=2";
    // }
    // return "redirect:/libro/list";
    // } catch (RuntimeException e) {
    // return "redirect:/?numMsg=3";
    // }
    // }

    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable long id, Model model) {
        try {
            Libro libro = libroService.obtenerPorId(id);
            // El command object del formulario es el libro con el id solicitado
            if (libro == null) {
                throw new RuntimeException("libro no encontrado");
            }
            model.addAttribute("libroForm", libro);
            return "editFormView";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "bookListView";
        }
    }

    @PostMapping("/editar/{id}/submit")
    public String showEditSubmit(@PathVariable Long id, @RequestParam MultipartFile portada, @Valid Libro libroForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/?numMsg=2";
        }
        try {
            Libro libro = libroService.editar(libroForm, portada);
            if (libro == null) {
                return "redirect:/?numMsg=2";
            }
            return "redirect:/libro/list";
        } catch (RuntimeException e) {
            // Si se lanza una excepción de salario, redirigimos con el mensaje adecuado
            return "redirect:/?numMsg=3";
        }
    }

    @GetMapping("/borrar/{id}")
    public String showDelete(@PathVariable long id) {
        libroService.borrar(id);
        return "redirect:/libro/list";
    }

    @GetMapping("/findByName")
    public String showFindByNameForm(Model model) {
        // Crear una instancia del formulario de búsqueda
        Libro libroForm = new Libro();
        model.addAttribute("findForm", libroForm); // Pasar el objeto findForm al modelo
        return "bookListView"; // Vista con el formulario
    }

    @PostMapping("/findByName")
    public String showFindByNameSubmit(Libro libroForm, Model model) {
        model.addAttribute("findForm", libroForm);
        model.addAttribute("listaLibros", libroService.buscarPorTitulo(libroForm.getTitulo()));
        return "bookListView";
    }

    @GetMapping("/findByGenero/{genero}")
    public String showFindByGenero(@PathVariable Genero genero, Model model) {
        model.addAttribute("listaLibros", libroService.buscarPorGenero(genero));
        model.addAttribute("generoSeleccionado", genero);
        model.addAttribute("findForm", new Libro());
        return "bookListView";
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileStorageService.loadAsResource(filename);
        return ResponseEntity.ok().body(file);
    }

}
