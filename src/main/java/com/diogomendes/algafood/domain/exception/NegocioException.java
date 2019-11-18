package com.diogomendes.algafood.domain.exception;

/**
 * Classe de Exception de Negocio Exception.
 * 
 * @author didam
 *
 */
public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = 6933410856472025668L;

	public NegocioException(String mensagem) {
		super(mensagem);
	}

	public NegocioException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}

}
