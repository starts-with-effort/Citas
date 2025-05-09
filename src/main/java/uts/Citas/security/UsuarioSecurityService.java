package uts.Citas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import uts.Citas.model.Usuario;
import uts.Citas.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioSecurityService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos por su nombre de usuario
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado con username: " + username);
        }

        Usuario usuario = usuarioOpt.get();

        // Prepara la lista de autoridades (roles) para Spring Security
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (usuario.getRol() != null && usuario.getRol() != null) {
            // Importante: Spring Security usualmente espera los roles con el prefijo "ROLE_"
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));
        } else {
            // Opcional: Puedes asignar un rol por defecto o lanzar una excepción si un usuario DEBE tener rol
             System.err.println("Advertencia: Usuario " + username + " no tiene un rol asignado.");
            // authorities.add(new SimpleGrantedAuthority("ROLE_DEFAULT")); // Ejemplo
        }

        // Retorna un objeto UserDetails que Spring Security entiende.
        // Necesita el username, la contraseña (¡hasheada desde la DB!) y las autoridades.
        return new User(usuario.getUsername(), usuario.getContrasena(), authorities);
    }
}
