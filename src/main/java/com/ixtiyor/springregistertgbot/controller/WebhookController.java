package com.ixtiyor.springregistertgbot.controller;

import com.ixtiyor.springregistertgbot.config.BotConfig;
import com.ixtiyor.springregistertgbot.handler.UpdateDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RequestMapping("/api/v1/bot")
@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final UpdateDispatcher dispatcher;
    private final BotConfig botConfig;

    @PostMapping("/webhook/{token}")
    public BotApiMethod<?> onUpdateReceived(
            @PathVariable("token") String token,
            @RequestBody Update update) {
        if (!botConfig.getToken().equals(token)) {
            return null;
        }
        return dispatcher.dispatch(update).orElse(null);
    }
}