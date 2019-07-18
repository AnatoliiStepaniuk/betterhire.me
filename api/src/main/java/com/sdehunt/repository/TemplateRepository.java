package com.sdehunt.repository;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Template;

import java.util.Optional;

public interface TemplateRepository {

    Optional<Template> find(TaskID taskId);

}
