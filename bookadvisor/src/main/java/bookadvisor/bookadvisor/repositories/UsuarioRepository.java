package bookadvisor.bookadvisor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import bookadvisor.bookadvisor.domain.Libro;
import bookadvisor.bookadvisor.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    Usuario findByNombre(String nombre);

    Usuario findByEmail(String email);

    Libro findById(int id);
}
