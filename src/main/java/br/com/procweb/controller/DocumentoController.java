package br.com.procweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.procweb.models.auxiliares.Movimento;
import br.com.procweb.services.DocumentoService;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/documentos")
public class DocumentoController {

	@Autowired
	private DocumentoService documentoService;

	@GetMapping(path = "/not_dez_dias/{idProc}/{idForn}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> notDezDias(@PathVariable Integer idProc,
			@PathVariable Integer idForn) {
		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=notdezdias.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(this.documentoService.notDezDias(idProc, idForn));
	}

	@GetMapping(path = "/not_cinco_dias/{idProc}/{idForn}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> notCincoDias(@PathVariable Integer idProc,
			@PathVariable Integer idForn) {
		return ResponseEntity.ok()
				.header("Content-Disposition", "inline; filename=notcincodias.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(this.documentoService.notCincoDias(idProc, idForn));
	}

	@GetMapping(path = "/not_impugnacao/{idProc}/{idForn}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> notImpugnacao(@PathVariable Integer idProc,
			@PathVariable Integer idForn) {
		return ResponseEntity.ok()
				.header("Content-Disposition", "inline; filename=notimpugnacao.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(this.documentoService.notImpugnacao(idProc, idForn));
	}

	@GetMapping(path = "/not_multa/{idProc}/{idForn}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> notMulta(@PathVariable Integer idProc,
			@PathVariable Integer idForn) {
		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=notmulta.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(this.documentoService.notMulta(idProc, idForn));
	}

	@GetMapping(path = "/despacho_not/{idProc}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> despachoNot(@PathVariable Integer idProc) {
		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=despachonot.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(this.documentoService.despachoOficio(idProc));
	}

	@PutMapping(path = "/despacho_aud/{idProc}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> despachoAud(@PathVariable Integer idProc,
			@RequestBody Movimento movimento) {
		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=despachoaud.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(this.documentoService.despachoAud(idProc, movimento));
	}

	@GetMapping(path = "/not_consumidor/{idProc}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> notConsumidor(@PathVariable Integer idProc) {
		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=despachonot.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(this.documentoService.notConsumidor(idProc));
	}

	@PutMapping(path = "/conv_aud_cons/{idProc}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> convAudCons(@PathVariable Integer idProc,
			@RequestBody Movimento movimento) {
		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=despachoaud.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(this.documentoService.convAudCons(idProc, movimento));
	}

	@PutMapping(path = "/conv_aud_forn/{idProc}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> convAudForn(@PathVariable Integer idProc,
			@RequestBody Movimento movimento) {
		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=despachoaud.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(this.documentoService.convAudForn(idProc, movimento));
	}

	@GetMapping(path = "/inicial/{idProc}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> inicial(@PathVariable Integer idProc) {
		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=despachonot.pdf")
				.contentType(MediaType.APPLICATION_PDF).body(this.documentoService.inicial(idProc));
	}
	
	@GetMapping(path = "/oficio/{idProc}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> oficio(@PathVariable Integer idProc) {
		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=despachonot.pdf")
				.contentType(MediaType.APPLICATION_PDF).body(this.documentoService.oficio(idProc));
	}

	@GetMapping(path = "/atendimento/{idAte}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> atendimento(@PathVariable Integer idAte) {
		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=despachonot.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(this.documentoService.atendimento(idAte));
	}

}
