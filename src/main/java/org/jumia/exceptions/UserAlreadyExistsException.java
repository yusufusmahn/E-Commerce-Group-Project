package org.jumia.exceptions;

public class UserAlreadyExistsException extends JumiaException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
