package com.ixtiyor.springregistertgbot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotInitializer {

    private final BotConfig botConfig;

    @Value("${app.domain}")
    private String appDomain;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {

            TelegramBot bot = new TelegramBot(botConfig);

            SetWebhook setWebhook = SetWebhook.builder()
                    .url(appDomain + bot.getBotPath() + botConfig.getToken())
                    .build();

            bot.execute(setWebhook);

            log.info("Webhook set successfully to: {}", setWebhook.getUrl());
        } catch (TelegramApiException e) {
            log.error("Error setting webhook", e);
        }
    }
}