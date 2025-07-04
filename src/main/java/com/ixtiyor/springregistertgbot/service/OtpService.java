package com.ixtiyor.springregistertgbot.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    private final Map<Long, String> otpCache = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public String generateAndStoreOtp(Long chatId) {
        String otp = String.format("%04d", random.nextInt(10000));
        otpCache.put(chatId, otp);
        return otp;
    }

    public boolean verifyOtp(Long chatId, String providedOtp) {
        String storedOtp = otpCache.get(chatId);
        if (storedOtp != null && storedOtp.equals(providedOtp)) {
            otpCache.remove(chatId);
            return true;
        }
        return false;
    }
}
