package com.back.banka.Services.IServices;

import java.util.Map;

public interface IEmailService {

        void sendEmail(String email, String subject, String body);
    }