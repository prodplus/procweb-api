package br.com.procweb.models.dto;

import java.io.Serializable;

import br.com.procweb.models.Processo;
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
public class ProcDesc implements Serializable {

	private static final long serialVersionUID = 1353203091149015509L;
	
	private ProcessoDto processo;
	private String descricao;
	
	public ProcDesc(Processo processo, String descricao) {
		this.setProcesso(new ProcessoDto(processo));
		this.setDescricao(descricao);
	}

}
