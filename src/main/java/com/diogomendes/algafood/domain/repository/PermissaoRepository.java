package com.diogomendes.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diogomendes.algafood.domain.model.Permissao;

/**
 * Classe de Repository de {@link Permissao}.
 * 
 * @author didam
 *
 */
@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

}
