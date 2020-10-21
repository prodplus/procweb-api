package br.com.procweb.models.forms;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.procweb.models.Consumidor;
import br.com.procweb.models.Fornecedor;
import br.com.procweb.models.Processo;
import br.com.procweb.models.auxiliares.Movimento;
import br.com.procweb.models.enums.Situacao;
import br.com.procweb.models.enums.TipoProcesso;
import br.com.procweb.services.ConsumidorService;
import br.com.procweb.services.FornecedorService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessoForm implements Serializable {

	private static final long serialVersionUID = 1728786500764503582L;

	private Integer id;
	@NotNull(message = "o 'tipo' é obrigatório!")
	private TipoProcesso tipo;
	@NotBlank(message = "os autos são obrigatórios!")
	@Pattern(regexp = "^[0-9]{3,4}\\/20[0-9]{2}", message = "autos inválidos!")
	private String autos;
	@Size(min = 1, message = "deve haver pelo menos um consumidor!")
	private List<Integer> consumidores = new ArrayList<>();
	private List<Integer> representantes = new ArrayList<>();
	@Size(min = 1, message = "deve haver pelo menos um consumidor!")
	private List<Integer> fornecedores = new ArrayList<>();
	@NotNull(message = "a data é obrigatória!")
	private LocalDate data;
	private String relato;
	private List<Movimento> movimentacao = new ArrayList<>();
	@NotNull(message = "a 'situação' é obrigatória!")
	private Situacao situacao;

	public ProcessoForm(Processo processo) {
		this.setId(processo.getId());
		this.setTipo(processo.getTipo());
		this.setAutos(processo.getAutos());
		processo.getConsumidores().forEach(c -> this.consumidores.add(c.getId()));
		processo.getRepresentantes().forEach(r -> this.representantes.add(r.getId()));
		processo.getFornecedores().forEach(f -> this.fornecedores.add(f.getId()));
		this.setMovimentacao(processo.getMovimentacao());
		this.setData(processo.getData());
		this.setRelato(processo.getRelato());
		this.setSituacao(processo.getSituacao());
	}

	public Processo converter(ConsumidorService consumidorService,
			FornecedorService fornecedorService) {
		List<Consumidor> consI = new ArrayList<>();
		List<Consumidor> reprI = new ArrayList<>();
		List<Fornecedor> fornI = new ArrayList<>();
		this.consumidores.forEach(c -> consI.add(consumidorService.buscar(c)));
		this.fornecedores.forEach(r -> reprI.add(consumidorService.buscar(r)));
		this.fornecedores.forEach(f -> fornI.add(fornecedorService.buscar(f)));
		return new Processo(this.getId(), this.getTipo(), this.getAutos(), consI, reprI, fornI,
				this.getData(), this.getMovimentacao(), this.getRelato(), this.getSituacao());
	}

}
