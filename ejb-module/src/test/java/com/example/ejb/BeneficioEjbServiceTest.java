package com.example.ejb;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BeneficioEjbServiceTest {

    private EntityManager em;
    private BeneficioEjbService service;

    @BeforeEach
    void setUp() throws Exception {
        em = mock(EntityManager.class);
        service = new BeneficioEjbService();
        // injetar EntityManager por reflexão, já que @PersistenceContext não é resolvido em teste unitário
        var field = BeneficioEjbService.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(service, em);
    }

    private Beneficio novoBeneficio(Long id, BigDecimal valor, boolean ativo) {
        Beneficio b = new Beneficio();
        b.setId(id);
        b.setValor(valor);
        b.setAtivo(ativo);
        return b;
    }

    @Test
    void transfer_sucesso_atualiza_saldos_e_persiste() {
        Beneficio from = novoBeneficio(1L, new BigDecimal("100.00"), true);
        Beneficio to = novoBeneficio(2L, new BigDecimal("10.00"), true);

        when(em.find(Beneficio.class, 1L, LockModeType.OPTIMISTIC)).thenReturn(from);
        when(em.find(Beneficio.class, 2L, LockModeType.OPTIMISTIC)).thenReturn(to);

        service.transfer(1L, 2L, new BigDecimal("25.00"));

        assertEquals(new BigDecimal("75.00"), from.getValor());
        assertEquals(new BigDecimal("35.00"), to.getValor());

        verify(em, times(1)).merge(from);
        verify(em, times(1)).merge(to);
        verify(em, times(1)).flush();
    }

    @Test
    void transfer_ids_iguais_dispara_excecao() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(1L, 1L, new BigDecimal("1")));
        assertTrue(ex.getMessage().contains("Origem e destino"));
        verify(em, never()).merge(any());
    }

    @Test
    void transfer_valor_invalido_dispara_excecao() {
        assertThrows(IllegalArgumentException.class, () -> service.transfer(1L, 2L, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> service.transfer(1L, 2L, new BigDecimal("-1")));
        verify(em, never()).merge(any());
    }

    @Test
    void transfer_beneficio_inativo_dispara_excecao() {
        Beneficio from = novoBeneficio(1L, new BigDecimal("100"), false);
        Beneficio to = novoBeneficio(2L, new BigDecimal("0"), true);
        when(em.find(Beneficio.class, 1L, LockModeType.OPTIMISTIC)).thenReturn(from);
        when(em.find(Beneficio.class, 2L, LockModeType.OPTIMISTIC)).thenReturn(to);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("10")));
        assertTrue(ex.getMessage().contains("inativo"));
    }

    @Test
    void transfer_saldo_insuficiente_dispara_excecao() {
        Beneficio from = novoBeneficio(1L, new BigDecimal("5"), true);
        Beneficio to = novoBeneficio(2L, new BigDecimal("0"), true);
        when(em.find(Beneficio.class, 1L, LockModeType.OPTIMISTIC)).thenReturn(from);
        when(em.find(Beneficio.class, 2L, LockModeType.OPTIMISTIC)).thenReturn(to);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("10")));
        assertTrue(ex.getMessage().contains("Saldo insuficiente"));
    }
}
