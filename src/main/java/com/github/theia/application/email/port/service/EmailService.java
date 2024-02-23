package com.github.theia.application.email.port.service;

import com.github.theia.application.email.port.in.SendEmailUseCase;
import com.github.theia.application.email.port.out.LoadEmailAuthByEmailPort;
import com.github.theia.application.email.port.out.SaveEmailPort;
import com.github.theia.domain.email.EmailAuthRedisEntity;
import com.github.theia.global.error.exception.TheiaException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import static com.github.theia.global.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EmailService implements SendEmailUseCase {

    @Value("${spring.auth-code-expiration-millis}")
    private Long authCodeExp;

    private final SaveEmailPort saveEmailPort;
    private final LoadEmailAuthByEmailPort loadEmailAuthByEmailPort;
    private final JavaMailSender emailSender;

    @Override
    @Transactional
    public void sendEmail(String toEmail, String title, String text) {

        String code = createCode();

        EmailAuthRedisEntity emailAuthRedisEntity = loadEmailAuthByEmailPort.findByEmail(toEmail)
                .orElse(
                        EmailAuthRedisEntity.builder()
                            .email(toEmail)
                            .code(code)
                            .authentication(false)
                            .expiredAt(authCodeExp)
                            .build()
                );

        emailAuthRedisEntity.updateCode(code);

        SimpleMailMessage message = createEmailForm(toEmail, title, code);

        try {
            saveEmailPort.save(emailAuthRedisEntity);
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

    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new TheiaException(ERROR_CODE);
        }
    }
}
