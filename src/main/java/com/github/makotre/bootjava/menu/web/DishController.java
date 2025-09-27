package com.github.makotre.bootjava.menu.web;

import com.github.makotre.bootjava.common.validation.ValidationUtil;
import com.github.makotre.bootjava.menu.model.Dish;
import com.github.makotre.bootjava.menu.model.Restaurant;
import com.github.makotre.bootjava.menu.repository.DishRepository;
import com.github.makotre.bootjava.menu.repository.RestaurantRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    static final String REST_URL = "/api";

    protected final Logger log = getLogger(getClass());

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Cacheable(value = "dishes", key = "#rId")
    @GetMapping("/restaurants/{rId}/dishes")
    public List<Dish> getAll(@PathVariable int rId) {
        log.info("getAll dishes for restaurant {}", rId);
        restaurantRepository.getExisted(rId);
        return dishRepository.getAll(rId);
    }

    @GetMapping("/restaurants/{rId}/dishes/{id}")
    public Dish get(@PathVariable int rId, @PathVariable int id) {
        log.info("get dish {} from restaurant {}", id, rId);
        restaurantRepository.getExisted(rId);
        return dishRepository.getBelonged(id, rId);
    }

    @Cacheable(value = "dishes", key = "#rId")
    @GetMapping("/restaurants/{rId}/dishes/by-date")
    public List<Dish> getByDate(@PathVariable int rId,
                                @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get menu for a date {} from restaurant {} ", date, rId);
        return dishRepository.getAll(rId).stream()
                .filter(dish -> dish.getServingDate().equals(date))
                .toList();
    }

    @Cacheable("dishes")
    @GetMapping("/restaurants/dishes/by-date")
    public List<Dish> getInAllByDate(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get All menu from all restaurants for a date {}", date);
        return dishRepository.findAll().stream()
                .filter(dish -> dish.getServingDate().equals(date))
                .toList();
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @DeleteMapping("/admin/restaurants/{rId}/dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int rId, @PathVariable int id) {
        log.info("delete dish {} in restaurant {}", id, rId);
        restaurantRepository.getExisted(rId);
        Dish dish = dishRepository.getBelonged(id, rId);
        dishRepository.delete(dish);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @PostMapping(value = "/admin/restaurants/{rId}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int rId) {
        log.info("create dish {} in restaurant {}", dish, rId);
        ValidationUtil.checkNew(dish);
        Restaurant restaurant = restaurantRepository.getExisted(rId);
        dish.setRestaurant(restaurant);
        Dish created = dishRepository.save(dish);
        URI uriOfNewResponse = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "admin/restaurants/{rId}/dishes/{id}").build().toUri();
        return ResponseEntity.created(uriOfNewResponse).body(created);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @PutMapping(value = "/admin/restaurants/{rId}/dishes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody Dish dish, @PathVariable int rId, @PathVariable int id) {
        log.info("update dish {} in restaurant {}", dish, rId);
        ValidationUtil.assureIdConsistent(dish, id);
        Restaurant restaurant = restaurantRepository.getExisted(rId);
        dish.setRestaurant(restaurant);
        dishRepository.getBelonged(id, rId);
        dishRepository.save(dish);
    }
}
