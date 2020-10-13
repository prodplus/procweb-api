package br.com.procweb.configs.security;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.procweb.models.dto.TokenDto;
import br.com.procweb.models.forms.LoginForm;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Service
public class AutenticacaoService {

	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private TokenService tokenService;

	public TokenDto autenticar(@Valid LoginForm form) {
		try {
			UsernamePasswordAuthenticationToken login = form.converter();
			Authentication authentication = authManager.authenticate(login);
			String token = tokenService.geraToken(authentication);
			return new TokenDto(token, "Bearer ");
		} catch (AuthenticationException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "erro de autenticação!",
					e.getCause());
		} catch (ValidationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "erro de validação!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"erro de servidor na autenticação!", e.getCause());
		}
	}

}
