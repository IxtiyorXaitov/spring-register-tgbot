package com.ixtiyor.springregistertgbot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Component
public class UpdateDispatcher {

    private final List<UpdateHandler> handlers;

    public UpdateDispatcher(List<UpdateHandler> handlers) {
        this.handlers = handlers;
    }

    public Optional<BotApiMethod<? extends Serializable>> dispatch(Update update) {

        return handlers.stream()
                .filter(handler -> handler.canHandle(update))
                .findFirst()
                .flatMap(handler -> handler.handle(update));
    }
}
