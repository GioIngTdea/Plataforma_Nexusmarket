package application.domain.service.inventory;

import application.domain.exception.InvalidOrderTransitionException;
import application.domain.exception.OrderNotFoundException;
import application.domain.models.Envio;
import application.domain.models.OperadorLogistico;
import application.domain.models.Pedido;
import application.domain.models.Producto;
import application.domain.ports.in.DispatchOrderPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.ShipmentRepositoryPort;
import application.domain.valueobjects.InventoryMovementType;
import application.domain.valueobjects.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DispatchOrderService implements DispatchOrderPort {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final ShipmentRepositoryPort shipmentRepositoryPort;

    @Override
    public Envio execute(String orderId, String logisticsOperatorId) {
        Pedido order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        validateOrderIsPaid(order);
        deductReservedInventory(order);

        order.setStatus(OrderStatus.DISPATCHED);
        orderRepositoryPort.save(order);

        OperadorLogistico operator = new OperadorLogistico();
        operator.setId(logisticsOperatorId);

        Envio shipment = new Envio();
        shipment.setOrder(order);
        shipment.setHandledBy(operator);

        return shipmentRepositoryPort.save(shipment);
    }

    private void validateOrderIsPaid(Pedido order) {
        if (order.getStatus() != OrderStatus.PAID) {
            throw new InvalidOrderTransitionException(order.getStatus(), OrderStatus.DISPATCHED);
        }
    }

    private void deductReservedInventory(Pedido order) {
        for (Map.Entry<Producto, Integer> item : order.getProducts().entrySet()) {
            Producto product = item.getKey();
            String warehouseId = order.getReservedWarehouseByProduct().get(product.getId());
            if (warehouseId == null) {
                continue;
            }
            inventoryRepositoryPort.findByProductAndWarehouse(product.getId(), warehouseId)
                    .ifPresent(inventory -> {
                        inventory.decreaseStock(item.getValue(), InventoryMovementType.SALE_EXIT);
                        inventoryRepositoryPort.save(inventory);
                    });
        }
    }
}
