package br.com.procweb.models.dto;

import java.io.Serializable;

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
public class TokenDto implements Serializable {

	private static final long serialVersionUID = -1596154721368349738L;
	
	private String token;
	private String tipo;

}
