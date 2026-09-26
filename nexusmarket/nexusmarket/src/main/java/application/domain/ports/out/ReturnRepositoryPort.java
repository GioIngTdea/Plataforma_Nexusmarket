package application.domain.ports.out;

import application.domain.models.Devolucion;

import java.util.Optional;

/** Persists and retrieves return requests. */
public interface ReturnRepositoryPort {
    Devolucion save(Devolucion returnRequest);
    Optional<Devolucion> findById(String id);
}
