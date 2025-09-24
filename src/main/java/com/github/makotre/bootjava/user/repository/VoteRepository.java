package com.github.makotre.bootjava.user.repository;

import org.springframework.transaction.annotation.Transactional;
import com.github.makotre.bootjava.common.BaseRepository;
import com.github.makotre.bootjava.user.model.Vote;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

}
