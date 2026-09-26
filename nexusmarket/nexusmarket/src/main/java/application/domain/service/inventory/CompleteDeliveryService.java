package application.domain.service.inventory;

import application.domain.exception.InvalidOrderTransitionException;
import application.domain.exception.OrderNotFoundException;
import application.domain.models.Pedido;
import application.domain.ports.in.CompleteDeliveryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.valueobjects.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompleteDeliveryService implements CompleteDeliveryPort {

    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    public void execute(String orderId) {
        Pedido order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getStatus() != OrderStatus.DISPATCHED) {
            throw new InvalidOrderTransitionException(order.getStatus(), OrderStatus.DELIVERED_COMPLETED);
        }

        order.setStatus(OrderStatus.DELIVERED_COMPLETED);
        orderRepositoryPort.save(order);
    }
}
