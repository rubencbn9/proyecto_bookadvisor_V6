package bookadvisor.bookadvisor.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import bookadvisor.bookadvisor.domain.Libro;
import bookadvisor.bookadvisor.domain.Usuario;
import bookadvisor.bookadvisor.domain.Valoracion;

public interface ValoracionRepository extends JpaRepository<Valoracion, Long>{
    Optional<Valoracion> findByUsuarioAndLibro(Usuario usuario, Libro libro); 
    Optional<Valoracion> findById(Long id);

    List<Valoracion> findByLibroId(Long id);

     @Query("SELECT AVG(v.puntuacion) FROM Valoracion v WHERE v.libro.id = :libroId")
    Double mediaPorLibro(@Param("libroId") Long libroId);
}
