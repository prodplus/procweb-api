package br.com.procweb.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

import br.com.procweb.models.Fornecedor;
import br.com.procweb.models.Processo;
import br.com.procweb.models.auxiliares.FornecedorNro;
import br.com.procweb.models.auxiliares.Movimento;
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
			if (processo.getMovimentacao() == null || processo.getMovimentacao().isEmpty()) {
				processo.getMovimentacao().add(new Movimento(processo.getData(), Situacao.BALCAO,
						Situacao.AUTUADO, "recém autuado", null, null));
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
				if (novo.getMovimentacao() == null || novo.getMovimentacao().isEmpty()) {
					novo.getMovimentacao().add(new Movimento(proc.getData(), Situacao.BALCAO,
							Situacao.AUTUADO, "recém autuado", null, null));
					novo.setSituacao(ProcessoUtils.handleSituacao(new ArrayList<>()));
				}
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
				novo.setSituacao(ProcessoUtils.handleSituacao(novo.getMovimentacao()));
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

	public Page<ProcessoDto> listarPorSituacao(Situacao situacao, Situacao situacao2, int pagina,
			int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.DESC, "data");
			Page<Processo> page = this.processoRepository.findAllBySituacaoOrSituacao(situacao,
					situacao2, pageable);
			return transformaDto(pageable, page);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Page<ProcessoDto> listarPorSituacao(Situacao situacao, Situacao situacao2, String autos,
			int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.DESC, "data");
			Page<Processo> page = this.processoRepository
					.findAllBySituacaoOrSituacaoAndAutosContaining(situacao, situacao2, autos,
							pageable);
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

	public List<Processo> listarPorSituacao(Situacao situacao) {
		try {
			return this.processoRepository.findAllBySituacao(situacao);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public List<Processo> listarPorAutosSituacao(String autos, Situacao situacao) {
		try {
			return this.processoRepository.findAllByAutosContainingAndSituacao(autos, situacao);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public List<FornecedorNro> ranking(Integer ano) {
		List<Fornecedor> fornecedores = this.fornecedorService.listar();
		List<FornecedorNro> fornsNro = new ArrayList<>();
		List<Processo> procAno = this.processoRepository
				.findAllByDataBetween(LocalDate.of(ano, 1, 1), LocalDate.of(ano, 12, 31));
		fornecedores.forEach(f -> fornsNro.add(new FornecedorNro(f, 0)));
		for (Processo proc : procAno) {
			for (Fornecedor forn : proc.getFornecedores()) {
				int index = fornsNro.indexOf(new FornecedorNro(forn, 0));
				FornecedorNro fornNro = fornsNro.get(index);
				fornNro.setProcessos(fornNro.getProcessos() + 1);
				fornsNro.set(index, fornNro);
			}
		}
		List<FornecedorNro> fornsNro2 = fornsNro.stream().filter(f -> f.getProcessos() > 0)
				.collect(Collectors.toList());
		Collections.sort(fornsNro2);
		return fornsNro2;
	}

}
