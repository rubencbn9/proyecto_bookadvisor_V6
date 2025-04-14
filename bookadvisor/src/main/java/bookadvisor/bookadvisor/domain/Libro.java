package bookadvisor.bookadvisor.domain;


import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Libro {

   @Id 
   @GeneratedValue (strategy=GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private LocalDate año;
    private String autor;
    private Idioma idioma;
    private String sinopsis;
    private LocalDate fechaDeAlta;
    private String rutaFoto;

     @ManyToOne
    @JoinColumn(name = "genero_id")
    private Genero genero;

}
