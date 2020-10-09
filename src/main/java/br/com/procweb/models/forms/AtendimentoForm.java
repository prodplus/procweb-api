package br.com.procweb.models.forms;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class AtendimentoForm implements Serializable {

	private static final long serialVersionUID = 5535663872853644001L;

	private Integer id;
	@NotNull(message = "a data é obrigatória!")
	private LocalDate data;
	@Size(min = 1, message = "deve haver pelo menos um consumidor!")
	private List<Integer> consumidores = new ArrayList<>();
	@Size(min = 1, message = "deve haver pelo menos um fornecedor!")
	private List<Integer> fornecedores = new ArrayList<>();
	private String relato;
	@NotNull(message = "o atendente é obrigatório!")
	private Integer atendente;

}
