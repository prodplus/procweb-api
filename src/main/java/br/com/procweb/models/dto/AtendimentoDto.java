package br.com.procweb.models.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.procweb.models.Atendimento;
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
public class AtendimentoDto implements Serializable {

	private static final long serialVersionUID = 7658120508830095524L;

	private Integer id;
	private List<String> consumidores = new ArrayList<>();
	private List<String> fornecedores = new ArrayList<>();
	private LocalDate data;
	private String relato;
	private String atendente;

	public AtendimentoDto(Atendimento atendimento) {
		this.setId(atendimento.getId());
		atendimento.getConsumidores().forEach(c -> this.consumidores.add(c.getDenominacao()));
		atendimento.getFornecedores().forEach(f -> this.fornecedores.add(f.getFantasia()));
		this.setData(atendimento.getData());
		this.setRelato(atendimento.getRelato());
		this.setAtendente(atendimento.getAtendente().getNome());
	}

}
