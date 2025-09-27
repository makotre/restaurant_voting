package com.github.makotre.bootjava.vote;

import com.github.makotre.bootjava.MatcherFactory;
import com.github.makotre.bootjava.menu.RestaurantTestData;
import com.github.makotre.bootjava.user.UserTestData;
import com.github.makotre.bootjava.vote.model.Vote;
import com.github.makotre.bootjava.vote.to.VoteTo;

import java.time.LocalDateTime;
import java.time.Month;

public class VoteTestData {

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "restaurant", "user");
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class);

    public static final int VOTE_ID = 1;
    public static final Vote vote = new Vote(VOTE_ID, UserTestData.user, RestaurantTestData.restaurant1, LocalDateTime.of(2025, Month.SEPTEMBER, 26, 10, 0), true);
    public static final VoteTo voteTo = new VoteTo(vote.getId(), vote.getRestaurant().getId(), vote.getDateTimeVoted(), vote.isChoice());

}
