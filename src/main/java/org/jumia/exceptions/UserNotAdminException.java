package org.jumia.exceptions;

public class UserNotAdminException extends JumiaException {
    public UserNotAdminException(String message) {
        super(message);
    }
}
