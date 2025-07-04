package org.jumia.exceptions;

public class TokenExpiredException extends JumiaException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
