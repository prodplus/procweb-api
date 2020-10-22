package br.com.procweb.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.procweb.models.Consumidor;
import br.com.procweb.models.Fornecedor;
import br.com.procweb.models.Processo;
import br.com.procweb.models.auxiliares.Movimento;
import br.com.procweb.utils.LocalDateUtils;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
public class ConvAudCons {

	public static ByteArrayInputStream gerar(Processo processo, Movimento movimento) {
		Document document = new Document(PageSize.A4);
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(document, output);
			document.setMargins(65, 30, 10, 40);
			document.open();

			// cria fontes e espaço
			Font titFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			titFont.setSize(14);
			Font intFont = FontFactory.getFont(FontFactory.HELVETICA);
			intFont.setSize(12);
			Font cabeca = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			cabeca.setSize(16);
			Paragraph espaco = new Paragraph(new Phrase(" ", intFont));
			espaco.setAlignment(Element.ALIGN_CENTER);

			Paragraph cabecalho = new Paragraph("COORDENADORIA DE PROTEÇÃO E DEFESA DO CONSUMIDOR",
					titFont);
			cabecalho.setAlignment(Element.ALIGN_CENTER);
			document.add(cabecalho);
			cabecalho = new Paragraph("PROCON - PATO BRANCO", titFont);
			cabecalho.setAlignment(Element.ALIGN_CENTER);
			document.add(cabecalho);
			cabecalho = new Paragraph(
					"Rua Luiz Favreto, nº 10, sala 7 - fone (46) 3902-1289 / (46) 3902-1325",
					intFont);
			cabecalho.setAlignment(Element.ALIGN_CENTER);
			document.add(cabecalho);
			cabecalho = new Paragraph("Email: proconpatobranco01@gmail.com", intFont);
			cabecalho.setAlignment(Element.ALIGN_CENTER);
			document.add(cabecalho);

			// margem superior
			for (int i = 0; i < 3; i++)
				document.add(espaco);

			Paragraph cabecaP = new Paragraph("CONVOCAÇÃO PARA AUDIÊNCIA CONCILIATÓRIA", cabeca);
			cabecaP.setAlignment(Element.ALIGN_CENTER);
			document.add(cabecaP);
			document.add(espaco);

			// data
			Paragraph data = new Paragraph(
					String.format("Pato Branco, %02d de %s de %d", LocalDate.now().getDayOfMonth(),
							LocalDateUtils.getMesExtenso(LocalDate.now().getMonthValue()),
							LocalDate.now().getYear()),
					intFont);
			data.setAlignment(Element.ALIGN_RIGHT);
			document.add(data);

			for (int i = 0; i < 2; i++)
				document.add(espaco);

			Paragraph identificacao = new Paragraph(String.format("AUTOS: %s", processo.getAutos()),
					titFont);
			document.add(identificacao);
			for (Consumidor c : processo.getConsumidores()) {
				identificacao = new Paragraph(String.format("CONSUMIDOR: %s", c.getDenominacao()),
						titFont);
				document.add(identificacao);
			}
			for (Fornecedor f : processo.getFornecedores()) {
				identificacao = new Paragraph(String.format("FORNECEDOR: %s", f.getRazaoSocial()),
						titFont);
				document.add(identificacao);
			}

			for (int i = 0; i < 2; i++)
				document.add(espaco);

			Paragraph conteudo = new Paragraph("PREZADO SENHOR", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
			document.add(espaco);
			conteudo = new Paragraph(
					"    Comunicamos que em razão da reclamação formulada por "
							+ "Vossa Senhoria contra o(s) fornecedor(es) supracitado(s), "
							+ "foi designada audiência conciliatória conforme especificado:",
					intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
			document.add(espaco);

			conteudo = new Paragraph(
					"DATA: " + movimento.getAuxD().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
							+ " - " + "HORÁRIO: "
							+ movimento.getAuxT().format(DateTimeFormatter.ofPattern("HH:mm")),
					titFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
			conteudo = new Paragraph(
					"LOCAL: Rua Luiz Favreto, nº 10, sala 07, Centro, Pato Branco, PR.", titFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
			document.add(espaco);

			conteudo = new Paragraph("ATENÇÃO:", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
			conteudo = new Paragraph("É permitida a entrada, no período da manhã, exclusivamente, "
					+ "na data e hora marcadas acima.", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
			conteudo = new Paragraph(
					"Não havendo comparecimento na audiência acima, "
							+ "o processo será arquivado como 'encerrado' e o nome do "
							+ "fornecedor não constará do Cadastro de Defesa do Consumidor.",
					intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
			document.add(espaco);

			conteudo = new Paragraph("Atenciosamente", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);

			for (int i = 0; i < 3; i++)
				document.add(espaco);

			Paragraph assinatura = new Paragraph("________________________", titFont);
			assinatura.setAlignment(Element.ALIGN_CENTER);
			document.add(assinatura);
			assinatura = new Paragraph("PROCON - Pato Branco, PR", titFont);
			assinatura.setAlignment(Element.ALIGN_CENTER);
			document.add(assinatura);

			document.close();

			return new ByteArrayInputStream(output.toByteArray());
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"erro no relatório!", e.getCause());
		}
	}

}
