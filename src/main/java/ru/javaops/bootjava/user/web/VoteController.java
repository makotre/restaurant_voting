package ru.javaops.bootjava.user.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.bootjava.user.model.Vote;
import ru.javaops.bootjava.user.repository.VoteRepository;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/api/admin/votes";

    protected final Logger log = getLogger(getClass());

    @Autowired
    private VoteRepository voteRepository;

    @GetMapping()
    public List<Vote> getAll() {
        log.info("getAll votes");
        return voteRepository.findAll();
    }
}
