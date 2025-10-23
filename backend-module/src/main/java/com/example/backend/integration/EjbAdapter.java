package com.example.backend.integration;

import com.example.ejb.BeneficioEjbService;

import java.math.BigDecimal;

/**
 * Adaptador simples para integrar com o serviço EJB.
 * Observação: sem um container EJB/JPA, o EntityManager do EJB não estará disponível em runtime.
 * Este adaptador existe para demonstrar a integração e permitir compilação entre módulos.
 */
public class EjbAdapter {
    // Instância simples; em um ambiente real, seria injetado via @EJB ou localizado via JNDI
    private final BeneficioEjbService ejbService = new BeneficioEjbService();

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        // Em ambiente sem container, esta chamada pode lançar NPE por faltar EntityManager.
        // Ainda assim, mantemos a assinatura para evidenciar a integração.
        ejbService.transfer(fromId, toId, amount);
    }
}
