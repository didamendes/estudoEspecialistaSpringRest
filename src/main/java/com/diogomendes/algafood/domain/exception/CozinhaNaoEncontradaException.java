package com.diogomendes.algafood.domain.exception;

import com.diogomendes.algafood.domain.model.Cozinha;

/**
 * Classe de Exception de {@link Cozinha}.
 * 
 * @author didam
 *
 */
public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 6933410856472025668L;

	public CozinhaNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public CozinhaNaoEncontradaException(Long id) {
		this(String.format("Não existe um cadastro de cozinha com código %d", id));
	}

}
