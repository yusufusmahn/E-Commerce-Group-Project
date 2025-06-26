package org.temu.exceptions;

public class UserNotAdminException extends TemuException {
    public UserNotAdminException(String message) {
        super(message);
    }
}
