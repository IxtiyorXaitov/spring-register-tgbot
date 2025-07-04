package com.ixtiyor.springregistertgbot.model;

import lombok.Data;

@Data
public class User {
    private final Long chatId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String languageCode = "en";
    private UserState state = UserState.START;
}