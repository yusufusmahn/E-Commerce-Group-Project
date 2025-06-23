package org.temu.exceptions;

public class InvalidPasswordException extends TemuException{
    public InvalidPasswordException(String message) {
        super(message);
    }
}
