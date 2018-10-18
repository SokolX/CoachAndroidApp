package pl.sokolx.coach.exception;

import pl.sokolx.coach.R;

public class UserEmailIsEmptyException extends Exception {
    public UserEmailIsEmptyException() {
    }

    public UserEmailIsEmptyException(String message) {
        super(message);
    }
}
