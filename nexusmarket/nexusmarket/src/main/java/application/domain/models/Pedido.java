package application.domain.models;

import application.domain.valueobjects.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The formal commercial commitment between a buyer and the marketplace,
 * originated from a confirmed CarritoCompra. A completed order can never
 * be modified.
 */
@Getter
@Setter
public class Pedido {
    private String id;
    private Comprador buyer;
    private CarritoCompra cart;
    /** Products and quantities frozen at confirmation time, keyed by product id. */
    private Map<Producto, Integer> products = new LinkedHashMap<>();
    private BigDecimal totalPrice;
    private String deliveryAddress;
    private OrderStatus status;
    /** Warehouse id each physical product's stock was reserved from, keyed by product id. */
    private Map<String, String> reservedWarehouseByProduct = new LinkedHashMap<>();

    public boolean isModifiable() {
        return status != OrderStatus.DELIVERED_COMPLETED && status != OrderStatus.CANCELLED;
    }

    public boolean hasPhysicalProducts() {
        return products.keySet().stream().anyMatch(p -> p instanceof ProductoFisico);
    }

    public boolean hasDigitalProducts() {
        return products.keySet().stream().anyMatch(p -> p instanceof ProductoDigital);
    }
}
