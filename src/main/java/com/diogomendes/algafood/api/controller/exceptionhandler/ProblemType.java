package com.diogomendes.algafood.api.controller.exceptionhandler;

import lombok.Getter;

/**
 * Classe enum de {@link ProblemType}.
 * 
 * @author didam
 *
 */
@Getter
public enum ProblemType {

	ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não econtrada"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");

	private String title;
	private String uri;

	/**
	 * Construtor de {@link ProblemType}.
	 * 
	 * @param path
	 * @param title
	 */
	private ProblemType(String path, String title) {
		this.uri = "https://algafood.com.br" + path;
		this.title = title;
	}

}
