package com.github.makotre.bootjava.app;

import lombok.Getter;
import org.springframework.lang.NonNull;
import com.github.makotre.bootjava.user.model.Role;
import com.github.makotre.bootjava.user.model.User;

public class AuthUser extends org.springframework.security.core.userdetails.User {

    @Getter
    private final User user;

    public AuthUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        this.user = user;
    }

    public int id() {
        return user.id();
    }

    public boolean hasRole(Role role) {
        return user.hasRole(role);
    }

    @Override
    public String toString() {
        return "AuthUser:" + id() + '[' + user.getEmail() + ']';
    }
}