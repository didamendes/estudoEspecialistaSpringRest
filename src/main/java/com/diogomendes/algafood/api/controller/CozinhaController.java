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

import com.diogomendes.algafood.domain.model.Cozinha;
import com.diogomendes.algafood.domain.repository.CozinhaRepository;
import com.diogomendes.algafood.domain.service.CozinhaService;

/**
 * Classe de controller de {@link Cozinha}.
 * 
 * @author didam
 *
 */
@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaService cozinhaService;

	@Autowired
	private CozinhaRepository cozinhaRepository;

	/**
	 * Retorna uma lista de {@link Cozinha}.
	 * 
	 * @return
	 */
	@GetMapping
	public List<Cozinha> listar() {
		return cozinhaRepository.findAll();
	}

	/**
	 * Retorna uma instância de {@link Cozinha} de acordo com id informado.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Cozinha buscar(@PathVariable Long id) {
		return cozinhaService.buscarCozinha(id);
	}

	/**
	 * Salva uma instância de {@link Cozinha}.
	 * 
	 * @param cozinha
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> salvar(@Valid @RequestBody Cozinha cozinha) {
		cozinha = cozinhaService.salvar(cozinha);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(cozinha.getId())
				.toUri();
		return ResponseEntity.created(uri).body(cozinha);
	}

	/**
	 * Alterar uma instância de {@link Cozinha}.
	 * 
	 * @param id
	 * @param cozinha
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> alterar(@PathVariable Long id, @RequestBody @Valid Cozinha cozinha) {
		Cozinha cozinhaAtual = cozinhaService.buscarCozinha(id);

		BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
		Cozinha cozinhaSalva = cozinhaService.salvar(cozinhaAtual);
		return ResponseEntity.ok(cozinhaSalva);
	}

	/**
	 * Remove uma instância de {@link Cozinha}.
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		cozinhaService.remover(id);
		return ResponseEntity.noContent().build();
	}

}
