package application.domain.exception;

public class ReturnNotFoundException extends RuntimeException {
    public ReturnNotFoundException(String returnId) {
        super("No return request found with id: " + returnId);
    }
}
