package com.diogomendes.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diogomendes.algafood.domain.model.Cozinha;

/**
 * Classe de Repository de {@link Cozinha}.
 * 
 * @author didam
 *
 */
@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {

}
