package com.diogomendes.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.diogomendes.algafood.domain.exception.CidadeNaoEncontradaException;
import com.diogomendes.algafood.domain.exception.EntidadeEmUsoException;
import com.diogomendes.algafood.domain.model.Cidade;
import com.diogomendes.algafood.domain.model.Estado;
import com.diogomendes.algafood.domain.repository.CidadeRepository;

/**
 * Classe de serviço de {@link Cidade}.
 * 
 * @author didam
 *
 */
@Service
public class CidadeService {

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private CidadeRepository cidadeRepository;

	/**
	 * Salva uma instância de {@link Cidade}.
	 * 
	 * @param cidade
	 * @return
	 */
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		Estado estado = estadoService.buscarEstado(estadoId);

		cidade.setEstado(estado);
		return cidadeRepository.save(cidade);
	}

	/**
	 * Remove uma instância de {@link Cidade}.
	 * 
	 * @param id
	 */
	public void remover(Long id) {
		try {
			cidadeRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new CidadeNaoEncontradaException(id);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Cozinha de código %d não pode ser removida, pois está em uso", id));
		}
	}

	/**
	 * Busca uma instância de {@link Cidade} de acordo com id informado.
	 * 
	 * @param id
	 * @return
	 */
	public Cidade buscarCidade(Long id) {
		return cidadeRepository.findById(id).orElseThrow(() -> new CidadeNaoEncontradaException(id));
	}

}
