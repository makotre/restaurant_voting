package com.github.makotre.bootjava.menu;

import com.github.makotre.bootjava.MatcherFactory;
import com.github.makotre.bootjava.menu.model.Dish;

import java.util.List;

public class DishTestData {

    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");

    public static final int DISH1_ID = 1;
    public static final int DISHR2_ID = DISH1_ID + 5;
    public static final Dish dish1 = new Dish(DISH1_ID, "Breakfast", 50);
    public static final Dish dish2 = new Dish(DISH1_ID + 1, "Lunch", 10);
    public static final Dish dish3 = new Dish(DISH1_ID + 2, "AfterLunch", 50);
    public static final Dish dish4 = new Dish(DISH1_ID + 3, "Soop", 60);
    public static final Dish dish5 = new Dish(DISH1_ID + 4, "Potato", 30);
    public static final Dish dish6 = new Dish(DISHR2_ID, "Chachapuri", 15);
    public static final Dish dish7 = new Dish(DISH1_ID + 6, "Tea", 10);
    public static final Dish dish8 = new Dish(DISH1_ID + 7, "Coffee", 15);

    public static final List<Dish> r1Dishes = List.of(dish1, dish2, dish3, dish4, dish5);
    public static final List<Dish> r2Dishes = List.of(dish6, dish7, dish8);

    public static Dish getNew() {
        return new Dish(null, "New", 100);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "UpdatedName", 100);
    }
}
