package com.diogomendes.algafood.core.validation;

import org.springframework.validation.BindingResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValidacaoException extends RuntimeException {
	 
	private static final long serialVersionUID = -3909287598311965514L;
	
	private BindingResult bindingResult;

}
