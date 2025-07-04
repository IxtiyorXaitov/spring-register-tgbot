package com.ixtiyor.springregistertgbot.model;

public enum UserState {
    START,
    AWAITING_LANGUAGE,
    AWAITING_PHONE_NUMBER,
    AWAITING_OTP,
    AWAITING_FIRST_NAME,
    AWAITING_LAST_NAME,
    REGISTERED
}