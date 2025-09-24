package com.github.makotre.bootjava.user.repository;

import org.springframework.transaction.annotation.Transactional;
import com.github.makotre.bootjava.common.BaseRepository;
import com.github.makotre.bootjava.user.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

}
