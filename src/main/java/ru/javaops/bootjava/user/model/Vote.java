package ru.javaops.bootjava.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javaops.bootjava.common.model.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Getter
@Setter
@NoArgsConstructor
public class Vote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTimeVoted;

    @Column(name = "the_vote", nullable = false)
    @NotNull
    private boolean theVote;

    public Vote(Integer id, User user, Restaurant restaurant, LocalDateTime dateTimeVoted, boolean theVote) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.dateTimeVoted = dateTimeVoted;
        this.theVote = theVote;
    }
}
