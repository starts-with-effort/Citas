package uts.Citas.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

import uts.Citas.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String>{

    Optional<Usuario> findByUsername(String username);
}
