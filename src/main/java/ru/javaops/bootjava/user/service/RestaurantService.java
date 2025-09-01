package ru.javaops.bootjava.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.app.AuthUser;
import ru.javaops.bootjava.common.error.DataConflictException;
import ru.javaops.bootjava.user.model.Restaurant;
import ru.javaops.bootjava.user.model.Role;
import ru.javaops.bootjava.user.repository.RestaurantRepository;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository repository;

    public void checkAdmin(AuthUser authUser) {
        if (!authUser.hasRole(Role.ADMIN)) {
            throw new DataConflictException("User id=" + authUser.id() + " is not an Admin");
        }
    }

    public Restaurant delete(AuthUser authUser, int id) {
        checkAdmin(authUser);
        return repository.findById(id).orElseThrow(
                () -> new DataConflictException("Restaurant id=" + id + " is not exist"));
    }

    @Transactional
    public Restaurant save(AuthUser authUser, Restaurant restaurant) {
        checkAdmin(authUser);
        return repository.save(restaurant);
    }
}
