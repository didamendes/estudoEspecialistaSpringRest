package com.diogomendes.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.diogomendes.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.diogomendes.algafood.domain.exception.EntidadeEmUsoException;
import com.diogomendes.algafood.domain.model.Cozinha;
import com.diogomendes.algafood.domain.repository.CozinhaRepository;

/**
 * Classe de serviço de {@link Cozinha}.
 * 
 * @author didam
 *
 */
@Service
public class CozinhaService {

	private static final String MSG_EM_USO = "Cozinha de código %d não pode ser removida, pois está em uso";

	@Autowired
	private CozinhaRepository cozinhaRepository;

	/**
	 * Salva uma instância de {@link Cozinha}.
	 * 
	 * @param cozinha
	 * @return
	 */
	public Cozinha salvar(Cozinha cozinha) {
		return cozinhaRepository.save(cozinha);
	}

	/**
	 * Remove uma instância de {@link Cozinha}.
	 * 
	 * @param id
	 */
	public void remover(Long id) {
		try {
			cozinhaRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new CozinhaNaoEncontradaException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_EM_USO, id));
		}
	}

	/**
	 * Busca uma instância de {@link Cozinha} de acordo com id informado.
	 * 
	 * @param id
	 * @return
	 */
	public Cozinha buscarCozinha(Long id) {
		return cozinhaRepository.findById(id).orElseThrow(() -> new CozinhaNaoEncontradaException(id));
	}

}
