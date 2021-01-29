package br.com.procweb.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

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

import br.com.procweb.models.Processo;
import br.com.procweb.models.enums.TipoPessoa;
import br.com.procweb.utils.LocalDateUtils;
import br.com.procweb.utils.MascaraUtils;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
public class Oficio {

	public static ByteArrayInputStream gerar(Processo processo) {
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

			for (int i = 0; i < 2; i++)
				document.add(espaco);

			// data
			Paragraph data = new Paragraph(
					String.format("Pato Branco, %02d de %s de %d", LocalDate.now().getDayOfMonth(),
							LocalDateUtils.getMesExtenso(LocalDate.now().getMonthValue()),
							LocalDate.now().getYear()),
					intFont);
			data.setAlignment(Element.ALIGN_RIGHT);
			document.add(data);

			Paragraph identificacao = new Paragraph("Autos nº: " + processo.getAutos(), intFont);
			identificacao.setAlignment(Element.ALIGN_LEFT);
			document.add(identificacao);

			for (int i = 0; i < 2; i++)
				document.add(espaco);

			Paragraph titulo = new Paragraph("Prezados Senhores:", intFont);
			titulo.setAlignment(Element.ALIGN_LEFT);
			document.add(titulo);
			document.add(espaco);

			StringBuilder builder1 = new StringBuilder();
			builder1.append(String.format("      Em %02d de %s de %s, o(a) consumidor(a) ",
					processo.getData().getDayOfMonth(),
					LocalDateUtils.getMesExtenso(processo.getData().getMonthValue()),
					processo.getData().getYear()));
			builder1.append(processo.getConsumidores().get(0).getDenominacao());
			builder1.append(String.format(", %s %s",
					processo.getConsumidores().get(0).getTipo().equals(TipoPessoa.FISICA)
							? "inscrito(a) no CPF nº "
							: " inscrito(a) no CNPJ nº",
					processo.getConsumidores().get(0).getTipo().equals(TipoPessoa.FISICA)
							? MascaraUtils.format("###.###.###-##",
									processo.getConsumidores().get(0).getCadastro())
							: MascaraUtils.format("##.###.###/####-##",
									processo.getConsumidores().get(0).getCadastro())));
			builder1.append(", formalizou reclamação em desfavor da(s) empresa(s) ");
			for (int i = 0; i < processo.getFornecedores().size(); i++) {
				if (i > 0)
					builder1.append(" e ");
				builder1.append(processo.getFornecedores().get(i).getRazaoSocial());
			}
			builder1.append(", alegando em síntese o que segue:");

			titulo = new Paragraph(builder1.toString(), intFont);
			titulo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(titulo);
			document.add(espaco);

			if (processo.getRelato() != null && !processo.getRelato().isEmpty()) {
				Paragraph conteudo = new Paragraph(processo.getRelato(), intFont);
				conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(conteudo);
				document.add(espaco);
			} else {
				Paragraph conteudo = new Paragraph("(RELATO INICIAL EM ANEXO)", intFont);
				conteudo.setAlignment(Element.ALIGN_CENTER);
				for (int i = 0; i < 6; i++)
					document.add(espaco);
				document.add(conteudo);
				for (int i = 0; i < 6; i++)
					document.add(espaco);
			}

			Paragraph notificacao = new Paragraph(
					"      Este ofício deverá ser respondido no prazo de 10 (dez) dias a contar do seu recebimento.",
					titFont);
			notificacao.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(notificacao);

			for (int i = 0; i < 2; i++)
				document.add(espaco);

			notificacao = new Paragraph("Procon Pato Branco - PR", intFont);
			notificacao.setAlignment(Element.ALIGN_CENTER);
			document.add(notificacao);

			document.close();

			return new ByteArrayInputStream(output.toByteArray());
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"erro no relatório!", e.getCause());
		}
	}

}
