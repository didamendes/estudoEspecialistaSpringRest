package com.diogomendes.algafood.domain.exception;

import com.diogomendes.algafood.domain.model.Estado;

/**
 * Classe de Exception de {@link Estado}.
 * 
 * @author didam
 *
 */
public class EstadoNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 6933410856472025668L;

	public EstadoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public EstadoNaoEncontradaException(Long id) {
		this(String.format("Não existe um cadastro de estado com código %d", id));
	}

}
