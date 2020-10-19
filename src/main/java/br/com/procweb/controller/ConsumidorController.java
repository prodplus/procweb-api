package br.com.procweb.controller;

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

import br.com.procweb.models.Consumidor;
import br.com.procweb.services.ConsumidorService;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/consumidores")
public class ConsumidorController {

	@Autowired
	private ConsumidorService consumidorService;

	@PostMapping
	public ResponseEntity<Consumidor> salvar(@RequestBody Consumidor consumidor) {
		return ResponseEntity.ok(this.consumidorService.salvar(consumidor));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Consumidor> atualizar(@PathVariable Integer id,
			@RequestBody Consumidor consumidor) {
		return ResponseEntity.ok(this.consumidorService.atualizar(id, consumidor));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Consumidor> buscar(@PathVariable Integer id) {
		return ResponseEntity.ok(this.consumidorService.buscar(id));
	}

	@GetMapping("/listar/{pagina}/{quant}")
	public ResponseEntity<Page<Consumidor>> listar(@PathVariable int pagina,
			@PathVariable int quant) {
		return ResponseEntity.ok(this.consumidorService.listar(pagina, quant));
	}

	@GetMapping("/listar/{pagina}/{quant}/{parametro}")
	public ResponseEntity<Page<Consumidor>> listarPorParametro(@PathVariable String parametro,
			@PathVariable int pagina, @PathVariable int quant) {
		return ResponseEntity
				.ok(this.consumidorService.listarPorParametro(parametro, pagina, quant));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Integer id) {
		this.consumidorService.excluir(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/existe/{cadastro}")
	public ResponseEntity<Boolean> consumidorExiste(@PathVariable String cadastro) {
		return ResponseEntity.ok(this.consumidorService.consumidorExiste(cadastro));
	}

}
