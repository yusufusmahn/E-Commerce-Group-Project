package org.jumia.services.email;

public interface EmailSender {
    void send(String to, String subject, String content, boolean isHtml);
}
