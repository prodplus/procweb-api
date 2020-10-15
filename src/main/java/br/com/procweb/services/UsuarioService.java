package br.com.procweb.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.procweb.models.Perfil;
import br.com.procweb.models.Usuario;
import br.com.procweb.models.dto.UsuarioDto;
import br.com.procweb.models.enums.TipoLog;
import br.com.procweb.models.forms.UsuarioForm;
import br.com.procweb.repositories.PerfilRepository;
import br.com.procweb.repositories.UsuarioRepository;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PerfilRepository perfilRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private LogService logService;
	private static final String ENTIDADE = "USUARIO";

	public UsuarioDto salvar(@Valid UsuarioForm usuario) {
		try {
			Usuario novo = usuario.converter(this.usuarioRepository, this.perfilRepository);
			novo.setPassword(this.encoder.encode(novo.getPassword()));
			UsuarioDto u = new UsuarioDto(this.usuarioRepository.save(novo));
			this.logService.insereLog(TipoLog.INSERCAO, u.getId(), ENTIDADE);
			return u;
		} catch (ValidationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "erro de validação",
					e.getCause());
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "usuário já cadastrado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public UsuarioDto atualizar(Integer id, @Valid UsuarioForm usuario) {
		try {
			UsuarioDto us = this.usuarioRepository.findById(id).map(novo -> {
				novo.setNome(usuario.getNome());
				novo.setEmail(usuario.getEmail());
				if (usuario.getPassword() != null && usuario.getPassword().length() > 0)
					novo.setPassword(this.encoder.encode(usuario.getPassword()));
				novo.setPerfil(this.perfilRepository.findByRole(usuario.getPerfil()).get());
				return new UsuarioDto(this.usuarioRepository.save(novo));
			}).orElseThrow(() -> new EntityNotFoundException());
			this.logService.insereLog(TipoLog.ATUALIZACAO, us.getId(), ENTIDADE);
			return us;
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "usuário não localizado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public UsuarioDto buscar(Integer id) {
		try {
			return this.usuarioRepository.findById(id).map(u -> new UsuarioDto(u))
					.orElseThrow(() -> new EntityNotFoundException());
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "usuário não localizado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Usuario buscarI(Integer id) {
		try {
			return this.usuarioRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException());
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "usuário não localizado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public Page<UsuarioDto> listarAtivos(boolean ativo, int pagina, int quant) {
		try {
			Pageable pageable = PageRequest.of(pagina, quant, Direction.ASC, "nome");
			Page<Usuario> page = this.usuarioRepository.findAllByAtivo(ativo, pageable);
			List<UsuarioDto> lista = new ArrayList<>();
			page.getContent().forEach(u -> lista.add(new UsuarioDto(u)));
			return new PageImpl<>(lista, pageable, page.getTotalElements());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public void ativar(Integer id, boolean ativar) {
		try {
			Usuario usuario = this.usuarioRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException());
			usuario.setAtivo(ativar);
			this.usuarioRepository.save(usuario);
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "usuário não localizado!",
					e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public void excluir(Integer id) {
		try {
			this.logService.insereLog(TipoLog.EXCLUSAO, id, ENTIDADE);
			this.usuarioRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"não é possível excluir o usuário!", e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public List<Perfil> getPerfis() {
		try {
			return this.perfilRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"ocorreu um erro no servidor!", e.getCause());
		}
	}

	public boolean loginDisponivel(String email) {
		Usuario u = this.usuarioRepository.findByEmail(email).orElse(null);
		return u != null;
	}

}
