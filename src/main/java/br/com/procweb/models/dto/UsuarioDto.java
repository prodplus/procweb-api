package br.com.procweb.models.dto;

import java.io.Serializable;

import br.com.procweb.models.Usuario;
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
public class UsuarioDto implements Serializable {

	private static final long serialVersionUID = -2190016776396608641L;
	
	private Integer id;
	private String nome;
	private String email;
	private String perfil;
	private boolean ativo;
	
	public UsuarioDto(Usuario usuario) {
		this.setId(usuario.getId());
		this.setNome(usuario.getNome());
		this.setEmail(usuario.getEmail());
		this.setPerfil(usuario.getPerfil().getRole());
		this.setAtivo(usuario.isAtivo());
	}

}
