package ru.javaops.bootjava.user.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.common.validation.ValidationUtil;
import ru.javaops.bootjava.user.model.Dish;
import ru.javaops.bootjava.user.model.Restaurant;
import ru.javaops.bootjava.user.repository.DishRepository;
import ru.javaops.bootjava.user.repository.RestaurantRepository;
import ru.javaops.bootjava.user.service.RestaurantService;

import java.net.URI;
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
    private RestaurantRepository repository;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/restaurants/{rId}/dishes")
    public List<Dish> getAll(@PathVariable int rId) {
        log.info("getAll dishes for restaurant {}", rId);
        repository.getExisted(rId);
        return dishRepository.getAll(rId);
    }

    @GetMapping("/restaurants/{rId}/dishes/{id}")
    public Dish get(@PathVariable int rId, @PathVariable int id) {
        log.info("get dish {} from restaurant {}", id, rId);
        repository.getExisted(rId);
        return dishRepository.getBelonged(id, rId);
    }

    @DeleteMapping("/admin/restaurants/{rId}/dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int rId, @PathVariable int id) {
        log.info("delete dish {} in restaurant {}", id, rId);
        repository.getExisted(rId);
        Dish dish = dishRepository.getBelonged(id, rId);
        dishRepository.delete(dish);
    }

    @PostMapping(value = "/admin/restaurants/{rId}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int rId) {
        log.info("create dish {} in restaurant {}", dish, rId);
        ValidationUtil.checkNew(dish);
        Restaurant restaurant = repository.getExisted(rId);
        restaurantService.checkFromToday(restaurant);
        dish.setRestaurant(restaurant);
        Dish created = dishRepository.save(dish);
        URI uriOfNewResponse = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "admin/restaurants/{rId}/dishes/{id}").build().toUri();
        return ResponseEntity.created(uriOfNewResponse).body(created);
    }

    @PutMapping(value = "/admin/restaurants/{rId}/dishes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody Dish dish, @PathVariable int rId, @PathVariable int id) {
        log.info("update dish {} in restaurant {}", dish, rId);
        ValidationUtil.assureIdConsistent(dish, id);
        Restaurant restaurant = repository.getExisted(rId);
        restaurantService.checkFromToday(restaurant);
        dish.setRestaurant(restaurant);
        dishRepository.getBelonged(id, rId);
        dishRepository.save(dish);
    }
}
