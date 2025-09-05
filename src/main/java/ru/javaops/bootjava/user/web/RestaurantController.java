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
    static final String REST_URL = "/api";

    protected final Logger log = getLogger(getClass());

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private RestaurantService service;

    @GetMapping("/restaurants/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return repository.getExisted(id);
    }

    @DeleteMapping("admin/restaurants/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        repository.deleteExisted(id);
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return repository.findAll();
    }

    @PostMapping(value = "admin/restaurants", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        ValidationUtil.checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResponse = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "admin/restaurants/{id}").build().toUri();
        return ResponseEntity.created(uriOfNewResponse).body(created);
    }

    @PutMapping(value = "admin/restaurants/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {}", restaurant);
        ValidationUtil.assureIdConsistent(restaurant, id);
        repository.save(restaurant);
    }

    @PatchMapping("restaurants/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void vote(@PathVariable int id, @RequestParam boolean vote, @AuthenticationPrincipal AuthUser authUser) {
        log.info("user {} " + (vote ? "vote restaurant {}" : "unvote restaurant {}"), authUser.id(), id);
        service.vote(id, vote, authUser);
    }
}
