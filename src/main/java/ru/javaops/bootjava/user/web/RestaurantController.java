package ru.javaops.bootjava.user.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.app.AuthUser;
import ru.javaops.bootjava.common.validation.ValidationUtil;
import ru.javaops.bootjava.user.model.Restaurant;
import ru.javaops.bootjava.user.repository.RestaurantRepository;
import ru.javaops.bootjava.user.service.RestaurantService;

import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    static final String REST_URL = "/api/admin/restaurant";

    protected final Logger log = getLogger(getClass());

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private RestaurantService service;

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return repository.getExisted(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete restaurant {} by user {}", id, authUser.id());
        Restaurant restaurant = service.delete(authUser, id);
        repository.delete(restaurant);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return repository.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Restaurant restaurant) {
        log.info("create {} by user {}", restaurant, authUser.id());
        ValidationUtil.checkNew(restaurant);
        Restaurant created = service.save(authUser, restaurant);
        URI uriOfNewResponse = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResponse).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} by user {}", restaurant, authUser.id());
        ValidationUtil.assureIdConsistent(restaurant, id);
        service.save(authUser, restaurant);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void vote(@PathVariable int id, @RequestParam boolean vote) {
        log.info(vote ? "vote {}" : "unvote {}", id);
        Restaurant restaurant = repository.getExisted(id);
        restaurant.setVote(vote);
    }
}
