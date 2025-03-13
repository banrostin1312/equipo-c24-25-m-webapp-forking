package com.back.banka.Services.IServices;

import java.util.Map;

public interface IEmailService {
    void sendEmail(String to, String subject, String content);
    void sendEmailTemplate(String to, String subject, String templatename, Map<String,Object> variables );
}