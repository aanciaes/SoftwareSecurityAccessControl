package exceptions;

public class AuthenticationError extends Exception {

    private static final long serialVersionUID = 1L;

    public AuthenticationError() {
        super();
    }

    public AuthenticationError(String message) {
        super(message);
    }
}
