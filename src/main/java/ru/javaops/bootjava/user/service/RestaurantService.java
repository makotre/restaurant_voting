package ru.javaops.bootjava.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.app.AuthUser;
import ru.javaops.bootjava.common.error.DataConflictException;
import ru.javaops.bootjava.common.error.NotFoundException;
import ru.javaops.bootjava.user.model.Restaurant;
import ru.javaops.bootjava.user.model.Role;
import ru.javaops.bootjava.user.model.User;
import ru.javaops.bootjava.user.model.Vote;
import ru.javaops.bootjava.user.repository.RestaurantRepository;
import ru.javaops.bootjava.user.repository.UserRepository;
import ru.javaops.bootjava.user.repository.VoteRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository repository;

    private final UserRepository userRepository;

    private final VoteRepository voteRepository;

    public void checkAdmin(AuthUser authUser) {
        if (!authUser.hasRole(Role.ADMIN)) {
            throw new DataConflictException("User id=" + authUser.id() + " is not an Admin");
        }
    }

    public Restaurant delete(AuthUser authUser, int id) {
        checkAdmin(authUser);
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Restaurant id=" + id + " is not exist"));
    }

    @Transactional
    public Restaurant save(AuthUser authUser, Restaurant restaurant) {
        checkAdmin(authUser);
        return repository.save(restaurant);
    }

    public void vote(int id, boolean vote, AuthUser authUser) {
        Restaurant restaurant = repository.getExisted(id);
        User user = userRepository.getExisted(authUser.id());

        Vote todaysVote = user.getVotes().stream()
                .filter(v -> v.getDateTimeVoted().toLocalDate().equals(restaurant.getCreateDate()))
                .findAny().orElse(null);

        if (todaysVote == null) { //didn't vote yet
            if (vote) {
                todaysVote = new Vote();
                todaysVote.setUser(user);
                todaysVote.setRestaurant(restaurant);
                todaysVote.setDateTimeVoted(LocalDateTime.now());
                todaysVote.setTheVote(vote);

                restaurant.plusVote();
                voteRepository.save(todaysVote);
                user.getVotes().add(todaysVote);
            }
        } else {
            LocalDateTime now = LocalDateTime.now();
            if (now.toLocalTime().isBefore(LocalTime.of(14, 0))) {
                if (!todaysVote.getRestaurant().equals(restaurant)) {
                    if (vote) {
                        if (todaysVote.isTheVote()) {
                            todaysVote.getRestaurant().minusVote();
                        }
                        restaurant.plusVote();
                        todaysVote.setRestaurant(restaurant);
                        todaysVote.setDateTimeVoted(now);
                        voteRepository.save(todaysVote);
                    }
                } else {
                    if (vote != todaysVote.isTheVote()) {
                        if (vote) {
                            restaurant.plusVote();
                        } else {
                            restaurant.minusVote();
                        }
                        todaysVote.setDateTimeVoted(now);
                        todaysVote.setTheVote(vote);
                        voteRepository.save(todaysVote);
                    }
                }
            } else {
                throw new DataConflictException("it is too late, vote can't be changed");
            }
        }
    }
}
