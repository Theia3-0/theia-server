package com.github.theia.application.email.port.service;

import com.github.theia.application.email.port.in.SendEmailUseCase;
import com.github.theia.global.error.exception.ErrorCode;
import com.github.theia.global.error.exception.TheiaException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.github.theia.global.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EmailService implements SendEmailUseCase {

    private final JavaMailSender emailSender;

    @Override
    @Transactional
    public void sendEmail(String toEmail, String title, String text) {
        SimpleMailMessage message = createEmailForm(toEmail, title, text);

        try {
            emailSender.send(message);
        } catch (Exception e) {
            throw new TheiaException(ERROR_EMAIL);
        }
    }

    @Override
    public SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}
