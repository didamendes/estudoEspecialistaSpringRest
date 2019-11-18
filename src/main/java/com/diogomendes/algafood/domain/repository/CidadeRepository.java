package com.diogomendes.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diogomendes.algafood.domain.model.Cidade;

/**
 * Classe de Repository de {@link Cidade}.
 * 
 * @author didam
 *
 */
@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

}
