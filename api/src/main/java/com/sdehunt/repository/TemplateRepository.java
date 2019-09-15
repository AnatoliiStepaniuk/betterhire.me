package com.sdehunt.repository;

import com.sdehunt.commons.model.Language;
import com.sdehunt.commons.model.Template;

import java.util.Optional;

public interface TemplateRepository {

    Optional<Template> find(String taskId, Language language);

}
