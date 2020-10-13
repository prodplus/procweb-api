package br.com.procweb.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.procweb.repositories.UsuarioRepository;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Service
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			return this.usuarioRepository.findByEmail(username)
					.orElseThrow(() -> new UsernameNotFoundException("Usuário não localizado!"));
		} catch (UsernameNotFoundException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"usuário não localizado!", e.getCause());
		}
	}

}
