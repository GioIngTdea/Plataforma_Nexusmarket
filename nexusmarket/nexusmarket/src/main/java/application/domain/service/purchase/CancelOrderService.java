package application.domain.service.purchase;

import application.domain.exception.InvalidOrderTransitionException;
import application.domain.exception.OrderNotFoundException;
import application.domain.exception.UnauthorizedUserException;
import application.domain.models.Administrador;
import application.domain.models.Pedido;
import application.domain.ports.in.CancelOrderPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueobjects.InventoryMovementType;
import application.domain.valueobjects.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelOrderService implements CancelOrderPort {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void execute(String orderId, String requesterId) {
        Pedido order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        validateRequester(order, requesterId);
        validatePendingPayment(order);

        releaseReservedInventory(order);

        order.setStatus(OrderStatus.CANCELLED);
        orderRepositoryPort.save(order);
    }

    private void validateRequester(Pedido order, String requesterId) {
        boolean isBuyer = order.getBuyer().getId().equals(requesterId);
        boolean isAdmin = userRepositoryPort.findById(requesterId)
                .map(Administrador.class::isInstance)
                .orElse(false);

        if (!isBuyer && !isAdmin) {
            throw new UnauthorizedUserException(requesterId, "Only the buyer or an administrator can cancel this order.");
        }
    }

    private void validatePendingPayment(Pedido order) {
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new InvalidOrderTransitionException(order.getStatus(), OrderStatus.CANCELLED);
        }
    }

    private void releaseReservedInventory(Pedido order) {
        order.getReservedWarehouseByProduct().forEach((productId, warehouseId) -> {
            int quantity = order.getProducts().entrySet().stream()
                    .filter(entry -> entry.getKey().getId().equals(productId))
                    .map(java.util.Map.Entry::getValue)
                    .findFirst().orElse(0);

            inventoryRepositoryPort.findByProductAndWarehouse(productId, warehouseId)
                    .ifPresent(inventory -> {
                        inventory.increaseStock(quantity, InventoryMovementType.ENTRY);
                        inventoryRepositoryPort.save(inventory);
                    });
        });
    }
}
