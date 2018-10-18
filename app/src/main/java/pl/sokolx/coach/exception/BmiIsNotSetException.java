package pl.sokolx.coach.exception;

public class BmiIsNotSetException extends Exception {
    public BmiIsNotSetException() {
    }

    public BmiIsNotSetException(String message) {
        super(message);
    }
}
