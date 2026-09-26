package application.domain.ports.out;

import application.domain.models.Envio;

/** Persists shipments. */
public interface ShipmentRepositoryPort {
    Envio save(Envio shipment);
}
