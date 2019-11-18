package com.diogomendes.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diogomendes.algafood.domain.exception.RestauranteNaoEncontradaException;
import com.diogomendes.algafood.domain.model.Cozinha;
import com.diogomendes.algafood.domain.model.Restaurante;
import com.diogomendes.algafood.domain.repository.RestauranteRepository;

/**
 * Classe de serviço de {@link Restaurante}.
 * 
 * @author didam
 *
 */
@Service
public class RestauranteService {

	@Autowired
	private CozinhaService cozinhaService;

	@Autowired
	private RestauranteRepository restauranteRepository;

	/**
	 * Salva uma instância de {@link Restaurante}.
	 * 
	 * @param restaurante
	 * @return
	 */
	public Restaurante salvar(Restaurante restaurante) {
		Long idCozinha = restaurante.getCozinha().getId();
		Cozinha cozinha = cozinhaService.buscarCozinha(idCozinha);

		restaurante.setCozinha(cozinha);
		return restauranteRepository.save(restaurante);
	}

	/**
	 * Busca uma instância de {@link Restaurante} de acordo com o id informado.
	 * 
	 * @param id
	 * @return
	 */
	public Restaurante buscarRestaurante(Long id) {
		return restauranteRepository.findById(id).orElseThrow(() -> new RestauranteNaoEncontradaException(id));
	}

}
