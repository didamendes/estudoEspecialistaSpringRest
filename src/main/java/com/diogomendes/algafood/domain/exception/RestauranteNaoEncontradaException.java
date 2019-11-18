package com.diogomendes.algafood.domain.exception;

import com.diogomendes.algafood.domain.model.Restaurante;

/**
 * Classe de Exception de {@link Restaurante}.
 * 
 * @author didam
 *
 */
public class RestauranteNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 6933410856472025668L;

	public RestauranteNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public RestauranteNaoEncontradaException(Long id) {
		this(String.format("Não existe um cadastro de restaurante com código %d", id));
	}

}
