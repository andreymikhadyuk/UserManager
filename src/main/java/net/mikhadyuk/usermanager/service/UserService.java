package net.mikhadyuk.usermanager.service;

import net.mikhadyuk.usermanager.model.User;

public interface UserService {
    void save(User user);

    void blockUser(User user);

    void removeUser(long id);

    User findByUsername(String username);
}
