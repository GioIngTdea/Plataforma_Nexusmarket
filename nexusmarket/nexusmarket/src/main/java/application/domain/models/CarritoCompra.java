package application.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The provisional product selection made by a buyer before confirming a
 * purchase. Confirming the cart gives rise to a Pedido.
 */
@Getter
@Setter
public class CarritoCompra {
    private String id;
    private Comprador owner;
    /** Selected products and the quantity requested for each, keyed by product id. */
    private Map<Producto, Integer> items = new LinkedHashMap<>();

    public void addProduct(Producto product, int quantity) {
        items.merge(product, quantity, Integer::sum);
    }

    public void removeProduct(Producto product) {
        items.remove(product);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
