package br.com.procweb.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import br.com.procweb.models.auxiliares.Endereco;
import br.com.procweb.models.enums.TipoPessoa;
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
@Entity
public class Consumidor implements Serializable {

	private static final long serialVersionUID = -5489715052114371017L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Enumerated(EnumType.STRING)
	@NotNull(message = "o 'tipo' é obrigatório!")
	private TipoPessoa tipo;
	@Column(nullable = false)
	@NotEmpty(message = "a 'denominação' é obrigatória!")
	private String denominacao;
	@Column(length = 30, nullable = false, unique = true)
	@NotEmpty(message = "o 'cadastro' é obrigatório!")
	private String cadastro;
	@Email(message = "email inválido!")
	private String email;
	@Embedded
	private Endereco endereco;
	@ElementCollection
	@CollectionTable(name = "fones_c", joinColumns = @JoinColumn(name = "cons_id"))
	@Column(name = "fone", length = 20)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<String> fones = new HashSet<>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cadastro == null) ? 0 : cadastro.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Consumidor other = (Consumidor) obj;
		if (cadastro == null) {
			if (other.cadastro != null)
				return false;
		} else if (!cadastro.equals(other.cadastro))
			return false;
		return true;
	}

}
