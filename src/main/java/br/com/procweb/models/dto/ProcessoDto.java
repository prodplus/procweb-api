package br.com.procweb.models.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.procweb.models.Processo;
import br.com.procweb.models.enums.Situacao;
import br.com.procweb.models.enums.TipoProcesso;
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
public class ProcessoDto implements Serializable {

	private static final long serialVersionUID = -1929155627946721198L;

	private Integer id;
	private TipoProcesso tipo;
	private String autos;
	private List<String> consumidores = new ArrayList<>();
	private List<String> representantes = new ArrayList<>();
	private List<String> fornecedores = new ArrayList<>();
	private LocalDate data;
	private Situacao situacao;

	public ProcessoDto(Processo processo) {
		this.setId(processo.getId());
		this.setTipo(processo.getTipo());
		this.setAutos(processo.getAutos());
		processo.getConsumidores().forEach(c -> this.consumidores.add(c.getDenominacao()));
		processo.getRepresentantes().forEach(r -> this.representantes.add(r.getDenominacao()));
		processo.getFornecedores().forEach(f -> this.fornecedores.add(f.getFantasia()));
		this.setData(processo.getData());
		this.setSituacao(processo.getSituacao());
	}

}
