package com.diogomendes.algafood.api.controller;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.diogomendes.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.diogomendes.algafood.domain.exception.NegocioException;
import com.diogomendes.algafood.domain.model.Restaurante;
import com.diogomendes.algafood.domain.repository.RestauranteRepository;
import com.diogomendes.algafood.domain.service.RestauranteService;
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
		return restauranteService.buscarRestaurante(id);
	}

	/**
	 * Salva uma instância de {@link Restaurante}.
	 * 
	 * @param restaurante
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody Restaurante restaurante) {
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
	public ResponseEntity<?> alterar(@PathVariable Long id, @RequestBody Restaurante restaurante) {
		Restaurante restauranteAtual = restauranteService.buscarRestaurante(id);

		BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro");

		try {
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
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,
			@RequestBody Map<String, Object> campos) {
		Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);

		if (restauranteAtual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		merge(campos, restauranteAtual.get());

		return alterar(restauranteId, restauranteAtual.get());
	}

	/**
	 * Método responsavel de fazer merge com os dados de origem com a instância que
	 * será alterado.
	 * 
	 * @param dadosOrigem
	 * @param restauranteDestino
	 */
	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
		ObjectMapper objectMapper = new ObjectMapper();
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			field.setAccessible(true);

			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

//			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);

			ReflectionUtils.setField(field, restauranteDestino, novoValor);
		});
	}

}
