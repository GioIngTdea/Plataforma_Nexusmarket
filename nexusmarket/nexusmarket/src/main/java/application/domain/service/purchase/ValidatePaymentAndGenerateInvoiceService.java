package application.domain.service.purchase;

import application.domain.exception.InvalidOrderTransitionException;
import application.domain.exception.OrderNotFoundException;
import application.domain.models.Factura;
import application.domain.models.Pedido;
import application.domain.ports.in.ValidatePaymentAndGenerateInvoicePort;
import application.domain.ports.out.InvoiceRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.valueobjects.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidatePaymentAndGenerateInvoiceService implements ValidatePaymentAndGenerateInvoicePort {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InvoiceRepositoryPort invoiceRepositoryPort;

    @Override
    public Factura execute(String orderId) {
        Pedido order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        validatePendingPayment(order);

        order.setStatus(OrderStatus.PAID);
        orderRepositoryPort.save(order);

        Factura invoice = new Factura();
        invoice.setOrder(order);
        invoice.setTotalAmount(order.getTotalPrice());

        return invoiceRepositoryPort.save(invoice);
    }

    private void validatePendingPayment(Pedido order) {
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new InvalidOrderTransitionException(order.getStatus(), OrderStatus.PAID);
        }
    }
}
