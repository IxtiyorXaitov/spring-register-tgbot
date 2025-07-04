package com.ixtiyor.springregistertgbot.handler.message;

import com.ixtiyor.springregistertgbot.handler.UpdateHandler;
import com.ixtiyor.springregistertgbot.model.User;
import com.ixtiyor.springregistertgbot.model.UserState;
import com.ixtiyor.springregistertgbot.service.LocalizationService;
import com.ixtiyor.springregistertgbot.service.OtpService;
import com.ixtiyor.springregistertgbot.service.UserService;
import com.ixtiyor.springregistertgbot.util.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TextMessageHandler implements UpdateHandler {

    private final UserService userService;
    private final OtpService otpService;
    private final LocalizationService localizationService;
    private final KeyboardFactory keyboardFactory;

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public Optional<BotApiMethod<? extends Serializable>> handle(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        User user = userService.findOrCreateUser(chatId);

        return switch (user.getState()) {
            case AWAITING_OTP -> handleOtpState(user, text);
            case AWAITING_FIRST_NAME -> handleFirstNameState(user, text);
            case AWAITING_LAST_NAME -> handleLastNameState(user, text);
            case REGISTERED -> handleRegisteredState(user, text);
            default -> Optional.empty();
        };
    }

    private Optional<BotApiMethod<? extends Serializable>> handleOtpState(User user, String text) {
        if (otpService.verifyOtp(user.getChatId(), text)) {
            user.setState(UserState.AWAITING_FIRST_NAME);
            userService.saveUser(user);
            String responseText = localizationService.getMessage("request.firstname", user.getLanguageCode());
            return Optional.of(new SendMessage(String.valueOf(user.getChatId()), responseText));
        } else {
            String responseText = localizationService.getMessage("otp.invalid", user.getLanguageCode());
            return Optional.of(new SendMessage(String.valueOf(user.getChatId()), responseText));
        }
    }

    private Optional<BotApiMethod<? extends Serializable>> handleFirstNameState(User user, String text) {
        user.setFirstName(text);
        user.setState(UserState.AWAITING_LAST_NAME);
        userService.saveUser(user);
        String responseText = localizationService.getMessage("request.lastname", user.getLanguageCode());
        return Optional.of(new SendMessage(String.valueOf(user.getChatId()), responseText));
    }

    private Optional<BotApiMethod<? extends Serializable>> handleLastNameState(User user, String text) {
        user.setLastName(text);
        user.setState(UserState.REGISTERED);
        userService.saveUser(user);
        String responseText = localizationService.getMessage("register.complete", user.getLanguageCode(), user.getFirstName());
        SendMessage message = new SendMessage(String.valueOf(user.getChatId()), responseText);
        message.setReplyMarkup(keyboardFactory.getMainMenuKeyboard(user.getLanguageCode()));
        return Optional.of(message);
    }

    private Optional<BotApiMethod<? extends Serializable>> handleRegisteredState(User user, String text) {
        String myProfileText = localizationService.getMessage("menu.my_profile", user.getLanguageCode());
        if (Objects.equals(text,myProfileText)) {
            String profileInfo = String.format("Name: %s %s\nPhone: %s", user.getFirstName(), user.getLastName(), user.getPhoneNumber());
            return Optional.of(new SendMessage(String.valueOf(user.getChatId()), profileInfo));
        }

        return Optional.empty();
    }
}