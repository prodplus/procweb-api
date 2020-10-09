package br.com.procweb.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.procweb.models.auxiliares.Movimento;
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
@Entity
public class Processo implements Serializable {

	private static final long serialVersionUID = 6941000754649711169L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	@NotNull(message = "o 'tipo' é obrigatório!")
	private TipoProcesso tipo;
	@Column(nullable = false, unique = true, length = 10)
	@Pattern(regexp = "^[0-9]{3,4}\\/20[0-9]{2}", message = "autos inválidos!")
	@NotEmpty(message = "os 'autos' são obrigatórios!")
	private String autos;
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderColumn
	@Size(min = 1, message = "deve haver pelo menos um consumidor!")
	private List<Consumidor> consumidores = new ArrayList<>();
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderColumn
	private List<Consumidor> representantes = new ArrayList<>();
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderColumn
	@Size(min = 1, message = "deve haver pelo menos um fornecedor!")
	private List<Fornecedor> fornecedores = new ArrayList<>();
	@Column(nullable = false)
	@NotNull(message = "a data é obrigatória!")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderColumn
	private List<Movimento> movimentacao = new ArrayList<>();
	@Lob
	@Basic(fetch = FetchType.EAGER)
	private String relato;
	@Enumerated(EnumType.STRING)
	@NotNull(message = "a 'situação' é obrigatória!")
	private Situacao situacao;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((autos == null) ? 0 : autos.hashCode());
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
		Processo other = (Processo) obj;
		if (autos == null) {
			if (other.autos != null)
				return false;
		} else if (!autos.equals(other.autos))
			return false;
		return true;
	}

}
