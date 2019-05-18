package com.sdehunt.commons.model.impl;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Solution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SolutionImpl implements Solution {

    private String id;
    private TaskID taskId;
    private String userId;
    private String repo;
    private String commit;
    private long score;
    private Instant created;

}
