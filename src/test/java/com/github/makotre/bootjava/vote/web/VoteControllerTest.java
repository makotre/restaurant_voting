package com.github.makotre.bootjava.vote.web;

import com.github.makotre.bootjava.AbstractControllerTest;
import com.github.makotre.bootjava.vote.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZoneId;
import java.util.List;

import static com.github.makotre.bootjava.menu.RestaurantTestData.R1_ID;
import static com.github.makotre.bootjava.menu.RestaurantTestData.R2_ID;
import static com.github.makotre.bootjava.user.UserTestData.*;
import static com.github.makotre.bootjava.vote.VoteTestData.*;
import static com.github.makotre.bootjava.vote.web.VoteController.REST_URL;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + "/";

    @Autowired
    private VoteRepository repository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(List.of(voteTo)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + VOTE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(voteTo));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void vote() throws Exception {
        when(clock.instant()).thenReturn(vote.getDateTimeVoted().plusDays(1)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + R1_ID)
                .param("vote", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        VOTE_MATCHER.assertMatch(repository.getBelonged(VOTE_ID, USER_ID), vote);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void voteForNotFound() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + NOT_FOUND)
                .param("vote", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void voteForReWithNoDishesToday() throws Exception {
        when(clock.instant()).thenReturn(vote.getDateTimeVoted().plusDays(5)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + R2_ID)
                .param("vote", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void voteSecondTimeAfterEleven() throws Exception {
        when(clock.instant()).thenReturn(vote.getDateTimeVoted().plusHours(2)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        //second time Exception
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + R2_ID)
                .param("vote", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void voteSecondTimeBeforeEleven() throws Exception {
        when(clock.instant()).thenReturn(vote.getDateTimeVoted().plusMinutes(10)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        //second time Exception
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + R2_ID)
                .param("vote", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
