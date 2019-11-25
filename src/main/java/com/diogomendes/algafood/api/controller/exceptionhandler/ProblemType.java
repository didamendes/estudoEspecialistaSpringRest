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

	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parametro invalido"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não econtrado"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível");

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
