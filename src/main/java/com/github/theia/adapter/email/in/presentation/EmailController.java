package com.github.theia.adapter.email.in.presentation;

import com.github.theia.adapter.email.in.presentation.dto.request.EmailSendRequest;
import com.github.theia.adapter.email.in.presentation.dto.request.EmailVerificationRequest;
import com.github.theia.application.email.port.in.VerifyEmailUseCase;
import com.github.theia.application.email.port.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;
    private final VerifyEmailUseCase verifyEmailUseCase;

    @PostMapping
    public ResponseEntity<Void> sendEmail(@RequestBody EmailSendRequest emailSendRequest) {
        emailService.sendEmail(emailSendRequest.getEmail(), "title", "text");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyEmail(@RequestBody EmailVerificationRequest emailVerificationRequest) {
        String email = emailVerificationRequest.getEmail();
        String code = emailVerificationRequest.getCode();

        verifyEmailUseCase.verifyEmail(email, code);

        return ResponseEntity.ok().build();
    }
}
