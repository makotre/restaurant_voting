package com.github.makotre.bootjava.menu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.makotre.bootjava.common.model.NamedEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(hidden = true)
    @JsonIgnore
    private List<Dish> dishes;

    public Restaurant(Restaurant r) {
        this(r.id, r.name);
        this.dishes = List.copyOf(r.dishes);
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }
}
