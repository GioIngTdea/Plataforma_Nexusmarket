package application.domain.ports.out;

import application.domain.models.Reembolso;

import java.util.Optional;

/** Persists and retrieves refunds. */
public interface RefundRepositoryPort {
    Reembolso save(Reembolso refund);
    Optional<Reembolso> findByReturnId(String returnId);
}
