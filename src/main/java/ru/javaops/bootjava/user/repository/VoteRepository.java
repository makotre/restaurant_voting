package ru.javaops.bootjava.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.common.BaseRepository;
import ru.javaops.bootjava.user.model.Vote;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

}
