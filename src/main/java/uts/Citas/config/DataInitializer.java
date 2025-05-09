package uts.Citas.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uts.Citas.model.Rol;
import uts.Citas.model.Usuario;
import uts.Citas.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository) {
        return args -> {
            // Crear usuario administrador si no existe
            String adminUsername = "administrador";
            if (usuarioRepository.findByUsername(adminUsername).isEmpty()) {
                Rol adminRol = Rol.ADMINISTRADOR;

                Usuario admin = new Usuario();
                admin.setUsername(adminUsername);
                admin.setContrasena("ADMIN000"); 
                admin.setRol(adminRol);

                usuarioRepository.save(admin);
                System.out.println("Usuario administrador creado con éxito.");
            } else {
                System.out.println("El usuario administrador ya existe.");
            }
        };
    }
}
