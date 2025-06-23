package org.temu.exceptions;

public class UserAlreadyExistsException extends TemuException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
