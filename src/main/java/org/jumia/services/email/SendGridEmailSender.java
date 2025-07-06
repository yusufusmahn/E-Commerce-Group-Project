package org.jumia.services.email;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridEmailSender implements EmailSender {

    @Value("${sendgrid.api.key}")
    private String apiKey;

    @Value("${sendgrid.sender.email}")
    private String senderEmail;

    @Override
    public void send(String to, String subject, String content, boolean isHtml) {
        Email from = new Email(senderEmail);
        Email toEmail = new Email(to);
        Content emailContent = new Content(isHtml ? "text/html" : "text/plain", content);
        Mail mail = new Mail(from, subject, toEmail, emailContent);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("Email sent! Status: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());


            System.out.println("Email sent! Status Code: " + response.getStatusCode());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to send email: " + ex.getMessage(), ex);
        }
    }
}
