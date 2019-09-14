package com.sdehunt.repository;

public interface TaskApplicationRepository {

    void save(String company, String task, String jobUrl, String taskUrl);

}
