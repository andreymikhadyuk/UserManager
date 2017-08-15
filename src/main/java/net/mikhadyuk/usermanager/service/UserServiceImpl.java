package net.mikhadyuk.usermanager.service;

import net.mikhadyuk.usermanager.dao.UserDao;
import net.mikhadyuk.usermanager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public void blockUser(User user) {
        user.setBlocked(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); //?????
        userDao.saveAndFlush(user);
    }

    @Override
    public void removeUser(long id) {
        userDao.delete(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
