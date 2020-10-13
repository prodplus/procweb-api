package br.com.procweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.procweb.models.Perfil;
import br.com.procweb.models.dto.UsuarioDto;
import br.com.procweb.models.forms.UsuarioForm;
import br.com.procweb.services.UsuarioService;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping
	public ResponseEntity<UsuarioDto> salvar(@RequestBody UsuarioForm usuario) {
		return ResponseEntity.ok(this.usuarioService.salvar(usuario));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UsuarioDto> atualizar(@PathVariable Integer id,
			@RequestBody UsuarioForm usuario) {
		return ResponseEntity.ok(this.usuarioService.atualizar(id, usuario));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDto> buscar(@PathVariable Integer id) {
		return ResponseEntity.ok(this.usuarioService.buscar(id));
	}

	@GetMapping("/listar/{pagina}/{quant}/{ativos}")
	public ResponseEntity<Page<UsuarioDto>> listarAtivos(@PathVariable boolean ativos,
			@PathVariable int pagina, @PathVariable int quant) {
		return ResponseEntity.ok(this.usuarioService.listarAtivos(ativos, pagina, quant));
	}

	@DeleteMapping("/ativar/{id}/{ativar}")
	public ResponseEntity<?> ativar(@PathVariable Integer id, @PathVariable boolean ativar) {
		this.usuarioService.ativar(id, ativar);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Integer id) {
		this.usuarioService.excluir(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/perfis")
	public ResponseEntity<List<Perfil>> getPerfis() {
		return ResponseEntity.ok(this.usuarioService.getPerfis());
	}

	@GetMapping("/email_existe/{email}")
	public ResponseEntity<Boolean> emailDisponivel(@PathVariable String email) {
		return ResponseEntity.ok(this.usuarioService.loginDisponivel(email));
	}

}
