package com.github.makotre.bootjava.user;

import lombok.experimental.UtilityClass;
import com.github.makotre.bootjava.user.model.Role;
import com.github.makotre.bootjava.user.model.User;
import com.github.makotre.bootjava.user.to.UserTo;

@UtilityClass
public class UsersUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}