package bookadvisor.bookadvisor.Service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import bookadvisor.bookadvisor.domain.FormInfo;
import bookadvisor.bookadvisor.domain.Usuario;

public interface UsuarioService {

     void enviarEmail(FormInfo formInfo);

     List<Usuario> obtenerTodos();

     Usuario añadir(Usuario usuario);

     Usuario editar(Usuario usuario);

     boolean borrar(Long id);

     List<Usuario> buscarPorNombre(String textoNombre);

     String store(MultipartFile file, String titulo);

     Usuario obtenerPorId(Long id);

     void delete(String filename);

     Resource loadAsResource(String filename);

     void updateUsuario(Usuario usuario);

     Usuario getUsuarioByNombre(String nombre);
     

}
