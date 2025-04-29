// package bookadvisor.bookadvisor.controllers;

// import java.time.LocalDate;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import bookadvisor.bookadvisor.Service.FileStorageService;
// import bookadvisor.bookadvisor.Service.LibroService;
// import bookadvisor.bookadvisor.domain.Genero;
// import bookadvisor.bookadvisor.domain.Libro;
// import jakarta.validation.Valid;



// @Controller
// public class controlador {
    
//      @Autowired
//      LibroService libroService;
//     @Autowired
//      JavaMailSender mailSender;

//     private final String gerenteEmail = "paralaspruebasgenerales@gmail.com"; 

   
//     @GetMapping({"/", "/home", "/index"})
//     public String primeravista(Model model) {
//         int anhoActual = LocalDate.now().getYear();
//         model.addAttribute("anhoActual", anhoActual);
//         return "indexView";
//     }
    
//     @GetMapping("/bienvenido")
//     public String AnhoActual( Model model) {
//          int anhoActual = LocalDate.now().getYear();
//         model.addAttribute("anhoActual", anhoActual);
//         return "bienvenido";
//     }
    

//     @GetMapping("/quienesSomos")
//     public String quinenesSomos(Model model) {
//         int anhoActual = LocalDate.now().getYear();
//         model.addAttribute("anhoActual", anhoActual);
//         return "quienesSomos";
//     }

//     @GetMapping("/proximamente")
//     public String Proximo(Model model) {
//        return "proximamente";
//     }

//      @GetMapping("/contacta")
//     public String showContactForm() {
//         return "contacta";
//     }

//     @PostMapping("/contacta")
//     public String handleContactForm(@RequestParam String nombre, 
//                                      @RequestParam String email,
//                                      @RequestParam String motivo,
//                                      @RequestParam(required = false) boolean condiciones,
//                                      Model model) {
//         if (!condiciones) {
//             model.addAttribute("error", "Debes aceptar las condiciones de servicio.");
//             return "contacta";  // Si no acepta las condiciones, volver al formulario
//         }

//         // Enviar el correo al gerente
//         SimpleMailMessage mensaje = new SimpleMailMessage();
//         mensaje.setTo(gerenteEmail); 
//         mensaje.setText("Nombre: %s\nEmail: %s\nMotivo: %s".formatted(nombre, email, motivo));
//         mensaje.setSubject("Nuevo mensaje de contacto");
//         mensaje.setText(String.format("Nombre: %s\nEmail: %s\nMotivo: %s", nombre, email, motivo));

//         mailSender.send(mensaje);

//         // Redirigir a la página de confirmación con los datos enviados
//         model.addAttribute("nombre", nombre);
//         model.addAttribute("email", email);
//         model.addAttribute("motivo", motivo);
//         return "confirmacion";  // Página de confirmación
//     }

// // aqui empieza la copia del otro ejercicio
// @Autowired
// private FileStorageService fileStorageService;

// @GetMapping({ "/bookListView"})
//     public String showBookList(@RequestParam(required = false) Integer numMsg,@RequestParam(required = false) String genero,  Model model) {
//         if (numMsg != null) {
//             switch (numMsg) {
//                 case 1 -> model.addAttribute("msg", "Libro no encontrado");
//                 case 2 -> model.addAttribute("msg", "Formulario incorrecto");
//             }
//         }
//         if (genero != null && !genero.isEmpty()) {
//             model.addAttribute("listaLibros", libroService.buscarPorGenero(Genero.valueOf(genero)));
//             model.addAttribute("generoSeleccionado", Genero.valueOf(genero));
//         } else {
//             model.addAttribute("listaLibros", libroService.obtenerTodosOrdenadosPorTitulo());
//             model.addAttribute("generoSeleccionado", null);
//         }
//         model.addAttribute("findForm", new Libro());
//         return "bookListView";
//     }




//     @GetMapping("/{id}")
//     public String showElement(@PathVariable long id, Model model) {
//        try {
//         Libro libro = libroService.obtenerPorId(id);
//         model.addAttribute("libro", libro);
//         return "bookView";
        
//        } catch (RuntimeException e) {
//         model.addAttribute("error", e.getMessage());
//         return "bookListView";
//        } 
//     }

//     @GetMapping("/nuevo")
//     public String showNew(Model model) {
//         // el commandobject del formulario es una instancia de libro vacia
//         model.addAttribute("libroForm", new Libro());
//         return "newBookView";
//     }

//     @PostMapping("/nuevo/submit")
//     // public String showNewSubmit(@Valid Libro libroForm,@RequestParam (required=false) MultipartFile file ,
//     public String showNewSubmit(@Valid Libro libroForm ,BindingResult bindingResult) {
//         if (bindingResult.hasErrors()) {
//             return "redirect:/?numMsg=2";
//         }
//         try {
//             // fileStorageService.tratarFichero(file);
//             Libro libro = libroService.añadir(libroForm);
//             if (libro == null) {
//                 return "redirect:/?numMsg=2";
//             }
//             return "redirect:/bookListView";
//         } catch (RuntimeException e) {
//             return "redirect:/?numMsg=3";
//         }
//     }

//     @GetMapping("/editar/{id}")
// public String showEditForm(@PathVariable long id, Model model) {
//     try {
//         Libro libro = libroService.obtenerPorId(id);
//         // El command object del formulario es el libro con el id solicitado
//         if (libro == null) {
//             throw new RuntimeException("libro no encontrado");
//         }
//         model.addAttribute("libroForm", libro);
//         return "editFormView";
//     } catch (RuntimeException e) {
//         model.addAttribute("error", e.getMessage());
//         return "bookListView"; 
//     }
// }

// @PostMapping("/editar/{id}/submit")
// public String showEditSubmit(@PathVariable Long id, @Valid Libro libroForm,
//         BindingResult bindingResult) {
//     if (bindingResult.hasErrors()) {
//         return "redirect:/?numMsg=2";
//     }
//     try {
//         Libro libro = libroService.editar(libroForm);
//         if (libro == null) {
//             return "redirect:/?numMsg=2";
//         }
//         return "redirect:/bookListView";
//     } catch (RuntimeException e) {
//         // Si se lanza una excepción de salario, redirigimos con el mensaje adecuado
//         return "redirect:/?numMsg=3";
//     }
// }

//     @GetMapping("/borrar/{id}")
//     public String showDelete(@PathVariable long id) {
//         libroService.borrar(id);
//         return "redirect:/bookListView";
//     }


//     @GetMapping("/findByName")
//     public String showFindByNameForm(Model model) {
//         // Crear una instancia del formulario de búsqueda
//         Libro libroForm = new Libro(); 
//         model.addAttribute("findForm", libroForm);  // Pasar el objeto findForm al modelo
//         return "bookListView";  // Vista con el formulario
//     }

//     @PostMapping("/findByName")
// public String showFindByNameSubmit(Libro libroForm, Model model) {
//     model.addAttribute("findForm", libroForm);    
// model.addAttribute("listaLibros",libroService.buscarPorTitulo(libroForm.getTitulo())); 
// return "bookListView";
// }


// @GetMapping("/findByGenero/{genero}")
// public String showFindByGenero(@PathVariable Genero genero, Model model) {
//         model.addAttribute("listaLibros", libroService.buscarPorGenero(genero));
//         model.addAttribute("generoSeleccionado", genero);
//         model.addAttribute("findForm", new Libro()); 
//         return "bookListView";
// }

    
    
// }
