package bookadvisor.bookadvisor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import bookadvisor.bookadvisor.domain.Genero;
import bookadvisor.bookadvisor.domain.Libro;

public interface LibroRepository extends JpaRepository <Libro, Long> {
    
    List<Libro> findByTituloContainingIgnoreCase(String nombre);
    List<Libro> findByGenero(Genero genero);
    Libro findById(int id);

    // consultas query

    // Cuenta cuántas valoraciones tiene un libro.
    @Query("SELECT COUNT(v) FROM Valoracion v WHERE v.libro.id = :libroId")
    long countVotacionesByLibroId(@Param("libroId") int libroId);

    //Suma total de puntos que ha recibido el libro.
    @Query("SELECT SUM(v.puntuacion) FROM Valoracion v WHERE v.libro.id = :libroId")
    int sumPuntosByLibroId(@Param("libroId") int libroId);

    //Promedio de puntuación del libro.
    @Query("SELECT AVG(v.puntuacion) FROM Valoracion v WHERE v.libro.id = :libroId")
    double avgPuntosByLibroId(@Param("libroId") int libroId);
}
