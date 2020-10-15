package br.com.procweb.models.forms;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.procweb.models.Atendimento;
import br.com.procweb.models.Consumidor;
import br.com.procweb.models.Fornecedor;
import br.com.procweb.models.Usuario;
import br.com.procweb.services.ConsumidorService;
import br.com.procweb.services.FornecedorService;
import br.com.procweb.services.UsuarioService;
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

	public Atendimento converter(ConsumidorService consumidorService,
			FornecedorService fornecedorService, UsuarioService usuarioService) {
		List<Consumidor> consI = new ArrayList<>();
		List<Fornecedor> fornI = new ArrayList<>();
		this.consumidores.forEach(c -> consI.add(consumidorService.buscar(c)));
		this.fornecedores.forEach(f -> fornI.add(fornecedorService.buscar(f)));
		Usuario atenI = usuarioService.buscarI(this.atendente);
		return new Atendimento(this.getId(), consI, fornI, getData(), getRelato(), atenI);
	}

}
