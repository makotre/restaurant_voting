package com.github.makotre.bootjava.vote.repository;

import org.springframework.transaction.annotation.Transactional;
import com.github.makotre.bootjava.common.BaseRepository;
import com.github.makotre.bootjava.vote.model.Vote;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

}
