package pl.sokolx.coach.exception;

public class UserDateOfBirthIsNotSetException extends Exception {
    public UserDateOfBirthIsNotSetException() {
    }

    public UserDateOfBirthIsNotSetException(String message) {
        super(message);
    }
}
