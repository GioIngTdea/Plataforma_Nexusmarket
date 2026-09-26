package application.domain.models;

import application.domain.valueobjects.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Any good offered in the marketplace catalog. The catalog distinguishes
 * physical products, which require inventory and dispatch, from digital
 * products, delivered immediately after payment.
 * This class cannot be instantiated directly.
 */
@Getter
@Setter
public abstract class Producto {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private List<String> variants = new ArrayList<>();
    private ProductStatus status;
    private Vendedor publishedBy;

    public void publish() {
        this.status = ProductStatus.PUBLISHED;
    }

    public void suspend() {
        this.status = ProductStatus.SUSPENDED;
    }

    public void discontinue() {
        this.status = ProductStatus.DISCONTINUED;
    }

    public boolean isAvailableForPurchase() {
        return ProductStatus.PUBLISHED.equals(status);
    }
}
