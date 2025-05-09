package uts.Citas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    String id;

    String username;
    String contrasena;
    Rol rol;

    public void setContrasena(String contrasena) {
        this.contrasena = new BCryptPasswordEncoder().encode(contrasena);
    }
}
