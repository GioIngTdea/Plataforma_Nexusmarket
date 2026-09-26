package application.domain.exception;

public class RefundAlreadyProcessedException extends RuntimeException {
    public RefundAlreadyProcessedException(String refundId) {
        super("Refund " + refundId + " was already processed");
    }
}
