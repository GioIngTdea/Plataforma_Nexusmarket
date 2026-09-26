package application.domain.exception;

public class InvalidOrderTransitionException extends RuntimeException {
    public InvalidOrderTransitionException(Object currentStatus, Object targetStatus) {
        super("Order cannot move from " + currentStatus + " to " + targetStatus);
    }
}
