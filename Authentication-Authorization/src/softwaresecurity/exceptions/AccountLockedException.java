package softwaresecurity.exceptions;

public class AccountLockedException extends Exception {

    private static final long serialVersionUID = 1L;

    public AccountLockedException() {
        super();
    }

    public AccountLockedException(String message) {
        super(message);
    }
}
