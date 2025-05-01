package bookadvisor.bookadvisor;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import bookadvisor.bookadvisor.Service.GeneroService;
import bookadvisor.bookadvisor.Service.LibroService;
import bookadvisor.bookadvisor.Service.UsuarioService;
import bookadvisor.bookadvisor.domain.Genero;
import bookadvisor.bookadvisor.domain.Idioma;
import bookadvisor.bookadvisor.domain.Libro;
import bookadvisor.bookadvisor.domain.Rol;
import bookadvisor.bookadvisor.domain.Usuario;

@SpringBootApplication
public class BookadvisorApplication {

   @Autowired
   UsuarioService usuarioService;
   @Autowired
   LibroService libroService;

   @Autowired 
   GeneroService generoService;

	public static void main(String[] args) {
		SpringApplication.run(BookadvisorApplication.class, args);
	}

	@Bean
CommandLineRunner init() {
    return args -> {

        // Crear los géneros primero
        Genero accion = generoService.crearGenero(new Genero(null,"ACCION"));    
        Genero drama = generoService.crearGenero(new Genero(null,"DRAMA"));    
        Genero comedia = generoService.crearGenero(new Genero(null,"COMEDIA"));  

        // Crear y añadir usuarios
        usuarioService.añadir(new Usuario(null, "admin", "ruben@mail.com", Rol.ADMINISTRADOR, "1234", null));
        usuarioService.añadir(new Usuario(null, "user", "ana@mail.com", Rol.USER, "1234", null));
        usuarioService.añadir(new Usuario(null, "manager", "juan@mail.com", Rol.MANAGER, "1234", null));

        // Crear y añadir libros, asignando géneros
        libroService.añadir(new Libro(null, "Cien años de soledad", LocalDate.of(1967, 6, 5), "Gabriel García Márquez", 
            Idioma.ESPAÑOL, "Una historia de la familia Buendía", LocalDate.now(), null, drama));

        libroService.añadir(new Libro(null, "El Quijote", LocalDate.of(1605, 1, 1), "Miguel de Cervantes", 
            Idioma.ESPAÑOL, "Un caballero que lucha contra gigantes", LocalDate.now(), null, comedia));

        libroService.añadir(new Libro(null, "1984", LocalDate.of(1949, 6, 8), "George Orwell", 
            Idioma.INGLÉS, "Una distopía en un régimen totalitario", LocalDate.now(), null, accion));

        libroService.añadir(new Libro(null, "Catching 22", LocalDate.of(1961, 11, 10), "Joseph Heller", 
            Idioma.INGLÉS, "Una sátira sobre la guerra", LocalDate.now(), null, accion));
    };
}


}
