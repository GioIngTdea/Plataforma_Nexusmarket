package application.domain.exception;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException(String userId, String reason) {
        super("User " + userId + " is not authorized to perform this action: " + reason);
    }
}
