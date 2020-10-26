package br.com.procweb.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.procweb.models.Processo;
import br.com.procweb.models.auxiliares.Movimento;
import br.com.procweb.models.dto.ProcDesc;
import br.com.procweb.models.dto.ProcessoDto;
import br.com.procweb.models.enums.Situacao;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Service
public class OperacaoService {

	@Autowired
	private ProcessoService processoService;

	public List<ProcessoDto> porNotFornecedor() {
		try {
			List<Processo> processos = this.processoService
					.listarPorSituacao(Situacao.NOTIFICAR_FORNECEDOR);
			NotCompare comparator = new NotCompare();
			Collections.sort(processos, comparator);
			List<ProcessoDto> retorno = new ArrayList<>();
			processos.forEach(p -> retorno.add(new ProcessoDto(p)));
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public List<ProcessoDto> porNotConsumidor() {
		try {
			List<Processo> processos = this.processoService
					.listarPorSituacao(Situacao.NOTIFICAR_CONSUMIDOR);
			NotCompare comparator = new NotCompare();
			Collections.sort(processos, comparator);
			List<ProcessoDto> retorno = new ArrayList<>();
			processos.forEach(p -> retorno.add(new ProcessoDto(p)));
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public List<ProcessoDto> porPrazo() {
		try {
			List<Processo> processos = this.processoService.listarPorSituacao(Situacao.PRAZO);
			PrazoCompare comparator = new PrazoCompare();
			Collections.sort(processos, comparator);
			List<ProcessoDto> retorno = new ArrayList<>();
			processos.forEach(p -> retorno.add(new ProcessoDto(p)));
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public List<ProcessoDto> porAudiencia() {
		try {
			List<Processo> processos = this.processoService.listarPorSituacao(Situacao.AUDIENCIA);
			AudienciaCompare comparator = new AudienciaCompare();
			Collections.sort(processos, comparator);
			List<ProcessoDto> retorno = new ArrayList<>();
			processos.forEach(p -> retorno.add(new ProcessoDto(p)));
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public List<ProcDesc> porAudienciaDesc() {
		try {
			List<Processo> processos = this.processoService.listarPorSituacao(Situacao.AUDIENCIA);
			AudienciaCompare comparator = new AudienciaCompare();
			Collections.sort(processos, comparator);
			List<ProcDesc> retorno = new ArrayList<>();
			processos.forEach(p -> {
				List<Movimento> movimentos = p.getMovimentacao();
				Collections.sort(movimentos);
				Movimento ultimo = p.getMovimentacao().get(0);
				String desc;
				if (ultimo.getAuxD() != null && ultimo.getAuxT() != null)
					desc = String.format("%02d/%02d %02d:%02d", ultimo.getAuxD().getDayOfMonth(),
							ultimo.getAuxD().getMonthValue(), ultimo.getAuxT().getHour(),
							ultimo.getAuxT().getMinute());
				else
					desc = "Erro!";
				retorno.add(new ProcDesc(p, desc));
			});
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public List<ProcessoDto> getNovos() {
		try {
			List<Processo> processos = this.processoService.listarPorSituacao(Situacao.AUTUADO);
			NotCompare comparator = new NotCompare();
			Collections.sort(processos, comparator);
			List<ProcessoDto> retorno = new ArrayList<>();
			processos.forEach(p -> retorno.add(new ProcessoDto(p)));
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public List<ProcessoDto> porAutosNovos(String autos) {
		try {
			List<Processo> processos = this.processoService.listarPorAutosSituacao(autos,
					Situacao.AUTUADO);
			NotCompare comparator = new NotCompare();
			Collections.sort(processos, comparator);
			List<ProcessoDto> retorno = new ArrayList<>();
			processos.forEach(p -> retorno.add(new ProcessoDto(p)));
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	/**
	 * 
	 * @author ©Marlon F. Garcia
	 *
	 */
	class NotCompare implements Comparator<Processo> {

		@Override
		public int compare(Processo o1, Processo o2) {
			List<Movimento> p1Movs = o1.getMovimentacao();
			List<Movimento> p2Movs = o2.getMovimentacao();
			Collections.sort(p1Movs);
			Collections.sort(p2Movs);
			Movimento mov1 = p1Movs.get(0);
			Movimento mov2 = p2Movs.get(0);
			return mov1.getData().compareTo(mov2.getData());
		}

	}

	/**
	 * 
	 * @author ©Marlon F. Garcia
	 *
	 */
	class PrazoCompare implements Comparator<Processo> {

		@Override
		public int compare(Processo o1, Processo o2) {
			List<Movimento> p1Movs = o1.getMovimentacao();
			List<Movimento> p2Movs = o2.getMovimentacao();
			Collections.sort(p1Movs);
			Collections.sort(p2Movs);
			Movimento mov1 = p1Movs.get(0);
			Movimento mov2 = p2Movs.get(0);
			if (mov1.getAuxD() != null && mov2.getAuxD() != null)
				return mov1.getAuxD().compareTo(mov2.getAuxD());
			else
				return mov1.getData().compareTo(mov2.getData());
		}

	}

	/**
	 * 
	 * @author ©Marlon F. Garcia
	 *
	 */
	class AudienciaCompare implements Comparator<Processo> {

		@Override
		public int compare(Processo o1, Processo o2) {
			List<Movimento> p1Movs = o1.getMovimentacao();
			List<Movimento> p2Movs = o2.getMovimentacao();
			Collections.sort(p1Movs);
			Collections.sort(p2Movs);
			Movimento mov1 = p1Movs.get(0);
			Movimento mov2 = p2Movs.get(0);
			if (mov1.getAuxD() != null && mov2.getAuxD() != null)
				if (mov1.getAuxD().compareTo(mov2.getAuxD()) == 0)
					return mov1.getAuxT().compareTo(mov2.getAuxT());
				else
					return mov1.getAuxD().compareTo(mov2.getAuxD());
			else
				return mov1.getData().compareTo(mov2.getData());
		}

	}

}
