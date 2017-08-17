package net.mikhadyuk.usermanager.service;

import net.mikhadyuk.usermanager.dao.UserDao;
import net.mikhadyuk.usermanager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.saveAndFlush(user);
    }

    @Override
    public void blockOrUnblockUser(User user) {
        user.setBlocked(!user.isBlocked());
        userDao.saveAndFlush(user);
    }

    @Override
    public void blockOrUnblockUser(long userId) {
        blockOrUnblockUser(findById(userId));
    }

    @Override
    public void removeUser(long userId) {
        userDao.delete(userId);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findById(long userId) {
        return userDao.findById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAll();
    }
}
