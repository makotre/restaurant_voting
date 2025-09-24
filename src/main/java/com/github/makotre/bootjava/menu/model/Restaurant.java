package com.github.makotre.bootjava.menu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.github.makotre.bootjava.common.model.NamedEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @Column(name = "voters_count", nullable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer votersCount = 0;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(hidden = true)
    @JsonIgnore
    private List<Dish> dishes;

    @Column(name = "create_date", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate createDate = LocalDate.now();

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.votersCount, r.createDate);
        this.dishes = List.copyOf(r.dishes);
    }

    public Restaurant(Integer id, String name, Integer votersCount, LocalDate createDate) {
        super(id, name);
        this.votersCount = votersCount;
        this.createDate = createDate;
    }

    public void plusVote() {
        votersCount++;
    }

    public void minusVote() {
        votersCount--;
    }
}
