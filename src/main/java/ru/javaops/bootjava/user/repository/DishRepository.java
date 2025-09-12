package ru.javaops.bootjava.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.common.BaseRepository;
import ru.javaops.bootjava.common.error.DataConflictException;
import ru.javaops.bootjava.common.error.NotFoundException;
import ru.javaops.bootjava.user.model.Dish;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:rId")
    List<Dish> getAll(int rId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:rId and d.id=:id")
    Dish get(int id, int rId);

    default Dish getBelonged(int id, int rId) {
        Dish dish = findById(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
        if (dish.getRestaurant().getId() == rId) {
            return get(id, rId);
        } else {
            throw new DataConflictException("dish id=" + id + " doesn't belong to restaurant id=" + rId);
        }
    }
}
