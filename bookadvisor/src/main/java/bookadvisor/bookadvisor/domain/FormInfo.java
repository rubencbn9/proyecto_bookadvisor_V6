package bookadvisor.bookadvisor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormInfo {
    private String nombre;
    private String email;
    
    private  Asunto asunto;
    private boolean aceptoCondiciones;
    private String texto;
}
