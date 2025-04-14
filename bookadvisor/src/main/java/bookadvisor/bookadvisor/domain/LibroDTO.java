package bookadvisor.bookadvisor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class LibroDTO {
    
    private Long id;
    private String titulo;
    private String autor;
    private Idioma idioma;
    private String sinopsis;
    private String rutaFoto;
    
}
