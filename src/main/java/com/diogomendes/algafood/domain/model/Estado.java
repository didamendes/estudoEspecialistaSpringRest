package com.diogomendes.algafood.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.diogomendes.algafood.core.validation.Groups;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Classe de modelo de {@link Estado}.
 * 
 * @author didam
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Estado {
	
	@Id
	@NotNull(groups = Groups.EstadoId.class)
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(nullable = false)
	private String nome;

}
