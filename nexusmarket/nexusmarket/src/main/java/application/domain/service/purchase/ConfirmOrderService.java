package application.domain.service.purchase;

import application.domain.exception.CartNotFoundException;
import application.domain.exception.EmptyCartException;
import application.domain.exception.InsufficientInventoryException;
import application.domain.models.CarritoCompra;
import application.domain.models.Inventario;
import application.domain.models.Pedido;
import application.domain.models.Producto;
import application.domain.ports.in.ConfirmOrderPort;
import application.domain.ports.in.ReserveInventoryPort;
import application.domain.ports.out.CartRepositoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.valueobjects.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConfirmOrderService implements ConfirmOrderPort {

    private final CartRepositoryPort cartRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final ReserveInventoryPort reserveInventoryPort;

    @Override
    public Pedido execute(String buyerId, String deliveryAddress) {
        CarritoCompra cart = cartRepositoryPort.findByBuyerId(buyerId)
                .orElseThrow(() -> new CartNotFoundException(buyerId));

        validateNotEmpty(cart);
        validateAvailableStock(cart);

        Pedido order = new Pedido();
        order.setBuyer(cart.getOwner());
        order.setCart(cart);
        order.setProducts(cart.getItems());
        order.setDeliveryAddress(deliveryAddress);
        order.setTotalPrice(calculateTotal(cart));
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        reservePhysicalStock(cart, order);

        Pedido persistedOrder = orderRepositoryPort.save(order);

        cart.clear();
        cartRepositoryPort.save(cart);

        return persistedOrder;
    }

    private void validateNotEmpty(CarritoCompra cart) {
        if (cart.isEmpty()) {
            throw new EmptyCartException(cart.getId());
        }
    }

    private void validateAvailableStock(CarritoCompra cart) {
        for (Map.Entry<Producto, Integer> item : cart.getItems().entrySet()) {
            Producto product = item.getKey();
            if (!isPhysical(product)) {
                continue;
            }
            int totalAvailable = inventoryRepositoryPort.findByProduct(product.getId()).stream()
                    .mapToInt(Inventario::getStock)
                    .sum();
            if (totalAvailable < item.getValue()) {
                throw new InsufficientInventoryException(product.getId(), item.getValue(), totalAvailable);
            }
        }
    }

    private void reservePhysicalStock(CarritoCompra cart, Pedido order) {
        for (Map.Entry<Producto, Integer> item : cart.getItems().entrySet()) {
            Producto product = item.getKey();
            if (!isPhysical(product)) {
                continue;
            }
            Inventario warehouseWithStock = inventoryRepositoryPort.findByProduct(product.getId()).stream()
                    .filter(inv -> inv.getStock() >= item.getValue())
                    .findFirst()
                    .orElseThrow(() -> new InsufficientInventoryException(product.getId(), item.getValue(), 0));

            reserveInventoryPort.execute(product.getId(), warehouseWithStock.getWarehouse().getId(), item.getValue());
            order.getReservedWarehouseByProduct().put(product.getId(), warehouseWithStock.getWarehouse().getId());
        }
    }

    private BigDecimal calculateTotal(CarritoCompra cart) {
        return cart.getItems().entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isPhysical(Producto product) {
        return product.getClass().getSimpleName().equals("ProductoFisico");
    }
}
