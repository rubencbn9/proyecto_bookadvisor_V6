package bookadvisor.bookadvisor.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bookadvisor.bookadvisor.domain.Genero;
import bookadvisor.bookadvisor.repositories.GeneroRepository;

@Service
public class GeneroServiceImpl implements GeneroService {
    
     @Autowired
     GeneroRepository generoRepository;

   public List<Genero> obtenerGeneros(){
    return generoRepository.findAll();
   }

   public Genero crearGenero(Genero genero){
    return generoRepository.save(genero);
   }

   public Optional<Genero> obtenerGeneroPorId(Long id){
    return generoRepository.findById(id);
   }

   public Genero actualizarGenero(Long id, Genero generoDetails){
    Optional<Genero> optionalGenero = generoRepository.findById(id);
    if (optionalGenero.isPresent()) {
        Genero genero = optionalGenero.get();
        genero.setNombre(generoDetails.getNombre());
        return generoRepository.save(genero);
    }
    return null;

   }

   public void deleteGenero(Long id){
    generoRepository.deleteById(id);

   }
}
