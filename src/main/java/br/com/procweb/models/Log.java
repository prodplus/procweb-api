package br.com.procweb.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.procweb.models.enums.TipoLog;
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
public class Log implements Serializable {

	private static final long serialVersionUID = 724440411212137829L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime data;
	@ManyToOne
	@JoinColumn(nullable = false)
	private Usuario usuario;
	@Column(nullable = false)
	private String log;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private TipoLog tipo;

}
