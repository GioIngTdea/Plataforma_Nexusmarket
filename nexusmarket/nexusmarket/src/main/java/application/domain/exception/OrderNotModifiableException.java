package application.domain.exception;

public class OrderNotModifiableException extends RuntimeException {
    public OrderNotModifiableException(String orderId) {
        super("Order " + orderId + " is completed or cancelled and can no longer be modified");
    }
}
