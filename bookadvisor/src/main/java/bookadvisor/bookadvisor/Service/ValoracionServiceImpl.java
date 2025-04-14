package bookadvisor.bookadvisor.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bookadvisor.bookadvisor.domain.Usuario;
import bookadvisor.bookadvisor.domain.Valoracion;
import bookadvisor.bookadvisor.repositories.ValoracionRepository;

@Service
public class ValoracionServiceImpl implements ValoracionService {

    @Autowired
    private ValoracionRepository valoracionRepository;

    public List<Valoracion> obtenerValoraciones() {
        return valoracionRepository.findAll();

    }

    public Valoracion crearValoracion(Valoracion valoracion) {
        Optional<Valoracion> existingValoracion = valoracionRepository.findByUsuarioAndLibro(valoracion.getUsuario(),
                valoracion.getLibro());
        if (existingValoracion.isPresent()) {
            throw new IllegalStateException("El usuario ya ha valorado este libro.");
        }
        return valoracionRepository.save(valoracion);
    }

    public Optional<Valoracion> getValoracionById(Long id) {
        return valoracionRepository.findById(id);
    }

    public Valoracion actualizarValoracion(Long id, Valoracion valoracionDetails) {
        Optional<Valoracion> optionalValoracion = valoracionRepository.findById(id);
        if (optionalValoracion.isPresent()) {
            Valoracion valoracion = optionalValoracion.get();
            valoracion.setPuntuacion(valoracionDetails.getPuntuacion());
            valoracion.setComentario(valoracionDetails.getComentario());
            return valoracionRepository.save(valoracion);
        }
        return null;
    }

    public void eliminarValoracion(Long id, Usuario usuarioActual) {
        Valoracion valoracion = valoracionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La valoración no existe"));

        if (!valoracion.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No puedes borrar valoraciones de otros usuarios");
        }

        valoracionRepository.deleteById(id);
    }
}
