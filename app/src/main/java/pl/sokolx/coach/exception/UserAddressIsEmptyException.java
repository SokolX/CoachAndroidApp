package pl.sokolx.coach.exception;

public class UserAddressIsEmptyException extends Exception {
    public UserAddressIsEmptyException() {
    }

    public UserAddressIsEmptyException(String message) {
        super(message);
    }
}
