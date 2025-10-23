package com.example.backend.repo;

import com.example.backend.model.BeneficioDto;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class BeneficioRepository {
    private final Map<Long, BeneficioDto> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    public List<BeneficioDto> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<BeneficioDto> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public BeneficioDto save(BeneficioDto dto) {
        long id = seq.incrementAndGet();
        dto.setId(id);
        store.put(id, dto);
        return dto;
    }

    public Optional<BeneficioDto> update(Long id, BeneficioDto dto) {
        return Optional.ofNullable(store.computeIfPresent(id, (k, old) -> {
            dto.setId(id);
            return dto;
        }));
    }

    public boolean delete(Long id) {
        return store.remove(id) != null;
    }
}
