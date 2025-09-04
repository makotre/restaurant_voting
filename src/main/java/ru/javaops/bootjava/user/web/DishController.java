package ru.javaops.bootjava.user.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.app.AuthUser;
import ru.javaops.bootjava.user.model.Dish;
import ru.javaops.bootjava.user.model.Restaurant;
import ru.javaops.bootjava.user.repository.DishRepository;
import ru.javaops.bootjava.user.repository.RestaurantRepository;

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
}
