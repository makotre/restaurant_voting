package com.github.makotre.bootjava.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.makotre.bootjava.common.BaseRepository;
import com.github.makotre.bootjava.common.error.NotFoundException;
import com.github.makotre.bootjava.user.model.User;

import java.util.Optional;

import static com.github.makotre.bootjava.app.config.SecurityConfig.PASSWORD_ENCODER;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {
    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.votes WHERE u.id = :id")
    User getWithVotes(int id);

    @Transactional
    default User prepareAndSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return save(user);
    }

    default User getExistedByEmail(String email) {
        return findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException("User with email=" + email + " not found"));
    }
}