package pl.sokolx.coach.exception;

public class UserPasswordShortLengthException extends Exception {
    public UserPasswordShortLengthException() {
    }

    public UserPasswordShortLengthException(String message) {
        super(message);
    }
}
