package br.com.procweb.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class Atendimento implements Serializable {

	private static final long serialVersionUID = 1994557715012460136L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderColumn
	private List<Consumidor> consumidores = new ArrayList<>();
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderColumn
	private List<Fornecedor> fornecedores = new ArrayList<>();
	@Column(nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "a data é obrigatória!")
	private LocalDate data;
	@Lob
	@Basic(fetch = FetchType.EAGER)
	private String relato;
	@ManyToOne
	@JoinColumn(nullable = false)
	@NotNull(message = "o atendente é obrigatório!")
	private Usuario atendente;

}
