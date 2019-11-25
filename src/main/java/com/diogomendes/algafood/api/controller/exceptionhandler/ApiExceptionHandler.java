package com.diogomendes.algafood.api.controller.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.diogomendes.algafood.core.validation.ValidacaoException;
import com.diogomendes.algafood.domain.exception.EntidadeEmUsoException;
import com.diogomendes.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.diogomendes.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
			+ "o problema persistir, entre em contato com o administrador do sistema.";

	public static final String MSG_DADOS_INVALIDOS = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = MSG_DADOS_INVALIDOS;

		BindingResult bindingResult = ex.getBindingResult();

		List<Problem.Object> fieldsErrors = fieldsErros(bindingResult);

		Problem problem = Problem(status, problemType, detail, fieldsErrors);

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	/**
	 * Tratar os erros de {@link ValidacaoException}.
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity<Object> handleValidacaoException(ValidacaoException ex, WebRequest request) {
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = MSG_DADOS_INVALIDOS;

		BindingResult bindingResult = ex.getBindingResult();

		List<Problem.Object> fieldsErrors = fieldsErros(bindingResult);

		Problem problem = createProblemBuilder(HttpStatus.BAD_REQUEST, problemType, detail).userMessage(detail)
				.objects(fieldsErrors).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * Tratar os erros de {@link Exception}.
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

		ex.printStackTrace();

		Problem problem = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	/**
	 * Tratar o erro de {@link NoHandlerFoundException}.
	 * 
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = String.format("O recurso '%s', que você tentou acessar, é inexistente. ", ex.getRequestURL());

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	/**
	 * Tratar o erro de {@link TypeMismatchException}.
	 * 
	 */
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}

	/**
	 * Tratar o erro {@link HttpMessageNotReadableException}.
	 * 
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
		}

		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problem, headers, HttpStatus.NOT_FOUND, request);
	}

	/**
	 * Exception Handler da classe de {@link EntidadeNaoEncontradaException}.
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradoException(EntidadeNaoEncontradaException ex,
			WebRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	/**
	 * Exception Handler da classe de {@link NegocioException}.
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * Exception Handler da classe de {@link EntidadeEmUsoException}.
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {

		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String detail = ex.getMessage();

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

	/**
	 * Implementação do HandleExceptionInternal.
	 * 
	 * @param ex
	 * @param body
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {
			body = Problem.builder().title(status.getReasonPhrase()).status(status.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).timestamp(LocalDateTime.now()).build();
		} else if (body instanceof String) {
			body = Problem.builder().title((String) body).status(status.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).timestamp(LocalDateTime.now()).build();
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	/**
	 * Método que criar o {@link ProcessBuilder}.
	 * 
	 * @param status
	 * @param problemType
	 * @param detail
	 * @return
	 */
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
		return Problem.builder().timestamp(LocalDateTime.now()).status(status.value()).type(problemType.getUri())
				.title(problemType.getTitle()).detail(detail);
	}

	/**
	 * Tratar o erro {@link MethodArgumentTypeMismatchException}.
	 * 
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

		String detail = String.format(
				"O parâmetro de URL '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	/**
	 * Tratar os erro {@link PropertyBindingException}.
	 * 
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' não é permitida ! ", path);

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	/**
	 * Tratar os error {@link InvalidFormatException}.
	 * 
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format(
				"A propriedade '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());

		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	/**
	 * Método responsavel de retorna o Problem para os
	 * {@link MethodArgumentNotValidException} e {@link ValidacaoException}.
	 * 
	 * @param status
	 * @param problemType
	 * @param detail
	 * @param fieldsErrors
	 * @return
	 */
	private Problem Problem(HttpStatus status, ProblemType problemType, String detail,
			List<Problem.Object> fieldsErrors) {
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).objects(fieldsErrors)
				.build();
		return problem;
	}

	/**
	 * Método responsavel por montar o {@link Problem.Object} para
	 * {@link MethodArgumentNotValidException} e {@link ValidacaoException}
	 * 
	 * @param bindingResult
	 * @return
	 */
	private List<Problem.Object> fieldsErros(BindingResult bindingResult) {
		List<Problem.Object> fieldsErrors = bindingResult.getAllErrors().stream().map(objectError -> {
			String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

			String name = objectError.getObjectName();

			if (objectError instanceof FieldError) {
				name = ((FieldError) objectError).getField();
			}

			return Problem.Object.builder().name(name).userMessage(message).build();
		}).collect(Collectors.toList());
		return fieldsErrors;
	}

}
