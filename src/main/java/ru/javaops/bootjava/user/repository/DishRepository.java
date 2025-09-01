package ru.javaops.bootjava.user.repository;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface DishRepository {

}
