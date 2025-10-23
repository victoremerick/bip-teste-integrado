package com.example.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;

@Stateless
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        // Validações básicas
        if (fromId == null || toId == null) {
            throw new IllegalArgumentException("IDs de origem e destino são obrigatórios");
        }
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Origem e destino não podem ser o mesmo benefício");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo");
        }

        // Carrega com locking otimista para evitar lost updates; versões serão verificadas no commit
        Beneficio from = em.find(Beneficio.class, fromId, LockModeType.OPTIMISTIC);
        Beneficio to   = em.find(Beneficio.class, toId, LockModeType.OPTIMISTIC);

        if (from == null || to == null) {
            throw new IllegalArgumentException("Benefício de origem ou destino não encontrado");
        }
        if (Boolean.FALSE.equals(from.getAtivo()) || Boolean.FALSE.equals(to.getAtivo())) {
            throw new IllegalStateException("Benefício inativo não pode participar de transferência");
        }

        BigDecimal saldoOrigem = from.getValor();
        if (saldoOrigem == null) {
            saldoOrigem = BigDecimal.ZERO;
        }
        if (saldoOrigem.compareTo(amount) < 0) {
            throw new IllegalStateException("Saldo insuficiente para transferência");
        }

        // Efetiva a transferência
        from.setValor(saldoOrigem.subtract(amount));
        BigDecimal destinoAtual = to.getValor() == null ? BigDecimal.ZERO : to.getValor();
        to.setValor(destinoAtual.add(amount));

        // merge opcional, pois entidades gerenciadas são sincronizadas automaticamente
        em.merge(from);
        em.merge(to);
        // flush para antecipar possíveis OptimisticLockException dentro da transação
        em.flush();
    }
}
