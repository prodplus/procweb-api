package br.com.procweb.services;

import java.time.LocalDate;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.procweb.models.Atendimento;
import br.com.procweb.models.Fornecedor;
import br.com.procweb.models.Processo;
import br.com.procweb.models.auxiliares.Movimento;
import br.com.procweb.models.enums.Situacao;
import br.com.procweb.reports.AtendIni;
import br.com.procweb.reports.ConvAudCons;
import br.com.procweb.reports.ConvAudForn;
import br.com.procweb.reports.DespachoAud;
import br.com.procweb.reports.DespachoNot;
import br.com.procweb.reports.Inicial;
import br.com.procweb.reports.NotCincoDias;
import br.com.procweb.reports.NotConsumidor;
import br.com.procweb.reports.NotDezDias;
import br.com.procweb.reports.NotImpugnacao;
import br.com.procweb.reports.NotMulta;
import br.com.procweb.reports.Oficio;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Service
public class DocumentoService {

	@Autowired
	private ProcessoService processoService;
	@Autowired
	private FornecedorService fornecedorService;
	@Autowired
	private AtendimentoService atendimentoService;

	public InputStreamResource notDezDias(Integer idProc, Integer idForn) {
		try {
			Processo processo = this.processoService.buscar(idProc);
			Fornecedor fornecedor = this.fornecedorService.buscar(idForn);
			return new InputStreamResource(NotDezDias.gerar(processo, fornecedor));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public InputStreamResource notCincoDias(Integer idProc, Integer idForn) {
		try {
			Processo processo = this.processoService.buscar(idProc);
			Fornecedor fornecedor = this.fornecedorService.buscar(idForn);
			return new InputStreamResource(NotCincoDias.gerar(processo, fornecedor));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public InputStreamResource notImpugnacao(Integer idProc, Integer idForn) {
		try {
			Processo processo = this.processoService.buscar(idProc);
			Fornecedor fornecedor = this.fornecedorService.buscar(idForn);
			return new InputStreamResource(NotImpugnacao.gerar(processo, fornecedor));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public InputStreamResource notMulta(Integer idProc, Integer idForn) {
		try {
			Processo processo = this.processoService.buscar(idProc);
			Fornecedor fornecedor = this.fornecedorService.buscar(idForn);
			return new InputStreamResource(NotMulta.gerar(processo, fornecedor));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public InputStreamResource despachoOficio(Integer idProc) {
		try {
			Processo processo = this.processoService.buscar(idProc);
			processo.getMovimentacao().add(new Movimento(LocalDate.now(), Situacao.AUTUADO,
					Situacao.NOTIFICAR_FORNECEDOR, "", null, null));
			Collections.sort(processo.getMovimentacao());
			return new InputStreamResource(
					DespachoNot.gerar(this.processoService.atualizar(idProc, processo)));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public InputStreamResource despachoAud(Integer idProc, Movimento movimento) {
		try {
			Processo processo = this.processoService.buscar(idProc);
			processo.getMovimentacao().add(new Movimento(LocalDate.now(), Situacao.AUTUADO,
					Situacao.AUDIENCIA, "", movimento.getAuxD(), movimento.getAuxT()));
			Collections.sort(processo.getMovimentacao());
			return new InputStreamResource(
					DespachoAud.gerar(this.processoService.atualizar(idProc, processo), movimento));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}
	
	public InputStreamResource notConsumidor(Integer idProc) {
		try {
			Processo processo = this.processoService.buscar(idProc);
			return new InputStreamResource(NotConsumidor.gerar(processo));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}
	
	public InputStreamResource convAudCons(Integer idProc, Movimento movimento) {
		try {
			Processo processo = this.processoService.buscar(idProc);
			return new InputStreamResource(ConvAudCons.gerar(processo, movimento));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}
	
	public InputStreamResource convAudForn(Integer idProc, Movimento movimento) {
		try {
			Processo processo = this.processoService.buscar(idProc);
			return new InputStreamResource(ConvAudForn.gerar(processo, movimento));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}
	
	public InputStreamResource inicial(Integer idProc) {
		try {
			Processo processo = this.processoService.buscar(idProc);
			return new InputStreamResource(Inicial.gerar(processo));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}
	
	public InputStreamResource oficio(Integer idProc) {
		try {
			Processo processo = this.processoService.buscar(idProc);
			return new InputStreamResource(Oficio.gerar(processo));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}
	
	public InputStreamResource atendimento(Integer idAte) {
		try {
			Atendimento atendimento = this.atendimentoService.buscar(idAte);
			return new InputStreamResource(AtendIni.gerar(atendimento));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

}
