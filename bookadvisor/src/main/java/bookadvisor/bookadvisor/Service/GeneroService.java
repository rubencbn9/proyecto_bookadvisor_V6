package bookadvisor.bookadvisor.Service;

import java.util.List;
import java.util.Optional;

import bookadvisor.bookadvisor.domain.Genero;

public interface GeneroService {
    

    List<Genero> obtenerGeneros();

    Genero crearGenero(Genero genero);

    Optional<Genero> obtenerGeneroPorId(Long id);

    Genero actualizarGenero(Long id, Genero generoDetails);

    void deleteGenero(Long id);
}
