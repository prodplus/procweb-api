package br.com.procweb.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.procweb.models.Atendimento;
import br.com.procweb.models.dto.AtendimentoDto;
import br.com.procweb.models.enums.TipoLog;
import br.com.procweb.models.forms.AtendimentoForm;
import br.com.procweb.repositories.AtendimentoRepository;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Service
public class AtendimentoService {

	@Autowired
	private AtendimentoRepository atendimentoRepository;
	@Autowired
	private LogService logService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private ConsumidorService consumidorService;
	@Autowired
	private FornecedorService fornecedorService;
	private static final String ENTIDADE = "ATENDIMENTO";

	public AtendimentoDto salvar(@Valid AtendimentoForm atendimento) {
		try {
			AtendimentoDto atd = new AtendimentoDto(
					this.atendimentoRepository.save(atendimento.converter(this.consumidorService,
							this.fornecedorService, this.usuarioService)));
			this.logService.insereLog(TipoLog.INSERCAO, atd.getId(), ENTIDADE);
			return atd;
		} catch (ValidationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "erro de validação!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public AtendimentoDto atualizar(Integer id, @Valid AtendimentoForm atendimento) {
		try {
			return this.atendimentoRepository.findById(id).map(novo -> {
				Atendimento i = atendimento.converter(this.consumidorService,
						this.fornecedorService, this.usuarioService);
				novo.setConsumidores(i.getConsumidores());
				novo.setFornecedores(i.getFornecedores());
				novo.setData(i.getData());
				novo.setRelato(i.getRelato());
				novo.setAtendente(i.getAtendente());
				this.logService.insereLog(TipoLog.ATUALIZACAO, id, ENTIDADE);
				return new AtendimentoDto(this.atendimentoRepository.save(novo));
			}).orElseThrow(() -> new EntityNotFoundException());
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "atendimento não localizado!",
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

	public Atendimento buscar(Integer id) {
		try {
			return this.atendimentoRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException());
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "atendimento não localizado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Page<AtendimentoDto> listar(int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.DESC, "data");
			Page<Atendimento> page = this.atendimentoRepository.findAll(pageable);
			List<AtendimentoDto> lista = new ArrayList<>();
			page.getContent().forEach(a -> lista.add(new AtendimentoDto(a)));
			return new PageImpl<>(lista, pageable, page.getTotalElements());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Page<AtendimentoDto> listarPorParametro(String parametro, int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.DESC, "data");
			List<AtendimentoDto> lista = new ArrayList<>();
			if (Character.isDigit(parametro.charAt(0))) {
				Page<Atendimento> page = this.atendimentoRepository
						.findAllByConsumidoresCadastroOrFornecedoresCnpj(parametro, parametro,
								pageable);
				page.getContent().forEach(a -> lista.add(new AtendimentoDto(a)));
				return new PageImpl<>(lista, pageable, page.getTotalElements());
			} else {
				Page<Atendimento> page = this.atendimentoRepository
						.findAllByConsumidoresDenominacaoContainingIgnoreCaseOrFornecedoresFantasiaContainingIgnoreCaseOrFornecedoresRazaoSocialContainingIgnoreCase(
								parametro, parametro, parametro, pageable);
				page.getContent().forEach(a -> lista.add(new AtendimentoDto(a)));
				return new PageImpl<>(lista, pageable, page.getTotalElements());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public void excluir(Integer id) {
		try {
			this.logService.insereLog(TipoLog.EXCLUSAO, id, ENTIDADE);
			this.atendimentoRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"não é possível excluir o atendimento!", e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

}
