package com.github.makotre.bootjava.menu.repository;

import org.springframework.transaction.annotation.Transactional;
import com.github.makotre.bootjava.common.BaseRepository;
import com.github.makotre.bootjava.menu.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

}
