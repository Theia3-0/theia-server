package com.github.theia.application.email.port.service;

import com.github.theia.application.email.port.in.SendEmailUseCase;
import com.github.theia.application.email.port.in.VerifyEmailUseCase;
import com.github.theia.application.email.port.out.LoadEmailAuthByEmailPort;
import com.github.theia.application.email.port.out.SaveEmailPort;
import com.github.theia.domain.email.EmailAuthRedisEntity;
import com.github.theia.global.error.exception.TheiaException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

import static com.github.theia.global.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EmailService implements SendEmailUseCase, VerifyEmailUseCase {

    @Value("${spring.codeExp}")
    private Long authCodeExp;

    private final SaveEmailPort saveEmailPort;
    private final LoadEmailAuthByEmailPort loadEmailAuthByEmailPort;
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Transactional
    public void sendEmail(String email, String title) {
        String code = createCode();

        EmailAuthRedisEntity emailAuthRedisEntity = loadEmailAuthByEmailPort.findByEmail(email)
                .orElse(
                        EmailAuthRedisEntity.builder()
                            .email(email)
                            .code(code)
                            .authentication(false)
                            .attemptCount(0)
                            .expiredAt(authCodeExp)
                            .build()
                );

        if (emailAuthRedisEntity.getAttemptCount() >= 5)
            throw new TheiaException(MANY_EMAIL);

        emailAuthRedisEntity.updateCode(code);

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject(title);
            helper.setText(createMailTemplate(code), true);

            saveEmailPort.save(emailAuthRedisEntity);
            emailSender.send(message);
        } catch (Exception e) {
            throw new TheiaException(ERROR_EMAIL);
        }
    }

    @Override
    @Transactional
    public void verifyEmail(String email, String code) {
        EmailAuthRedisEntity emailAuth = loadEmailAuthByEmailPort.findByEmail(email)
                .orElseThrow(() -> new TheiaException(NOT_FOUND_EMAIL));

        if (!Objects.equals(emailAuth.getCode(), code)) throw new TheiaException(NOT_VERIFY_CODE);

        emailAuth.updateAuthentication(true);

        saveEmailPort.save(emailAuth);
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

    private String createMailTemplate(String code) {
        Context context = new Context();
        context.setVariable("code", code);

        return templateEngine.process("mail-template", context);
    }
}
