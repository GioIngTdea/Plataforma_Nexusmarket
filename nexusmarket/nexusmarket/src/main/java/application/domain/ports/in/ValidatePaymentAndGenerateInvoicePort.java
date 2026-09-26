package application.domain.ports.in;

import application.domain.models.Factura;

public interface ValidatePaymentAndGenerateInvoicePort {
    Factura execute(String orderId);
}
