package com.github.makotre.bootjava.menu.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.github.makotre.bootjava.AbstractControllerTest;
import com.github.makotre.bootjava.common.util.JsonUtil;
import com.github.makotre.bootjava.menu.model.Dish;
import com.github.makotre.bootjava.menu.repository.DishRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.makotre.bootjava.menu.DishTestData.*;
import static com.github.makotre.bootjava.menu.DishTestData.getNew;
import static com.github.makotre.bootjava.menu.DishTestData.getUpdated;
import static com.github.makotre.bootjava.user.UserTestData.*;
import static com.github.makotre.bootjava.menu.web.DishController.REST_URL;

public class DishControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + "/restaurants/1/dishes/";
    private static final String REST_URL_SLASH_ADMIN = REST_URL + "/admin/restaurants/1/dishes/";

    @Autowired
    DishRepository dishRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1));
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
    void getFromOtherR() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + DISHR2_ID))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/restaurants/1/dishes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(r1Dishes));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/admin/restaurants/1/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isCreated());
        Dish created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishRepository.getExisted(newId), newDish);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH_ADMIN + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(dishRepository.getExisted(DISH1_ID), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateFromOtherR() throws Exception {
        Dish updated = getUpdated();
        updated.setId(DISHR2_ID);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH_ADMIN + DISHR2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH_ADMIN + DISH1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.findById(DISH1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteFromOtherR() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH_ADMIN + DISHR2_ID))
                .andDo(print())
                .andExpect(status().isConflict());
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
        Dish invalid = new Dish(null, null, 0, null);
        perform(MockMvcRequestBuilders.post(REST_URL + "/admin/restaurants/1/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DISH1_ID, null, 0, null);
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH_ADMIN + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Dish updated = new Dish(dish1);
        updated.setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH_ADMIN + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Dish duplicate = new Dish(null, dish1.getName(), 10, dish1.getServingDate());
        perform(MockMvcRequestBuilders.post(REST_URL + "/admin/restaurants/1/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicate)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        Dish duplicate = new Dish(DISH1_ID, dish2.getName(), 10, dish1.getServingDate());
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH_ADMIN + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicate)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createForAnotherDay() throws Exception {
        when(clock.instant()).thenReturn(LocalDateTime.now().minusDays(1)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        Dish dish = new Dish(null, dish1.getName(), 10, dish1.getServingDate());
        perform(MockMvcRequestBuilders.post(REST_URL + "/admin/restaurants/1/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(dish)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateForAnotherDay() throws Exception {
        when(clock.instant()).thenReturn(LocalDateTime.now().minusDays(1)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH_ADMIN + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
