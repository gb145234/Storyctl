package fscut.manager.demo.exception;

public class CustomerNoAuthorityException extends RuntimeException {
    public CustomerNoAuthorityException(String message) {
        super(message);
    }
}
