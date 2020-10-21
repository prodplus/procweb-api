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
public class ConvAudForn {

	public static ByteArrayInputStream gerar(Processo processo, Movimento movimento) {
		Document document = new Document(PageSize.A4);
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(document, output);
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

			document.add(espaco);

			Paragraph conteudo = new Paragraph("PREZADOS SENHORES", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
			document.add(espaco);
			conteudo = new Paragraph("Comunicamos que foi registrada reclamação contra esse "
					+ "fornecedor, por infração aos dispositivos da Lei Federal "
					+ "nº 8.078/90, de 11 de setembro de 1990 e do Decreto Federal "
					+ "nº 2.181, de 20 de março de 1997, dando origem ao Processo "
					+ "Administrativo neste Órgão, de acordo com o histórico da "
					+ "ocorrência anexo.", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			conteudo.setFirstLineIndent(30f);
			document.add(conteudo);
			conteudo = new Paragraph("Fica Vossa Senhoria NOTIFICADO, nos termos do Art. 55, §4º, "
					+ "da mencionada Lei e Decreto, a comparecer à audiência conciliatória, "
					+ "conforme especificado abaixo, para a qual o representante deverá "
					+ "trazer carta de preposição ou procuração emitidos pela "
					+ "empresa fornecedora.", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			conteudo.setFirstLineIndent(30f);
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

			conteudo = new Paragraph(
					"Outrossim, esclarecemos que, apesar do horário de atendimento "
							+ "deste Órgão ser somente das 13:30h às 17:00h, é permitida a "
							+ "entrada, exclusivamente, na data e hora marcados para a "
							+ "realização da audiência.",
					intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			conteudo.setFirstLineIndent(30f);
			document.add(conteudo);
			document.add(espaco);

			conteudo = new Paragraph("Atenciosamente", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
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
