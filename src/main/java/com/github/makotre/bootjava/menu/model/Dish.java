package com.github.makotre.bootjava.menu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.github.makotre.bootjava.common.model.NamedEntity;

import java.time.LocalDate;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(columnNames = {"r_id", "serving_date", "name"}, name = "dish_unique_restaurant_servingdate_dishname_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity {

    @NotNull
    @Column(name = "price", nullable = false)
    private int price;

    @NotNull
    @Column(name = "serving_date", nullable = false)
    private LocalDate servingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_id", nullable = false)
    @Schema(hidden = true)
    @JsonIgnore
    private Restaurant restaurant;

    public Dish(Integer id, String name, int price, LocalDate servingDate) {
        super(id, name);
        this.price = price;
        this.servingDate = servingDate;
    }

    public Dish(Dish d) {
        this(d.id, d.name, d.price, d.servingDate);
    }
}
