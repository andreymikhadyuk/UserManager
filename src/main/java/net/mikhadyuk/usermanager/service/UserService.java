package net.mikhadyuk.usermanager.service;

import net.mikhadyuk.usermanager.model.User;

import java.util.List;

public interface UserService {
    void save(User user);

    void blockOrUnblockUser(User user);

    void blockOrUnblockUser(long userId);

    void removeUser(long userId);

    User findByUsername(String username);

    User findById(long userId);

    List<User> findAllUsers();
}
