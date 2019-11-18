package com.diogomendes.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diogomendes.algafood.domain.model.Estado;

/**
 * Classe de Repository de {@link Estado}.
 * 
 * @author didam
 *
 */
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

}
