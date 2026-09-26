package application.domain.exception;

public class NegativeStockException extends RuntimeException {
    public NegativeStockException(String productId, String warehouseId, int available, int requested) {
        super("Cannot remove " + requested + " units of product " + productId + " from warehouse "
                + warehouseId + ": only " + available + " available");
    }
}
