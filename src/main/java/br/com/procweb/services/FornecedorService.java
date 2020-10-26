package br.com.procweb.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.procweb.models.Fornecedor;
import br.com.procweb.models.enums.TipoLog;
import br.com.procweb.repositories.FornecedorRepository;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Service
public class FornecedorService {

	@Autowired
	private FornecedorRepository fornecedorRepository;
	@Autowired
	private LogService logService;
	private static final String ENTIDADE = "FORNECEDOR";

	public Fornecedor salvar(@Valid Fornecedor fornecedor) {
		try {
			Fornecedor forn = this.fornecedorRepository.save(fornecedor);
			this.logService.insereLog(TipoLog.INSERCAO, forn.getId(), ENTIDADE);
			return forn;
		} catch (ValidationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "erro de validação!",
					e.getCause());
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "fornecedor já cadastrado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Fornecedor atualizar(Integer id, Fornecedor fornecedor) {
		try {
			Fornecedor forn = this.fornecedorRepository.findById(id).map(novo -> {
				novo.setFantasia(fornecedor.getFantasia());
				novo.setRazaoSocial(fornecedor.getRazaoSocial());
				novo.setCnpj(fornecedor.getCnpj());
				novo.setEmail(fornecedor.getEmail());
				novo.setFones(fornecedor.getFones());
				novo.setEndereco(fornecedor.getEndereco());
				return this.salvar(novo);
			}).orElseThrow(() -> new EntityNotFoundException());
			return forn;
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "fornecedor não localizado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Fornecedor buscar(Integer id) {
		try {
			return this.fornecedorRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException());
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "fornecedor não localizado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Page<Fornecedor> listar(int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.ASC, "fantasia");
			return this.fornecedorRepository.findAll(pageable);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public List<Fornecedor> listar() {
		try {
			return this.fornecedorRepository.findAll(Sort.by(Order.asc("fantasia")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Page<Fornecedor> listarPorParametro(String parametro, int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.ASC, "fantasia");
			if (Character.isDigit(parametro.charAt(0)))
				return this.fornecedorRepository.findAllByCnpjContaining(parametro, pageable);
			else
				return this.fornecedorRepository
						.findAllByFantasiaContainingIgnoreCaseOrRazaoSocialContainingIgnoreCase(
								parametro, parametro, pageable);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public void excluir(Integer id) {
		try {
			this.logService.insereLog(TipoLog.EXCLUSAO, id, ENTIDADE);
			this.fornecedorRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"não é possível excluir o fornecedor!", e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public boolean fornecedorExiste(String fantasia) {
		try {
			Fornecedor forn = this.fornecedorRepository.findByFantasia(fantasia).orElse(null);
			return forn != null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

}
