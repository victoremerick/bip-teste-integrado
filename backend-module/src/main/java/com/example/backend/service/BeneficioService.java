package com.example.backend.service;

import com.example.backend.model.BeneficioDto;
import com.example.backend.repo.BeneficioRepository;
import com.example.backend.integration.EjbAdapter;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BeneficioService {
    private final BeneficioRepository repo;
    private final EjbAdapter ejbAdapter;

    // Construtor padrão para produção
    public BeneficioService() {
        this.repo = new BeneficioRepository();
        this.ejbAdapter = new EjbAdapter();
    }

    // Construtor para testes/injeção manual
    public BeneficioService(BeneficioRepository repo, EjbAdapter ejbAdapter) {
        this.repo = repo;
        this.ejbAdapter = ejbAdapter;
    }

    public List<BeneficioDto> list() {
        return repo.findAll();
    }

    public BeneficioDto get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Benefício não encontrado"));
    }

    public BeneficioDto create(BeneficioDto dto) {
        if (dto.getAtivo() == null) dto.setAtivo(Boolean.TRUE);
        return repo.save(dto);
    }

    public BeneficioDto update(Long id, BeneficioDto dto) {
        return repo.update(id, dto).orElseThrow(() -> new NoSuchElementException("Benefício não encontrado"));
    }

    public void delete(Long id) {
        if (!repo.delete(id)) {
            throw new NoSuchElementException("Benefício não encontrado");
        }
    }

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        // Integração com EJB; validações de erro vêm do EJB
        ejbAdapter.transfer(fromId, toId, amount);
        // Atualiza saldos também em memória se existirem
        try {
            BeneficioDto from = get(fromId);
            BeneficioDto to = get(toId);
            if (from.getValor() != null && amount != null) {
                from.setValor(from.getValor().subtract(amount));
            }
            if (to.getValor() != null && amount != null) {
                to.setValor(to.getValor().add(amount));
            }
        } catch (NoSuchElementException ignore) {
            // se não existir no repositório em memória, ignoramos aqui
        }
    }
}
