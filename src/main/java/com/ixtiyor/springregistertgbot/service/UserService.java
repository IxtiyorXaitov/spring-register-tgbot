package com.ixtiyor.springregistertgbot.service;

import com.ixtiyor.springregistertgbot.model.User;
import com.ixtiyor.springregistertgbot.model.UserState;
import com.ixtiyor.springregistertgbot.repository.InMemoryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final InMemoryUserRepository userRepository;

    public User findOrCreateUser(Long chatId) {
        return userRepository.findByChatId(chatId)
                .orElseGet(() -> {
                    User newUser = new User(chatId);
                    newUser.setState(UserState.START);
                    return userRepository.save(newUser);
                });
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
