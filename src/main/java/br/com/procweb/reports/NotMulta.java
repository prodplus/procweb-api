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

import br.com.procweb.models.Fornecedor;
import br.com.procweb.models.Processo;
import br.com.procweb.utils.LocalDateUtils;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
public class NotMulta {

	public static ByteArrayInputStream gerar(Processo processo, Fornecedor fornecedor) {
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

			for (int i = 0; i < 3; i++)
				document.add(espaco);

			Paragraph titulo = new Paragraph("NOTIFICAÇÃO PARA RECOLHIMENTO DE MULTA", titFont);
			titulo.setAlignment(Element.ALIGN_CENTER);
			document.add(titulo);
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

			Paragraph identificacao = new Paragraph("Autos nº: " + processo.getAutos(), titFont);
			identificacao.setAlignment(Element.ALIGN_LEFT);
			document.add(identificacao);
			identificacao = new Paragraph("Fornecedor: " + fornecedor.getFantasia() + " ("
					+ fornecedor.getRazaoSocial() + ")", titFont);
			identificacao.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(identificacao);

			for (int i = 0; i < 2; i++)
				document.add(espaco);

			Paragraph conteudo = new Paragraph("Prezado(a) Senhor(a)", intFont);
			conteudo.setAlignment(Element.ALIGN_LEFT);
			conteudo.setFirstLineIndent(30f);
			document.add(conteudo);
			document.add(espaco);

			conteudo = new Paragraph("Nos termos do Art. 56, § único da Lei 8078/90, "
					+ "combinado com o Art. 46, §2º do Decreto Federal 2181/97 e "
					+ "da Lei Municipal 2120/2001, NOTIFICAMOS Vossa Senhoria para "
					+ "efetuar o recolhimento da multa fixada na decisão administrativa "
					+ "em anexo, ou requerendo, apresentar recurso, no prazo máximo "
					+ "de 10 (dez) dias contados a partir do recebimento desta.", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			conteudo.setLeading(25f);
			conteudo.setFirstLineIndent(30f);
			document.add(conteudo);
			document.add(espaco);

			conteudo = new Paragraph("Notificamos ainda que, caso não haja apresentação "
					+ "de recurso no prazo acima, nem o recolhimento do valor "
					+ "da multa arbitrada, será o débito inscrito em dívida ativa, "
					+ "para subsequente cobrança executiva.", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			conteudo.setLeading(25f);
			conteudo.setFirstLineIndent(30f);
			document.add(conteudo);
			document.add(espaco);

			for (int i = 0; i < 2; i++)
				document.add(espaco);

			conteudo = new Paragraph("Atenciosamente", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			conteudo.setFirstLineIndent(5);
			document.add(conteudo);

			for (int i = 0; i < 3; i++)
				document.add(espaco);

			conteudo = new Paragraph("_______________________", intFont);
			conteudo.setAlignment(Element.ALIGN_CENTER);
			document.add(conteudo);
			conteudo = new Paragraph("PROCON - Pato Branco", intFont);
			conteudo.setAlignment(Element.ALIGN_CENTER);
			document.add(conteudo);

			document.newPage();

			conteudo = new Paragraph("Expedi, via AR, com cópias do Despacho "
					+ "Administrativo e Decisão Administrativa.", intFont);
			conteudo.setAlignment(Element.ALIGN_CENTER);
			document.add(conteudo);

			document.close();

			return new ByteArrayInputStream(output.toByteArray());
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"erro no relatório!", e.getCause());
		}
	}

}
