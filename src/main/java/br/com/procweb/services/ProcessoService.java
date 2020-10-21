package br.com.procweb.services;

import java.time.LocalDate;
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

import br.com.procweb.models.Processo;
import br.com.procweb.models.dto.ProcessoDto;
import br.com.procweb.models.enums.Situacao;
import br.com.procweb.models.enums.TipoLog;
import br.com.procweb.models.forms.ProcessoForm;
import br.com.procweb.repositories.ProcessoRepository;
import br.com.procweb.utils.GeradorAutos;
import br.com.procweb.utils.ProcessoUtils;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Service
public class ProcessoService {

	@Autowired
	private ProcessoRepository processoRepository;
	@Autowired
	private LogService logService;
	@Autowired
	private ConsumidorService consumidorService;
	@Autowired
	private FornecedorService fornecedorService;
	private static final String ENTIDADE = "PROCESSO";

	public ProcessoDto salvar(@Valid ProcessoForm processo) {
		try {
			if (processo.getAutos() == null) {
				processo.setAutos(GeradorAutos.getAutos(this.processoRepository.findAll(),
						processo.getData().getYear()));
				processo.setSituacao(ProcessoUtils.handleSituacao(new ArrayList<>()));
			}
			Processo proc = this.processoRepository
					.save(processo.converter(this.consumidorService, this.fornecedorService));
			this.logService.insereLog(TipoLog.INSERCAO, proc.getId(), ENTIDADE);
			return new ProcessoDto(proc);
		} catch (ValidationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "erro de validação!",
					e.getCause());
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "processo já cadastrado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public ProcessoDto atualizar(Integer id, @Valid ProcessoForm processo) {
		try {
			return this.processoRepository.findById(id).map(novo -> {
				Processo proc = processo.converter(this.consumidorService, this.fornecedorService);
				novo.setAutos(proc.getAutos());
				novo.setData(proc.getData());
				novo.setConsumidores(proc.getConsumidores());
				novo.setRepresentantes(proc.getRepresentantes());
				novo.setFornecedores(proc.getFornecedores());
				novo.setRelato(proc.getRelato());
				novo.setMovimentacao(proc.getMovimentacao());
				novo.setSituacao(ProcessoUtils.handleSituacao(novo.getMovimentacao()));
				novo.setTipo(proc.getTipo());
				return new ProcessoDto(this.processoRepository.save(novo));
			}).orElseThrow(() -> new EntityNotFoundException());
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "processo não localizado!",
					e.getCause());
		} catch (ValidationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "erro de validação!",
					e.getCause());
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "processo já cadastrado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Processo atualizar(Integer id, Processo processo) {
		try {
			return this.processoRepository.findById(id).map(novo -> {
				novo = processo;
				return this.processoRepository.save(novo);
			}).orElseThrow(() -> new EntityNotFoundException());
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "processo não localizado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Processo buscar(Integer id) {
		try {
			return this.processoRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException());
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "erro de validação!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Page<ProcessoDto> listar(int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.DESC, "data");
			Page<Processo> page = this.processoRepository.findAll(pageable);
			return transformaDto(pageable, page);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	private Page<ProcessoDto> transformaDto(Pageable pageable, Page<Processo> page) {
		List<ProcessoDto> dtos = new ArrayList<>();
		page.getContent().forEach(p -> dtos.add(new ProcessoDto(p)));
		return new PageImpl<>(dtos, pageable, page.getTotalElements());
	}

	public Page<ProcessoDto> listarPorAutos(String autos, int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.DESC, "data");
			Page<Processo> page = this.processoRepository.findAllByAutosContaining(autos, pageable);
			return transformaDto(pageable, page);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Page<ProcessoDto> listarPorConsumidor(String param, int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.DESC, "data");
			Page<Processo> page = this.processoRepository
					.findAllByConsumidoresDenominacaoContainingIgnoreCaseOrConsumidoresCadastroContaining(
							param, param, pageable);
			return transformaDto(pageable, page);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Page<ProcessoDto> listarPorFornecedor(String param, int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.DESC, "data");
			Page<Processo> page = this.processoRepository
					.findAllByFornecedoresFantasiaContainingIgnoreCaseOrFornecedoresRazaoSocialContainingIgnoreCase(
							param, param, pageable);
			return transformaDto(pageable, page);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Page<ProcessoDto> listarPorSituacao(Situacao situacao, int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.DESC, "data");
			Page<Processo> page = this.processoRepository.findAllBySituacao(situacao, pageable);
			return transformaDto(pageable, page);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public void excluir(Integer id) {
		try {
			this.logService.insereLog(TipoLog.EXCLUSAO, id, ENTIDADE);
			this.processoRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"não é possível excluir o processo!", e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public String getAutos(LocalDate data) {
		try {
			LocalDate inicio = LocalDate.of(data.getYear(), 1, 1);
			LocalDate fim = LocalDate.of(data.getYear(), 12, 31);
			return GeradorAutos.getAutos(this.processoRepository.findAllByDataBetween(inicio, fim),
					data.getYear());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

}
