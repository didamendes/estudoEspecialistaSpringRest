package com.diogomendes.algafood.api.controller;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.diogomendes.algafood.core.validation.ValidacaoException;
import com.diogomendes.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.diogomendes.algafood.domain.exception.NegocioException;
import com.diogomendes.algafood.domain.model.Restaurante;
import com.diogomendes.algafood.domain.repository.RestauranteRepository;
import com.diogomendes.algafood.domain.service.RestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe de controller de {@link Restaurante}.
 * 
 * @author didam
 *
 */
@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteService restauranteService;

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private SmartValidator validator;

	/**
	 * Retorna uma lista de {@link Restaurante}.
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> listar() {
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		return ResponseEntity.ok(restaurantes);
	}

	/**
	 * Retorna uma instância de {@link Restaurante} de acordo com id informado.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Restaurante buscar(@PathVariable Long id) {
		/*
		 * if (true) { throw new IllegalArgumentException("teste"); }
		 */
		return restauranteService.buscarRestaurante(id);
	}

	/**
	 * Salva uma instância de {@link Restaurante}.
	 * 
	 * @param restaurante
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> salvar(@Valid @RequestBody Restaurante restaurante) {
		try {
			restaurante = restauranteService.salvar(restaurante);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
					.buildAndExpand(restaurante.getId()).toUri();
			return ResponseEntity.created(uri).body(restaurante);
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	/**
	 * Alterar uma instância de {@link Restaurante}.
	 * 
	 * @param id
	 * @param restaurante
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> alterar(@PathVariable(required = true) Long id,
			@RequestBody @Valid Restaurante restaurante) {
		try {
			Restaurante restauranteAtual = restauranteService.buscarRestaurante(id);

			BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco",
					"dataCadastro");

			Restaurante restauranteSalvo = restauranteService.salvar(restauranteAtual);
			return ResponseEntity.ok(restauranteSalvo);
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	/**
	 * Alterar uma instância de {@link Restaurante} utilizando 'PATCH'.
	 * 
	 * @param restauranteId
	 * @param campos
	 * @return
	 */
	@PatchMapping("/{restauranteId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos,
			HttpServletRequest request) {
		Restaurante restauranteAtual = restauranteService.buscarRestaurante(restauranteId);

		merge(campos, restauranteAtual, request);
		validate(restauranteAtual, "restaurante");

		return alterar(restauranteId, restauranteAtual);
	}

	/**
	 * Valida os campos do tipo Patch.
	 * 
	 * @param restauranteAtual
	 * @param string
	 */
	private void validate(Restaurante restaurante, String objectName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
		validator.validate(restaurante, bindingResult);
		
		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
	}

	/**
	 * Método responsavel de fazer merge com os dados de origem com a instância que
	 * será alterado.
	 * 
	 * @param dadosOrigem
	 * @param restauranteDestino
	 */
	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {

		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);

				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}

}
