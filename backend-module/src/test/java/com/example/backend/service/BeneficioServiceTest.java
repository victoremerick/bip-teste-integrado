package com.example.backend.service;

import com.example.backend.integration.EjbAdapter;
import com.example.backend.model.BeneficioDto;
import com.example.backend.repo.BeneficioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BeneficioServiceTest {

    private BeneficioRepository repo;
    private EjbAdapter ejbAdapter;
    private BeneficioService service;

    @BeforeEach
    void setUp() {
        repo = new BeneficioRepository();
        ejbAdapter = mock(EjbAdapter.class);
        service = new BeneficioService(repo, ejbAdapter);
    }

    private BeneficioDto novo(String nome, BigDecimal valor) {
        BeneficioDto d = new BeneficioDto();
        d.setNome(nome);
        d.setValor(valor);
        d.setAtivo(true);
        return d;
    }

    @Test
    void crud_basico_funciona() {
        // create
        BeneficioDto a = service.create(novo("A", new BigDecimal("10")));
        BeneficioDto b = service.create(novo("B", new BigDecimal("20")));
        assertNotNull(a.getId());
        assertNotNull(b.getId());

        // list
        List<BeneficioDto> todos = service.list();
        assertEquals(2, todos.size());

        // get
        BeneficioDto got = service.get(a.getId());
        assertEquals("A", got.getNome());

        // update
        BeneficioDto novo = novo("A2", new BigDecimal("15"));
        BeneficioDto atualizado = service.update(a.getId(), novo);
        assertEquals("A2", atualizado.getNome());
        assertEquals(new BigDecimal("15"), atualizado.getValor());

        // delete
        service.delete(b.getId());
        assertEquals(1, service.list().size());
    }

    @Test
    void transfer_chama_ejb_e_atualiza_saldos_em_memoria() {
        BeneficioDto from = service.create(novo("FROM", new BigDecimal("100")));
        BeneficioDto to = service.create(novo("TO", new BigDecimal("5")));

        // não lançar exceção do EJB
        doNothing().when(ejbAdapter).transfer(from.getId(), to.getId(), new BigDecimal("25"));

        service.transfer(from.getId(), to.getId(), new BigDecimal("25"));

        assertEquals(new BigDecimal("75"), service.get(from.getId()).getValor());
        assertEquals(new BigDecimal("30"), service.get(to.getId()).getValor());

        verify(ejbAdapter, times(1)).transfer(from.getId(), to.getId(), new BigDecimal("25"));
    }
}
