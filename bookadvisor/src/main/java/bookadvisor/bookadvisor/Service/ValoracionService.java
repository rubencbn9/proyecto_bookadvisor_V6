package bookadvisor.bookadvisor.Service;

import java.util.List;
import java.util.Optional;

import bookadvisor.bookadvisor.domain.Usuario;
import bookadvisor.bookadvisor.domain.Valoracion;

public interface ValoracionService {
    
    List<Valoracion> obtenerValoraciones();

    Valoracion crearValoracion(Valoracion valoracion);

    Optional<Valoracion> getValoracionById(Long id);

   
    Valoracion actualizarValoracion(Long id, Valoracion valoracionDetails);

    void eliminarValoracion(Long id,Usuario usuarioActual);
}
