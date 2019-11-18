package com.diogomendes.algafood.domain.exception;

/**
 * Classe de Exception de Entidade Em Uso.
 * 
 * @author didam
 *
 */
public class EntidadeEmUsoException extends RuntimeException {

	private static final long serialVersionUID = -2157676115641423032L;

	public EntidadeEmUsoException(String mensagem) {
		super(mensagem);
	}

}
