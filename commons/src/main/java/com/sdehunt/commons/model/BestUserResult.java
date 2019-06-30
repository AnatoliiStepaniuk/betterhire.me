package com.sdehunt.commons.model;

import com.sdehunt.commons.TaskID;
import lombok.Data;

@Data
public class BestUserResult {

    private long rank;
    private TaskID taskID;

}
