package com.github.makotre.bootjava.vote.to;

import com.github.makotre.bootjava.common.to.BaseTo;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    @NotNull
    int rId;

    @NotNull
    LocalDateTime dateTimeVoted;

    @NotNull
    boolean choice;

    public VoteTo(Integer id, int rId, LocalDateTime dateTimeVoted, boolean choice) {
        super(id);
        this.rId = rId;
        this.dateTimeVoted = dateTimeVoted;
        this.choice = choice;
    }
}
