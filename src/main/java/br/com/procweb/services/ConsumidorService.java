package br.com.procweb.services;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.procweb.models.Consumidor;
import br.com.procweb.models.enums.TipoLog;
import br.com.procweb.repositories.ConsumidorRepository;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Service
public class ConsumidorService {

	@Autowired
	private ConsumidorRepository consumidorRepository;
	@Autowired
	private LogService logService;
	private static final String ENTIDADE = "CONSUMIDOR";

	public Consumidor salvar(@Valid Consumidor consumidor) {
		try {
			consumidor.setDenominacao(consumidor.getDenominacao().trim());
			Consumidor cons = this.consumidorRepository.save(consumidor);
			this.logService.insereLog(TipoLog.INSERCAO, cons.getId(), ENTIDADE);
			return cons;
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "consumidor já cadastrado!",
					e.getCause());
		} catch (ValidationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "erro de validação!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Consumidor atualizar(Integer id, @Valid Consumidor consumidor) {
		try {
			Consumidor cons = this.consumidorRepository.findById(id).map(novo -> {
				novo.setDenominacao(consumidor.getDenominacao().trim());
				novo.setCadastro(consumidor.getCadastro());
				novo.setEmail(consumidor.getEmail());
				novo.setEndereco(consumidor.getEndereco());
				novo.setFones(consumidor.getFones());
				return this.salvar(novo);
			}).orElseThrow(() -> new EntityNotFoundException());
			return cons;
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "consumidor não localizado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Consumidor buscar(Integer id) {
		try {
			return this.consumidorRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException());
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "consumidor não localizado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Page<Consumidor> listar(int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.ASC, "denominacao");
			return this.consumidorRepository.findAll(pageable);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Page<Consumidor> listarPorParametro(String parametro, int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.ASC, "denominacao");
			if (Character.isDigit(parametro.charAt(0)))
				return this.consumidorRepository.findAllByCadastroContaining(parametro, pageable);
			else
				return this.consumidorRepository.findAllByDenominacaoContainingIgnoreCase(parametro,
						pageable);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public void excluir(Integer id) {
		try {
			this.logService.insereLog(TipoLog.EXCLUSAO, id, ENTIDADE);
			this.consumidorRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"não é possível excluir o consumidor!", e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public boolean consumidorExiste(String cadastro) {
		try {
			Consumidor cons = this.consumidorRepository.findByCadastro(cadastro).orElse(null);
			return cons != null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

}
