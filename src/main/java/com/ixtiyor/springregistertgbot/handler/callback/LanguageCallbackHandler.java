package com.ixtiyor.springregistertgbot.handler.callback;

import com.ixtiyor.springregistertgbot.handler.UpdateHandler;
import com.ixtiyor.springregistertgbot.model.User;
import com.ixtiyor.springregistertgbot.model.UserState;
import com.ixtiyor.springregistertgbot.service.LocalizationService;
import com.ixtiyor.springregistertgbot.service.UserService;
import com.ixtiyor.springregistertgbot.util.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LanguageCallbackHandler implements UpdateHandler {

    private final UserService userService;
    private final LocalizationService localizationService;
    private final KeyboardFactory keyboardFactory;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("lang_");
    }

    @Override
    public Optional<BotApiMethod<? extends Serializable>> handle(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String langCode = update.getCallbackQuery().getData().substring(5);

        User user = userService.findOrCreateUser(chatId);
        user.setLanguageCode(langCode);
        user.setState(UserState.AWAITING_PHONE_NUMBER);
        userService.saveUser(user);

        String text = localizationService.getMessage("request.phone", langCode);
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        message.setReplyMarkup(keyboardFactory.getRequestContactKeyboard(langCode));

        return Optional.of(message);
    }
}