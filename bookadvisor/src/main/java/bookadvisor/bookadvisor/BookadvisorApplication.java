package bookadvisor.bookadvisor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import bookadvisor.bookadvisor.Service.UsuarioService;
import bookadvisor.bookadvisor.domain.Rol;
import bookadvisor.bookadvisor.domain.Usuario;

@SpringBootApplication
public class BookadvisorApplication {

   @Autowired
   UsuarioService usuarioService;

	public static void main(String[] args) {
		SpringApplication.run(BookadvisorApplication.class, args);
	}

	@Bean
	CommandLineRunner init() {
		return args -> {
			usuarioService.añadir(new Usuario(null, "ruben","ruben@mail.com",Rol.ADMINISTRADOR,"1234",null)); 
		};
	}

}
