package bookadvisor.bookadvisor.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/contacta")
public class ContactoController {

    @Autowired
    JavaMailSender mailSender;

    private final String gerenteEmail = "paralaspruebasgenerales@gmail.com";

    @GetMapping("/")
    public String showContactForm() {
        return "contacta";
    }

    @PostMapping("/submit")
    public String handleContactForm(@RequestParam String nombre, 
                                     @RequestParam String email,
                                     @RequestParam String motivo,
                                     @RequestParam(required = false) boolean condiciones,
                                     Model model) {
        if (!condiciones) {
            model.addAttribute("error", "Debes aceptar las condiciones de servicio.");
            return "contacta";
        }

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(gerenteEmail);
        mensaje.setText(String.format("Nombre: %s\nEmail: %s\nMotivo: %s", nombre, email, motivo));
        mensaje.setSubject("Nuevo mensaje de contacto");

        mailSender.send(mensaje);

        model.addAttribute("nombre", nombre);
        model.addAttribute("email", email);
        model.addAttribute("motivo", motivo);
        return "confirmacion";
    }
}
