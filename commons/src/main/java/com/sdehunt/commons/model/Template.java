package com.sdehunt.commons.model;

import com.sdehunt.commons.TaskID;
import lombok.Data;

/**
 * Template repository for task solution
 */
@Data
public class Template {

    TaskID taskID;
    String repo;
}
