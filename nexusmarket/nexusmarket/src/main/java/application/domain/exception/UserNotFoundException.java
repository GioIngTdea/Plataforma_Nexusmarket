package application.domain.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("No user found with id: " + userId);
    }
}
