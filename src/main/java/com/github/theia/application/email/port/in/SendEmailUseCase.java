package com.github.theia.application.email.port.in;

import org.springframework.mail.SimpleMailMessage;

public interface SendEmailUseCase {
    void sendEmail(String toEmail, String title, String text);

    SimpleMailMessage createEmailForm(String toEmail, String title, String text);
}
