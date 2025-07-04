package com.ixtiyor.springregistertgbot.handler.message;

import com.ixtiyor.springregistertgbot.handler.UpdateHandler;
import com.ixtiyor.springregistertgbot.model.User;
import com.ixtiyor.springregistertgbot.model.UserState;
import com.ixtiyor.springregistertgbot.service.LocalizationService;
import com.ixtiyor.springregistertgbot.service.OtpService;
import com.ixtiyor.springregistertgbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.io.Serializable;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ContactMessageHandler implements UpdateHandler {

    private final UserService userService;
    private final OtpService otpService;
    private final LocalizationService localizationService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasContact()) {
            return false;
        }
        User user = userService.findOrCreateUser(update.getMessage().getChatId());
        return user.getState() == UserState.AWAITING_PHONE_NUMBER;
    }

    @Override
    public Optional<BotApiMethod<? extends Serializable>> handle(Update update) {
        long chatId = update.getMessage().getChatId();
        User user = userService.findOrCreateUser(chatId);

        user.setPhoneNumber(update.getMessage().getContact().getPhoneNumber());
        user.setState(UserState.AWAITING_OTP);
        userService.saveUser(user);

        String otp = otpService.generateAndStoreOtp(chatId);

        String text = localizationService.getMessage("request.otp", user.getLanguageCode())
                + "\n\nDEMO: Your OTP is " + otp;

        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        message.setReplyMarkup(new ReplyKeyboardRemove(true)); // Remove the contact button
        return Optional.of(message);
    }
}
