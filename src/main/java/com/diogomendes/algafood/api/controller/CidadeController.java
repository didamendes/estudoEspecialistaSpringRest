package com.diogomendes.algafood.api.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.diogomendes.algafood.domain.exception.EstadoNaoEncontradaException;
import com.diogomendes.algafood.domain.exception.NegocioException;
import com.diogomendes.algafood.domain.model.Cidade;
import com.diogomendes.algafood.domain.repository.CidadeRepository;
import com.diogomendes.algafood.domain.service.CidadeService;

/**
 * Classe de controller de {@link Cidade}.
 * 
 * @author didam
 *
 */
@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private CidadeRepository cidadeRepository;

	/**
	 * Retorna uma lista de {@link Cidade}.
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> listar() {
		List<Cidade> cidades = cidadeRepository.findAll();
		return ResponseEntity.ok(cidades);
	}

	/**
	 * Retorna uma instância de {@link Cidade} de acordo com o id informado.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Cidade buscar(@PathVariable Long id) {
		return cidadeService.buscarCidade(id);
	}

	/**
	 * Salva uma instância de {@link Cidade}.
	 * 
	 * @param cidade
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody @Valid Cidade cidade) {

		try {
			Cidade cidadeSalva = cidadeService.salvar(cidade);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
					.buildAndExpand(cidadeSalva.getId()).toUri();
			return ResponseEntity.created(uri).body(cidadeSalva);
		} catch (EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	/**
	 * Alterar uma instância de {@link Cidade}.
	 * 
	 * @param id
	 * @param cidade
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> alterar(@PathVariable Long id, @RequestBody @Valid Cidade cidade) {
		try {
			Cidade cidadeVigente = cidadeService.buscarCidade(id);
			BeanUtils.copyProperties(cidade, cidadeVigente, "id");

			Cidade cidadeSalva = cidadeService.salvar(cidadeVigente);
			return ResponseEntity.ok(cidadeSalva);
		} catch (EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	/**
	 * Remove uma instância de {@link Cidade}.
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		cidadeService.remover(id);
		return ResponseEntity.noContent().build();
	}

}
