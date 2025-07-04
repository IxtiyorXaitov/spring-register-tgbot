package com.ixtiyor.springregistertgbot.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Optional;

public interface UpdateHandler {

    boolean canHandle(Update update);

    Optional<BotApiMethod<? extends Serializable>> handle(Update update);
}