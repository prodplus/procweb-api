package br.com.procweb.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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

import br.com.procweb.models.Processo;
import br.com.procweb.models.auxiliares.FornecedorNro;
import br.com.procweb.models.dto.ProcessoDto;
import br.com.procweb.models.enums.Situacao;
import br.com.procweb.models.forms.ProcessoForm;
import br.com.procweb.services.ProcessoService;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/processos")
public class ProcessoController {

	@Autowired
	private ProcessoService processoService;

	@PostMapping
	public ResponseEntity<ProcessoDto> salvar(@RequestBody ProcessoForm processo) {
		return ResponseEntity.ok(this.processoService.salvar(processo));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProcessoDto> atualizar(@PathVariable Integer id,
			@RequestBody ProcessoForm processo) {
		return ResponseEntity.ok(this.processoService.atualizar(id, processo));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Processo> buscar(@PathVariable Integer id) {
		return ResponseEntity.ok(this.processoService.buscar(id));
	}

	@GetMapping("/listar/{pagina}/{quant}")
	public ResponseEntity<Page<ProcessoDto>> listar(@PathVariable int pagina,
			@PathVariable int quant) {
		return ResponseEntity.ok(this.processoService.listar(pagina, quant));
	}

	@PutMapping("/porautos/{pagina}/{quant}")
	public ResponseEntity<Page<ProcessoDto>> listarPorAutos(@RequestBody String autos,
			@PathVariable int pagina, @PathVariable int quant) {
		return ResponseEntity.ok(this.processoService.listarPorAutos(autos, pagina, quant));
	}

	@GetMapping("/porconsumidor/{pagina}/{quant}/{param}")
	public ResponseEntity<Page<ProcessoDto>> listarPorConsumidor(@PathVariable String param,
			@PathVariable int pagina, @PathVariable int quant) {
		return ResponseEntity.ok(this.processoService.listarPorConsumidor(param, pagina, quant));
	}

	@GetMapping("/porfornecedor/{pagina}/{quant}/{param}")
	public ResponseEntity<Page<ProcessoDto>> listarPorFornecedor(@PathVariable String param,
			@PathVariable int pagina, @PathVariable int quant) {
		return ResponseEntity.ok(this.processoService.listarPorFornecedor(param, pagina, quant));
	}

	@GetMapping("/porsituacao/{pagina}/{quant}/{situacao}")
	public ResponseEntity<Page<ProcessoDto>> listarPorSituacao(@PathVariable Situacao situacao,
			@PathVariable int pagina, @PathVariable int quant) {
		return ResponseEntity.ok(this.processoService.listarPorSituacao(situacao, pagina, quant));
	}

	@GetMapping("/porsituacao/{pagina}/{quant}/{situacao}/{situacao2}")
	public ResponseEntity<Page<ProcessoDto>> listarPorSituacao(@PathVariable Situacao situacao,
			@PathVariable Situacao situacao2, @PathVariable int pagina, @PathVariable int quant) {
		return ResponseEntity
				.ok(this.processoService.listarPorSituacao(situacao, situacao2, pagina, quant));
	}

	@PutMapping("/porsituacao/{pagina}/{quant}/{situacao}/{situacao2}")
	public ResponseEntity<Page<ProcessoDto>> listarPorSituacao(@PathVariable Situacao situacao,
			@PathVariable Situacao situacao2, @RequestBody String autos, @PathVariable int pagina,
			@PathVariable int quant) {
		return ResponseEntity.ok(
				this.processoService.listarPorSituacao(situacao, situacao2, autos, pagina, quant));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Integer id) {
		this.processoService.excluir(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/autos/{data}")
	public ResponseEntity<Map<String, String>> getAutos(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
		Map<String, String> retorno = new HashMap<>();
		retorno.put("nro", this.processoService.getAutos(data));
		return ResponseEntity.ok(retorno);
	}

	@GetMapping("/ranking/{ano}")
	public ResponseEntity<List<FornecedorNro>> ranking(@PathVariable Integer ano) {
		return ResponseEntity.ok(this.processoService.ranking(ano));
	}

}
