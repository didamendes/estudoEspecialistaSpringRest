package com.diogomendes.algafood.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.diogomendes.algafood.core.validation.Groups;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Classe de modelo de {@link Cidade}.
 * 
 * @author didam
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cidade {

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String nome;

	@Valid
	@ConvertGroup(from = Default.class, to = Groups.EstadoId.class)
	@ManyToOne
	@JoinColumn(name = "estado_id")
	private Estado estado;

}
