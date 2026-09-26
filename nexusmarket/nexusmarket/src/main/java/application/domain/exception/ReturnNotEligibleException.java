package application.domain.exception;

public class ReturnNotEligibleException extends RuntimeException {
    public ReturnNotEligibleException(String orderId) {
        super("Order " + orderId + " is not eligible for a return: it must be delivered and completed first");
    }
}
