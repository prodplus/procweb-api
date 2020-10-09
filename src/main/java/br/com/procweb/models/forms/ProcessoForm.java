package br.com.procweb.models.forms;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
	private LocalDate data;
	private String relato;
	@NotNull(message = "a 'situação' é obrigatória!")
	private Situacao situacao;

}
