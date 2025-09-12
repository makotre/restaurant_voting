package ru.javaops.bootjava.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javaops.bootjava.common.model.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_time"}, name = "vote_unique_user_datetime_idx")})
@Getter
@Setter
@NoArgsConstructor
public class Vote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(hidden = true)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @Schema(hidden = true)
    @JsonIgnore
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

    @JsonProperty("userId")
    public Integer getUserId() {
        return user != null ? user.getId() : null;
    }
    @JsonProperty("restaurantId")
    public Integer getRestaurantId() {
        return restaurant != null ? restaurant.getId() : null;
    }
}
