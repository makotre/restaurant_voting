package com.github.makotre.bootjava.vote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.makotre.bootjava.common.model.BaseEntity;
import com.github.makotre.bootjava.menu.model.Restaurant;
import com.github.makotre.bootjava.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(hidden = true)
    @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTimeVoted;

    @Column(name = "choice", nullable = false)
    @NotNull
    private boolean choice;

    public Vote(Integer id, User user, Restaurant restaurant, LocalDateTime dateTimeVoted, boolean choice) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.dateTimeVoted = dateTimeVoted;
        this.choice = choice;
    }
}
