package com.diogomendes.algafood.domain.exception;

import com.diogomendes.algafood.domain.model.Cidade;

/**
 * Classe de Exception de {@link Cidade}.
 * 
 * @author didam
 *
 */
public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 6933410856472025668L;

	public CidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public CidadeNaoEncontradaException(Long id) {
		this(String.format("Não existe um cadastro de cidade com código %d", id));
	}

}
