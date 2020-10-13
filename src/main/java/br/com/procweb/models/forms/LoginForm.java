package br.com.procweb.models.forms;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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
public class LoginForm implements Serializable {

	private static final long serialVersionUID = -918548010027018362L;
	@NotBlank(message = "o email é obrigatório!")
	private String email;
	@NotBlank(message = "a senha é obrigatória!")
	private String password;
	
	public UsernamePasswordAuthenticationToken converter() {
		return new UsernamePasswordAuthenticationToken(email, password);
	}

}
