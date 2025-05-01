package bookadvisor.bookadvisor.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import bookadvisor.bookadvisor.domain.Usuario;
import bookadvisor.bookadvisor.domain.Valoracion;

public interface ValoracionService {
    
    List<Valoracion> obtenerValoraciones();

    Valoracion crearValoracion(Valoracion valoracion);

    Optional<Valoracion> getValoracionById(Long id);

   
    Valoracion actualizarValoracion(Long id, Valoracion valoracionDetails);

    void eliminarValoracion(Long id,Usuario usuarioActual);

    Map<Long, Integer> obtenerSumaDePuntosPorLibro();

    Map<Long, Integer> obtenerCantidadDeVotantesPorLibro();

    Map<Long, Double> obtenerMediaDePuntuacionPorLibro();

    List<Valoracion> obtenerPorLibroId (Long id);
    
    List<Valoracion>obtenerPorUsuario (Long id);
}
