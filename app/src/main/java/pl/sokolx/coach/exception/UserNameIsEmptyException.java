package pl.sokolx.coach.exception;

public class UserNameIsEmptyException extends Exception {
    public UserNameIsEmptyException() {
    }

    public UserNameIsEmptyException(String message) {
        super(message);
    }
}
