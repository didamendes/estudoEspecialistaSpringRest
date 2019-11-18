package com.diogomendes.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diogomendes.algafood.domain.model.FormaPagamento;

/**
 * Classe de Repository de {@link FormaPagamento}.
 * 
 * @author didam
 *
 */
@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

}
