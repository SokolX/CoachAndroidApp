package pl.sokolx.coach.exception;

public class UserPasswordIsEmptyException extends Exception {
    public UserPasswordIsEmptyException() {
    }

    public UserPasswordIsEmptyException(String message) {
        super(message);
    }
}
