package bookadvisor.bookadvisor.Service;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import bookadvisor.bookadvisor.domain.FormInfo;
import bookadvisor.bookadvisor.domain.Genero;
import bookadvisor.bookadvisor.domain.Libro;
import bookadvisor.bookadvisor.domain.LibroDTO;
import bookadvisor.bookadvisor.repositories.LibroRepository;
import bookadvisor.bookadvisor.repositories.ValoracionRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;



@Service
public class LibroServiceImplBD implements LibroService {

    // private List<Libro> biblioteca = new ArrayList<>();

    @Autowired
    LibroRepository libroRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ValoracionRepository valoracionRepository;

    @Autowired
    FileStorageService fileStorageService;
    
    @Autowired
    private JavaMailSender mailSender;
    private final Path rootLocation = Paths.get("uploadFiles");




    public void enviarEmail(FormInfo formInfo) {
        MimeMessageHelper helper;
        String destinatario = "ruben.barreiro.7@gmail.com";
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

    public List<Libro> obtenerTodosOrdenadosPorTitulo() {
        List<Libro> libros = libroRepository.findAll();
        Collections.sort(libros, Comparator.comparing(Libro::getTitulo)); // Ordenación ascendente por nombre
        return libros;
    }

    public List<Libro> obtenerTodos() {
        try {
            List<Libro> lista = libroRepository.findAll();
            return lista;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todos los libros", e);
        }
    }

    public Libro obtenerPorId(long id) {
        try {
            for (Libro libro : libroRepository.findAll()) {
                if (libro.getId() == id) {
                    return libro;
                }
            }
            throw new RuntimeException("Libro con ID " + id + " no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el libro con ID " + id, e);
        }    
    }

    public Libro añadir(Libro libro, MultipartFile file) {
    //    try {
    //      libroRepository.save(libro) ;
    //      return libro;
    //    } catch (Exception e) {
    //     throw new RuntimeException("Error al añadir el libro con ID " + libro.getId(), e);
    // }
    if(!file.isEmpty()){
        try{
            String nombreImagen= fileStorageService.store(file);
            libro.setRutaFoto(nombreImagen);
        }catch(Exception e){
            throw new RuntimeException("Error al añadir el libro con ID " + libro.getId(), e);
        }        
    }
    return libroRepository.save(libro);
    }

    public Libro editar(Libro libro, MultipartFile file) {
        // try  {
        //     borrarImagen(libro.getId());
        //     libroRepository.save(libro);
        //     return libro;
        // } catch (Exception e) {
        //     throw new RuntimeException("Error al editar el libro con ID");
        // }

        borrarImagen(libro.getId());
        if(!file.isEmpty()){
            try{
                String nombreImagen= fileStorageService.store(file);
                libro.setRutaFoto(nombreImagen);
            }catch(Exception e){
                throw new RuntimeException("Error al añadir el libro con ID " + libro.getId(), e);
            }        
        }
        return libroRepository.save(libro);
    }

    public boolean borrar(Long id) {
        try {
            Libro libro = obtenerPorId(id);
            if (libro == null) {
                return false;
            }
            borrarImagen(id);
            libroRepository.delete(libro);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al borrar el libro con ID " + id, e);
        }
        // borrarImagen(id);
        // libroRepository.deleteById(id);           
        }


    public void borrarImagen(Long id){
        Libro libro = libroRepository.findById(id).orElse(null);
        if(libro != null){
            fileStorageService.delete(libro.getRutaFoto());
    }
    }

    public List<Libro> buscarPorTitulo(String textoTitulo) {
        try {
            textoTitulo = textoTitulo.toLowerCase();
            List<Libro> encontrados = libroRepository.findByTituloContainingIgnoreCase(textoTitulo);
            return encontrados;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libros por nombre: " + textoTitulo, e);
        }
    }

    public List<Libro> buscarPorGenero(Genero genero) {
        try {
            List<Libro> encontrados = libroRepository.findByGenero(genero);
            return encontrados;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libros por género: " + genero, e);
        }
    }

    // public String store(MultipartFile file, String titulo) throws RuntimeException {
    //     if (file.isEmpty())
    //         throw new RuntimeException("Fichero vacío");
    //     String filename = StringUtils.cleanPath(file.getOriginalFilename());
    //     if (filename.contains("..")) {
    //         throw new RuntimeException("Fichero incorrecto");
    //     }
    //     String extension = StringUtils.getFilenameExtension(filename);
    //     String storedFilename = titulo + "." + extension;

    //     try (InputStream inputStream = file.getInputStream()) {
    //         Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
    //                 StandardCopyOption.REPLACE_EXISTING);
    //         return storedFilename;
    //     } catch (IOException ioe) {
    //         throw new RuntimeException("Error en escritura");
    //     }
    // }

    public void delete(String filename) throws RuntimeException {
        try {
            Path file = rootLocation.resolve(filename);
            if (!Files.exists(file))
                throw new RuntimeException("No existe el fichero");
            Files.delete(file);
        } catch (IOException ioe) {
            throw new RuntimeException("Error en borrado");
        }

    }


     public Resource loadAsResource(String filename) throws RuntimeException {
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


    public void updateLibro(Libro libro) {
        try {
            libroRepository.save(libro);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el libro con ID " + libro.getId(), e);
        }
    }


    //Conversion a DTO entidad Libro

    public List<LibroDTO> convertLibroToDto(List<Libro> listaLibros) {
            try {
                List<LibroDTO> listaDto = new ArrayList<>();
                for (Libro libro : listaLibros) {
                    listaDto.add(modelMapper.map(libro, LibroDTO.class));
                }
                return listaDto;
            } catch (Exception e) {
                throw new RuntimeException("Error al convertir libros a DTO", e);
            }
        }

  

    public Double obtenerMediaValoracion(Long libroId) {
        return valoracionRepository.mediaPorLibro(libroId);
    }

        public long contarVotantes(int libroId) {
            try {
                return libroRepository.countVotacionesByLibroId(libroId);
            } catch (Exception e) {
                throw new RuntimeException("Error al contar votantes para el libro con ID " + libroId, e);
            }
        }
   

        public int sumarPuntos(int libroId) {
            try {
                Integer suma = libroRepository.sumPuntosByLibroId(libroId);
                return (suma != null) ? suma : 0;
            } catch (Exception e) {
                throw new RuntimeException("Error al sumar puntos para el libro con ID " + libroId, e);
            }
        }
    
        
        public double mediaPuntos(int libroId) {
            try {
                Double media = libroRepository.avgPuntosByLibroId(libroId);
                return (media != null) ? media : 0.0;
            } catch (Exception e) {
                throw new RuntimeException("Error al calcular la media de puntos para el libro con ID " + libroId, e);
            
        }
    }

}
