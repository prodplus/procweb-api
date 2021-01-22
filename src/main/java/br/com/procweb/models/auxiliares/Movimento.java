package br.com.procweb.models.auxiliares;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.procweb.models.enums.Situacao;
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
public class Movimento implements Serializable, Comparable<Movimento> {

	private static final long serialVersionUID = 8625299115958397357L;
	@Column(nullable = false)
	@NotNull(message = "a data é obrigatória!")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	@NotEmpty(message = "campo obrigatório!")
	private Situacao de;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	@NotEmpty(message = "campo obrigatório!")
	private Situacao para;
	private String averbacao;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate auxD;
	@JsonFormat(pattern = "HH:mm")
	private LocalTime auxT;

	@Override
	public int compareTo(Movimento o) {
		if (this.data != null && o.getData() != null)
			if (this.data.compareTo(o.getData()) == 0)
				if (this.auxD != null && o.getAuxD() != null)
					if (this.auxD.compareTo(o.getAuxD()) == 0)
						if (this.auxT != null && o.getAuxT() != null)
							return this.auxT.compareTo(o.getAuxT()) * -1;
						else
							return 0;
					else
						return this.auxD.compareTo(o.getAuxD()) * -1;
				else
					return 0;
			else
				return this.data.compareTo(o.getData()) * -1;
		else
			return 0;
	}

}
