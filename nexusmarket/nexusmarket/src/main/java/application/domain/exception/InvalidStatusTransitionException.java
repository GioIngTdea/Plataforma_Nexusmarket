package application.domain.exception;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(Object currentStatus, Object targetStatus, String reason) {
        super("Cannot transition from " + currentStatus + " to " + targetStatus + ": " + reason);
    }
}
