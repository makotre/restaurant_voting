package com.github.makotre.bootjava.menu.web;

import com.github.makotre.bootjava.AbstractControllerTest;
import com.github.makotre.bootjava.common.util.JsonUtil;
import com.github.makotre.bootjava.menu.model.Restaurant;
import com.github.makotre.bootjava.menu.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.makotre.bootjava.menu.RestaurantTestData.*;
import static com.github.makotre.bootjava.menu.RestaurantTestData.getNew;
import static com.github.makotre.bootjava.menu.RestaurantTestData.getUpdated;
import static com.github.makotre.bootjava.menu.web.RestaurantController.REST_URL;
import static com.github.makotre.bootjava.user.UserTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + "/restaurants/";
    private static final String REST_URL_SLASH_ADMIN = REST_URL + "/admin/restaurants/";

    @Autowired
    private RestaurantRepository repository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + R1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(R_MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getALl() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/restaurants"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(R_MATCHER.contentJson(restaurant1, restaurant2));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newR = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/admin/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newR)))
                .andExpect(status().isCreated());

        Restaurant created = R_MATCHER.readFromJson(action);
        int newId = created.id();
        newR.setId(newId);
        R_MATCHER.assertMatch(created, newR);
        R_MATCHER.assertMatch(repository.getExisted(newId), newR);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH_ADMIN + R1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        R_MATCHER.assertMatch(repository.getExisted(R1_ID), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH_ADMIN + R1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(R1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH_ADMIN + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, null);
        perform(MockMvcRequestBuilders.post(REST_URL + "/admin/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(R1_ID, null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH_ADMIN + R1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Restaurant updated = new Restaurant(restaurant1);
        updated.setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH_ADMIN + R1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}