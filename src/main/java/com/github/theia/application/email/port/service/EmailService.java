package com.github.theia.application.email.port.service;

import com.github.theia.application.email.port.in.SendEmailUseCase;
import com.github.theia.application.email.port.in.VerifyEmailUseCase;
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
import java.util.Objects;
import java.util.Random;

import static com.github.theia.global.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EmailService implements SendEmailUseCase, VerifyEmailUseCase {

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
                            .attemptCount(0)
                            .expiredAt(authCodeExp)
                            .build()
                );

        if (emailAuthRedisEntity.getAttemptCount() >= 5)
            throw new TheiaException(MANY_EMAIL);

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
    public void verifyEmail(String email, String code) {
        EmailAuthRedisEntity emailAuth = loadEmailAuthByEmailPort.findByEmail(email)
                .orElseThrow(() -> new TheiaException(NOT_FOUND_EMAIL));

        if (!Objects.equals(emailAuth.getCode(), code)) throw new TheiaException(NOT_VERIFY_CODE);

        emailAuth.updateAuthentication(true);

        saveEmailPort.save(emailAuth);
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new TheiaException(ERROR_CODE);
        }
    }
}
