package ru.javaops.bootjava.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.bootjava.common.model.NamedEntity;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @Column(name = "vote", nullable = false, columnDefinition = "bool default false")
    private boolean vote;

    @Column(name = "voters_count", nullable = false)
    @NotNull
    private Integer votersCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(hidden = true)
    private List<Dish> dishes;

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.vote, r.votersCount);
        this.dishes = List.copyOf(r.dishes);
    }

    public Restaurant(Integer id, String name, boolean vote, Integer votersCount) {
        super(id, name);
        this.vote = vote;
        this.votersCount = votersCount;
    }
}
