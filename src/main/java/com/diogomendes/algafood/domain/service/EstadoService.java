package com.diogomendes.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.diogomendes.algafood.domain.exception.EntidadeEmUsoException;
import com.diogomendes.algafood.domain.exception.EstadoNaoEncontradaException;
import com.diogomendes.algafood.domain.model.Estado;
import com.diogomendes.algafood.domain.repository.EstadoRepository;

/**
 * Classe de serviço de {@link Estado}.
 * 
 * @author didam
 *
 */
@Service
public class EstadoService {

	@Autowired
	private EstadoRepository estadoRepository;

	/**
	 * Salva uma instância de {@link Estado}.
	 * 
	 * @param estado
	 * @return
	 */
	public Estado salvar(Estado estado) {
		return estadoRepository.save(estado);
	}

	/**
	 * Remove uma instância de {@link Estado}.
	 * 
	 * @param id
	 */
	public void remover(Long id) {
		try {
			estadoRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EstadoNaoEncontradaException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Cozinha de código %d não pode ser removida, pois está em uso", id));
		}

	}

	/**
	 * Busca uma instância de {@link Estado} de acordo com id informado.
	 * 
	 * @param id
	 * @return
	 */
	public Estado buscarEstado(Long id) {
		return estadoRepository.findById(id).orElseThrow(() -> new EstadoNaoEncontradaException(id));
	}

}
