package com.github.makotre.bootjava.vote.web;

import com.github.makotre.bootjava.app.AuthUser;
import com.github.makotre.bootjava.vote.model.Vote;
import com.github.makotre.bootjava.vote.repository.VoteRepository;
import com.github.makotre.bootjava.vote.service.VoteService;
import com.github.makotre.bootjava.vote.to.VoteTo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/api/votes";

    private final Logger log = getLogger(getClass());

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private VoteService service;

    @GetMapping()
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll votes for user {}", authUser.id());
        return voteRepository.getAll(authUser.id()).stream()
                .map(v -> new VoteTo(v.getId(), v.getRestaurant().getId(), v.getDateTimeVoted(), v.isChoice()))
                .toList();
    }

    @GetMapping("/{id}")
    public VoteTo get(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("get vote {} for user {}", id, userId);
        Vote v = voteRepository.getBelonged(id, userId);
        return new VoteTo(v.getId(), v.getRestaurant().getId(), v.getDateTimeVoted(), v.isChoice());
    }

    @PatchMapping("/{rId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int rId, @RequestParam boolean vote, @AuthenticationPrincipal AuthUser authUser) {
        log.info("user {} " + (vote ? "vote restaurant {}" : "unvote restaurant {}"), authUser.id(), rId);
        service.vote(rId, vote, authUser);
    }
}
