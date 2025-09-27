package com.github.makotre.bootjava.vote.service;

import com.github.makotre.bootjava.app.AuthUser;
import com.github.makotre.bootjava.common.error.DataConflictException;
import com.github.makotre.bootjava.menu.model.Dish;
import com.github.makotre.bootjava.menu.model.Restaurant;
import com.github.makotre.bootjava.menu.repository.RestaurantRepository;
import com.github.makotre.bootjava.user.model.User;
import com.github.makotre.bootjava.user.repository.UserRepository;
import com.github.makotre.bootjava.vote.model.Vote;
import com.github.makotre.bootjava.vote.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
@Transactional
public class VoteService {
    static final LocalTime DEADLINE = LocalTime.of(11, 0);

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    private final VoteRepository voteRepository;

    @Autowired
    private Clock clock;

    public void vote(int rId, boolean vote, AuthUser authUser) {
        Restaurant restaurant = restaurantRepository.getExisted(rId);
        User user = userRepository.getWithVotes(authUser.id());
        LocalDateTime now = LocalDateTime.now(clock);

        if (!restaurant.getDishes().stream()
                .map(Dish::getServingDate)
                .toList().contains(now.toLocalDate())) {
            throw new DataConflictException("There is no menu in restaurant with id=" + rId + " for today");
        }

        Vote todaysVote = user.getVotes().stream()
                .filter(v -> v.getDateTimeVoted().toLocalDate().equals(now.toLocalDate()))
                .findAny().orElse(null);

        if (todaysVote == null) { //didn't vote yet
            if (vote) {
                todaysVote = new Vote(null, user, restaurant, now, vote);
                voteRepository.save(todaysVote);
            }
        } else {
            if (now.toLocalTime().isBefore(DEADLINE)) {
                if (!todaysVote.getRestaurant().equals(restaurant)) {
                    if (vote) {
                        todaysVote.setRestaurant(restaurant);
                        todaysVote.setDateTimeVoted(now);
                        todaysVote.setChoice(vote);
                        voteRepository.save(todaysVote);
                    }
                } else {
                    if (vote != todaysVote.isChoice()) {
                        todaysVote.setDateTimeVoted(now);
                        todaysVote.setChoice(vote);
                        voteRepository.save(todaysVote);
                    }
                }
            } else {
                throw new DataConflictException("it is too late, vote can't be changed");
            }
        }
    }
}
