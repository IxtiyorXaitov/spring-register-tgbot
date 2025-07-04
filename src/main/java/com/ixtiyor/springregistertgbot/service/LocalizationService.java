package com.ixtiyor.springregistertgbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LocalizationService {
    private final MessageSource messageSource;

    public String getMessage(String key, String languageCode, Object... args) {
        Locale locale = languageCode != null ? Locale.forLanguageTag(languageCode) : Locale.ENGLISH;
        return messageSource.getMessage(key, args, locale);
    }
}
