package br.com.procweb.configs.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.procweb.models.Perfil;
import br.com.procweb.models.Usuario;
import br.com.procweb.repositories.PerfilRepository;
import br.com.procweb.repositories.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Service
public class TokenService {

	@Value("${jwt.expiration}")
	private Long expiration;
	@Value("${jwt.secret}")
	private String secret;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PerfilRepository perfilRepository;

	public String geraToken(Authentication auth) {
		try {
			Usuario logado = new Usuario();
			if (auth.getPrincipal() instanceof User) {
				User user = (User) auth.getPrincipal();
				Perfil perfil = new Perfil();
				for (GrantedAuthority g : user.getAuthorities())
					perfil = this.perfilRepository.findByRole(g.getAuthority()).get();
				logado = this.usuarioRepository.findByEmail(user.getUsername()).orElse(new Usuario(
						0, "TESTE", "teste@teste.com", user.getPassword(), perfil, true));
			} else {
				logado = (Usuario) auth.getPrincipal();
			}

			if (logado.getId() == null)
				logado.setId(0);

			Date hoje = new Date();
			Date dataExpiracao = new Date(hoje.getTime() + this.expiration);

			Perfil perfilAtual = logado.getPerfil();

			return Jwts.builder().setIssuer(logado.getNome()).setSubject(logado.getId().toString())
					.setIssuedAt(hoje).claim("id", logado.getId())
					.claim("email", logado.getUsername()).claim("perfil", perfilAtual.getRole())
					.claim("nome", logado.getNome()).setExpiration(dataExpiracao)
					.signWith(SignatureAlgorithm.HS256, this.secret).compact();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"erro na geração do token!", e.getCause());
		}
	}

	public boolean isTokenValido(String token) {
		try {
			if (token != null && token.length() > 0) {
				Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
				return true;
			} else {
				return false;
			}
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "login expirado!",
					e.getCause());
		} catch (UnsupportedJwtException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token não suportado!",
					e.getCause());
		} catch (MalformedJwtException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token inválido!",
					e.getCause());
		} catch (SignatureException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "erro de assinatura!",
					e.getCause());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "argumento ilegal!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"erro na validação do token!", e.getCause());
		}
	}

	public Integer getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Integer.valueOf(claims.getSubject());
	}

}
