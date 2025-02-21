package com.back.banka.Services.IServices;

public interface IEmailService {
    void sendEmail(String email, String subject, String body);
}