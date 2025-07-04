package com.ixtiyor.springregistertgbot.handler.command;

import com.ixtiyor.springregistertgbot.model.User;
import com.ixtiyor.springregistertgbot.model.UserState;
import com.ixtiyor.springregistertgbot.service.LocalizationService;
import com.ixtiyor.springregistertgbot.service.UserService;
import com.ixtiyor.springregistertgbot.util.KeyboardFactory;
import com.ixtiyor.springregistertgbot.handler.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {

    private final UserService userService;
    private final LocalizationService localizationService;
    private final KeyboardFactory keyboardFactory;

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && Objects.equals(update.getMessage().getText(),"/start");
    }

    @Override
    public Optional<BotApiMethod<? extends Serializable>> handle(Update update) {
        long chatId = update.getMessage().getChatId();
        User user = userService.findOrCreateUser(chatId);

        SendMessage message;
        if (user.getState() == UserState.REGISTERED) {
            String text = localizationService.getMessage("start.existing_user", user.getLanguageCode(), user.getFirstName());
            message = new SendMessage(String.valueOf(chatId), text);
            message.setReplyMarkup(keyboardFactory.getMainMenuKeyboard(user.getLanguageCode()));
        } else {
            user.setState(UserState.AWAITING_LANGUAGE);
            userService.saveUser(user);
            String text = localizationService.getMessage("start.new_user", user.getLanguageCode());
            message = new SendMessage(String.valueOf(chatId), text);
            message.setReplyMarkup(keyboardFactory.getLanguageSelectionKeyboard());
        }
        return Optional.of(message);
    }
}