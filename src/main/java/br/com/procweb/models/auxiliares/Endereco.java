package br.com.procweb.models.auxiliares;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.procweb.models.enums.UF;
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
@Embeddable
public class Endereco implements Serializable {

	private static final long serialVersionUID = -6148302796886001842L;
	@Column(length = 30)
	private String cep;
	private String logradouro;
	@Column(length = 30)
	private String numero;
	@Column(length = 50)
	private String complemento;
	private String bairro;
	private String municipio;
	@Enumerated(EnumType.STRING)
	@Column(length = 2)
	private UF uf;

}
