package com.ixtiyor.springregistertgbot.util;

import com.ixtiyor.springregistertgbot.service.LocalizationService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class KeyboardFactory {

    private final LocalizationService localizationService;

    public KeyboardFactory(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    public InlineKeyboardMarkup getLanguageSelectionKeyboard() {
        InlineKeyboardButton eng = new InlineKeyboardButton("English \uD83C\uDDEC\uD83C\uDDE7");
        eng.setCallbackData("lang_en");

        InlineKeyboardButton rus = new InlineKeyboardButton("Русский \uD83C\uDDF7\uD83C\uDDFA");
        rus.setCallbackData("lang_ru");

        InlineKeyboardButton uzb = new InlineKeyboardButton("O'zbekcha \uD83C\uDDFA\uD83C\uDDFF");
        uzb.setCallbackData("lang_uz");

        return new InlineKeyboardMarkup(List.of(List.of(eng, rus, uzb)));
    }

    public ReplyKeyboardMarkup getRequestContactKeyboard(String lang) {
        KeyboardButton button = new KeyboardButton(localizationService.getMessage("request.phone", lang));
        button.setRequestContact(true);
        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(new KeyboardRow(List.of(button))))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup getMainMenuKeyboard(String lang) {
        KeyboardRow row1 = new KeyboardRow(List.of(
                new KeyboardButton(localizationService.getMessage("menu.my_profile", lang))
        ));
        KeyboardRow row2 = new KeyboardRow(List.of(
                new KeyboardButton(localizationService.getMessage("menu.settings", lang))
        ));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .resizeKeyboard(true)
                .build();
    }
}