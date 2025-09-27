package com.github.makotre.bootjava.vote.repository;

import com.github.makotre.bootjava.common.BaseRepository;
import com.github.makotre.bootjava.common.error.DataConflictException;
import com.github.makotre.bootjava.common.error.NotFoundException;
import com.github.makotre.bootjava.vote.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId")
    List<Vote> getAll(int userId);

    @Query("SELECT v FROM Vote v WHERE v.id = :id and v.user.id = :userId")
    Vote get(int userId, int id);

    default Vote getBelonged(int id, int userId) {
        Vote vote = findById(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
        if (vote.getUser().getId() == userId) {
            return get(userId, id);
        } else {
            throw new DataConflictException("vote id=" + id + " belongs to other user");
        }
    }

}
