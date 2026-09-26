package application.domain.exception;

public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException(String identifier) {
        super("Identifier is already registered: " + identifier);
    }
}
