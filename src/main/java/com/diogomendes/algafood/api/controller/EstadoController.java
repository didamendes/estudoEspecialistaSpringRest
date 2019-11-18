package com.diogomendes.algafood.api.controller;

import java.net.URI;
import java.util.List;

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

import com.diogomendes.algafood.domain.model.Estado;
import com.diogomendes.algafood.domain.repository.EstadoRepository;
import com.diogomendes.algafood.domain.service.EstadoService;

/**
 * Classe de controller de {@link Estado}.
 * 
 * @author didam
 *
 */
@RestController
@RequestMapping("/estados")
public class EstadoController {

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private EstadoRepository estadoRepository;

	/**
	 * Retorna uma lista de {@link Estado}.
	 * 
	 * @return
	 */
	@GetMapping
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}

	/**
	 * Retorna uma instância de {@link Estado} de acordo com id informado.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Estado buscar(@PathVariable Long id) {
		return estadoService.buscarEstado(id);
	}

	/**
	 * Salva uma instância de {@link Estado}.
	 * 
	 * @param estado
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody Estado estado) {
		Estado estadoSalvo = estadoService.salvar(estado);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(estadoSalvo.getId())
				.toUri();
		return ResponseEntity.created(uri).body(estadoSalvo);
	}

	/**
	 * Alterar uma instância de {@link Estado}.
	 * 
	 * @param id
	 * @param estado
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> alterar(@PathVariable Long id, @RequestBody Estado estado) {
		Estado estadoVigente = estadoService.buscarEstado(id);

		BeanUtils.copyProperties(estado, estadoVigente, "id");
		Estado estadoSalvo = estadoService.salvar(estadoVigente);
		return ResponseEntity.ok(estadoSalvo);
	}

	/**
	 * Remove ums instância de {@link Estado}.
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		estadoService.remover(id);
		return ResponseEntity.noContent().build();
	}

}
