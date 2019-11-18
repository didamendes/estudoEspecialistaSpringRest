package com.diogomendes.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diogomendes.algafood.domain.model.Restaurante;

/**
 * Classe de Repository de {@link Restaurante}.
 * 
 * @author didam
 *
 */
@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

}
