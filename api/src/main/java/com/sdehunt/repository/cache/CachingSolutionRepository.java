package com.sdehunt.repository.cache;

import com.sdehunt.commons.cache.Cache;
import com.sdehunt.commons.cache.CacheImpl;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.repository.SolutionQuery;
import com.sdehunt.repository.SolutionRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CachingSolutionRepository implements SolutionRepository {

    private SolutionRepository inner;
    private Cache<String, Long> totalSolutionsCache;

    public CachingSolutionRepository(SolutionRepository inner) {
        this.inner = inner;
        this.totalSolutionsCache = new CacheImpl<>("totalSolutions", String.class, Long.class, 1, 300);
    }

    @Override
    public String save(Solution solution) {
        return inner.save(solution);
    }

    @Override
    public void update(Solution solution) {
        inner.update(solution);
    }

    @Override
    public Optional<Solution> get(String id) {
        return inner.get(id);
    }

    @Override
    public void delete(String id) {
        inner.delete(id);
    }

    @Override
    public List<Solution> query(SolutionQuery request) {
        return inner.query(request);
    }

    @Override
    public boolean isPresentForUser(Solution solution) {
        return inner.isPresentForUser(solution);
    }

    @Override
    public long getTotalSolutions() {
        return totalSolutionsCache.computeIfAbsent("", s -> inner.getTotalSolutions());
    }

    @Override
    public Map<String, List<String>> getAllRepos() {
        return inner.getAllRepos();
    }
}
