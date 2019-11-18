package com.diogomendes.algafood.domain.exception;

/**
 * Classe Abstract de Exception de Entidade Nao Encontrada.
 * 
 * @author didam
 *
 */
public abstract class EntidadeNaoEncontradaException extends NegocioException {

	private static final long serialVersionUID = 6933410856472025668L;

	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

}
