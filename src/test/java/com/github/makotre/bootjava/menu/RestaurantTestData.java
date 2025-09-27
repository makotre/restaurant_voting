package com.github.makotre.bootjava.menu;

import com.github.makotre.bootjava.MatcherFactory;
import com.github.makotre.bootjava.menu.model.Restaurant;

import java.time.LocalDate;

import static com.github.makotre.bootjava.menu.DishTestData.r1Dishes;
import static com.github.makotre.bootjava.menu.DishTestData.r2Dishes;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> R_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "dishes");

    public static final int R1_ID = 1;
    public static final int R2_ID = R1_ID + 1;

    public static final Restaurant restaurant1 = new Restaurant(R1_ID, "MacDac");
    public static final Restaurant restaurant2 = new Restaurant(R2_ID, "KeeFCi");

    static {
        restaurant1.setDishes(r1Dishes);
        restaurant2.setDishes(r2Dishes);
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "New");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(R1_ID, "UpdatedName");
    }
}
