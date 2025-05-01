package bookadvisor.bookadvisor.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import bookadvisor.bookadvisor.domain.FormInfo;
import bookadvisor.bookadvisor.domain.Usuario;
// import bookadvisor.bookadvisor.repositories.LibroRepository;
import bookadvisor.bookadvisor.repositories.UsuarioRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UsuarioServiceImpl implements UsuarioService{


     @Autowired
    private UsuarioRepository usuarioRepository;

    
    // @Autowired
    // private LibroRepository libroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
  

    @Autowired
    private JavaMailSender mailSender;
    private final Path rootLocation = Paths.get("uploadFotos");

    public void enviarEmail(FormInfo formInfo){
        MimeMessageHelper helper;
        String destinatario = "yagoaltafaj@hotmail.com";
        String asunto = "Nuevo contacto desde la web";
        String cuerpoMensaje = "Nombre: " + formInfo.getNombre() + "\n" +

                "Email: " + formInfo.getEmail() + "\n" +

                "Asunto: " + formInfo.getAsunto() + "\n" +
                "Acepto las condiciones: " + formInfo.isAceptoCondiciones() + "\n" +
                "Comentarios: " + formInfo.getTexto();

        MimeMessage message = mailSender.createMimeMessage();
        try {
            helper = new MimeMessageHelper(message, true);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }

        try {
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpoMensaje, true);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to set email properties", e);
        }

        mailSender.send(message);
    }

    public List<Usuario> obtenerTodos(){
        List<Usuario> lista = usuarioRepository.findAll();
        return lista;
    }

    public Usuario añadir(Usuario usuario){
        if (usuarioRepository.findByNombre(usuario.getNombre()) != null)
        return null; // ya existe ese nombre de usuario
    String passCrypted = passwordEncoder.encode(usuario.getPassword());
    usuario.setPassword(passCrypted);
    try {
        return usuarioRepository.save(usuario);
    } catch (DataIntegrityViolationException e) {
        e.printStackTrace();
        return null;
    }
    }

    public Usuario editar(Usuario usuario) {
        Usuario usuarioEncontrado = usuarioRepository.findById(usuario.getId()).orElse(null);
        if (usuarioEncontrado == null) {
            return null;  // Retorna null si no se encuentra el usuario
        }
        
        // Actualiza los campos del usuario encontrado con los datos del formulario
        usuarioEncontrado.setNombre(usuario.getNombre());
        usuarioEncontrado.setEmail(usuario.getEmail());
        usuarioEncontrado.setRol(usuario.getRol());
    
        // Guarda el usuario actualizado en la base de datos
        return usuarioRepository.save(usuarioEncontrado);
    }
    

    public boolean borrar(Long id){
        Usuario usuario = obtenerPorId(id);
        if (usuario == null) {
            return false;
        }
        usuarioRepository.delete(usuario);
        return true;
    }

    public List<Usuario> buscarPorNombre(String textoNombre){
        textoNombre = textoNombre.toLowerCase();
        List<Usuario> encontrados = usuarioRepository.findByNombreContainingIgnoreCase(textoNombre);

        return encontrados;
    }

    public String store(MultipartFile file, String titulo){
        if (file.isEmpty())
            throw new RuntimeException("Fichero vacío");
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.contains("..")) {
            throw new RuntimeException("Fichero incorrecto");
        }
        String extension = StringUtils.getFilenameExtension(filename);
        String storedFilename = titulo + "." + extension;

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        } catch (IOException ioe) {
            throw new RuntimeException("Error en escritura");
        }
    }

    public Usuario obtenerPorId(Long id){
        for (Usuario usuario : usuarioRepository.findAll()) {
            if (usuario.getId() == id) {

                return usuario;
            }
        }
        throw new RuntimeException(); 
    }

    public void delete(String filename){
        try {
            Path file = rootLocation.resolve(filename);
            if (!Files.exists(file))
                throw new RuntimeException("No existe el fichero");
            Files.delete(file);
        } catch (IOException ioe) {
            throw new RuntimeException("Error en borrado");
        }
    }

    public Resource loadAsResource(String filename){
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable())
                return resource;
            else
                throw new RuntimeException("Error IO");
        } catch (Exception e) {
            throw new RuntimeException("Error IO");
        }
    }

    public void updateUsuario(Usuario usuario){
        usuarioRepository.save(usuario);
    }

    public Usuario getUsuarioByNombre(String nombre){
        return usuarioRepository.findByNombre(nombre);

    }
}
