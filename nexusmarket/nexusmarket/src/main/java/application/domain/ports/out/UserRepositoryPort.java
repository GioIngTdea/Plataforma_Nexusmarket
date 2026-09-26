package application.domain.ports.out;

import application.domain.models.Usuario;

import java.util.Optional;

/** Persists and retrieves Usuario accounts and their subtypes. */
public interface UserRepositoryPort {
    Usuario save(Usuario user);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByIdentifier(String identifier);
    Optional<Usuario> findById(String id);
}
