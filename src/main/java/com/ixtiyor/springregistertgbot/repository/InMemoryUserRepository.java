package com.ixtiyor.springregistertgbot.repository;

import com.ixtiyor.springregistertgbot.model.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository {
    private final Map<Long, User> userCache = new ConcurrentHashMap<>();

    public Optional<User> findByChatId(Long chatId) {
        return Optional.ofNullable(userCache.get(chatId));
    }

    public User save(User user) {
        userCache.put(user.getChatId(), user);
        return user;
    }
}
