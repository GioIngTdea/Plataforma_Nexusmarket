package application.domain.exception;

public class RefundNotFoundException extends RuntimeException {
    public RefundNotFoundException(String refundId) {
        super("No refund found with id: " + refundId);
    }
}
