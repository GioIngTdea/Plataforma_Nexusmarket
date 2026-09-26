package application.domain.models;

import lombok.Getter;
import lombok.Setter;

/**
 * A product delivered immediately upon payment confirmation, without
 * requiring physical inventory.
 */
@Getter
@Setter
public class ProductoDigital extends Producto {
    /** How the product is handed over to the buyer (e.g. a download link or a code). */
    private String deliveryConfig;
}
