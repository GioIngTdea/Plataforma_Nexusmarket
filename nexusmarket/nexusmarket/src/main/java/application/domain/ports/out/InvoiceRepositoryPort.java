package application.domain.ports.out;

import application.domain.models.Factura;

/** Persists invoices. */
public interface InvoiceRepositoryPort {
    Factura save(Factura invoice);
}
