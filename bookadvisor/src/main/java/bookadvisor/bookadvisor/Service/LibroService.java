package bookadvisor.bookadvisor.Service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import bookadvisor.bookadvisor.domain.Genero;
import bookadvisor.bookadvisor.domain.Libro;
import bookadvisor.bookadvisor.domain.LibroDTO;

public interface LibroService {

      List<Libro> obtenerTodosOrdenadosPorTitulo();

      List<Libro> obtenerTodos();

      Libro obtenerPorId(long id);

      Libro añadir(Libro libro, MultipartFile file);

      Libro editar(Libro libro, MultipartFile multipartFile);

      boolean borrar(Long id);

      List<Libro> buscarPorTitulo(String textoTitulo);

      List<Libro> buscarPorGenero(Genero genero);

      // public String store(MultipartFile file, String titulo);

      public void delete(String filename);

      public Resource loadAsResource(String filename);

      public void updateLibro(Libro libro);

      public List<LibroDTO> convertLibroToDto(List<Libro> listaLibros);

      public long contarVotantes(int libroId);

      public int sumarPuntos(int libroId);

      public double mediaPuntos(int libroId);

      Double obtenerMediaValoracion(Long libroId);

      void borrarImagen(Long id);


}
