package net.mikhadyuk.usermanager.dao;

import net.mikhadyuk.usermanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDao extends JpaRepository<User, Long> {
    //@Query("SELECT u FROM User u where u.username = :username")
    User findByUsername(@Param("username") String username);
    User findById(@Param("id") long id);
}
