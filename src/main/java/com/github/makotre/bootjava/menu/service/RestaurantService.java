package com.github.makotre.bootjava.menu.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.makotre.bootjava.app.AuthUser;
import com.github.makotre.bootjava.common.error.DataConflictException;
import com.github.makotre.bootjava.menu.model.Restaurant;
import com.github.makotre.bootjava.user.model.User;
import com.github.makotre.bootjava.vote.model.Vote;
import com.github.makotre.bootjava.menu.repository.RestaurantRepository;
import com.github.makotre.bootjava.user.repository.UserRepository;
import com.github.makotre.bootjava.vote.repository.VoteRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository repository;

    private final UserRepository userRepository;

    private final VoteRepository voteRepository;

    @Autowired
    private Clock clock;

    public void vote(int id, boolean vote, AuthUser authUser) {
        Restaurant restaurant = repository.getExisted(id);
        checkFromToday(restaurant);

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
            LocalDateTime now = LocalDateTime.now(clock);
            if (now.toLocalTime().isBefore(LocalTime.of(11, 0))) {
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

    public void checkFromToday(Restaurant restaurant) {
        if (!restaurant.getCreateDate().equals(LocalDate.now(clock))) {
            throw new DataConflictException("restaurant id=" + restaurant.getId() + " is not available today");
        }
    }
}
