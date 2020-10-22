package br.com.procweb.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
public class DespachoAud {
	
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
			document.add(espaco);

			Paragraph cabecaP = new Paragraph("DESPACHO ADMINISTRATIVO", cabeca);
			cabecaP.setAlignment(Element.ALIGN_CENTER);
			document.add(cabecaP);

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

			for (int i = 0; i < 3; i++)
				document.add(espaco);

			Paragraph conteudo = new Paragraph("I - Na forma do Art. 33 do Decreto Federal 2.181, "
					+ "instaurem-se procedimento administrativo;", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
			document.add(espaco);
			String dois = String.format(
					"II - Expeça-se notificação, pessoalmente ou por AR, para que as "
							+ "partes compareçam EM AUDIÊNCIA CONCILIATÓRIA A SER REALIZADA "
							+ "NESTE ÓRGÃO EM %02d/%02d/%04d às %02d:%02d, facultado a "
							+ "empresa fornecedora solucionar, comprovadamente a este Órgão, "
							+ "a reclamação da consumidora 48h anteriores a audiência designada;",
					movimento.getAuxD().getDayOfMonth(), movimento.getAuxD().getMonthValue(),
					movimento.getAuxD().getYear(), movimento.getAuxT().getHour(),
					movimento.getAuxT().getMinute());
			conteudo = new Paragraph(dois, intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
			document.add(espaco);
			conteudo = new Paragraph(
					"III - Determino que, caso ocorra conciliação e a fornecedora se "
							+ "comprometer a informar nos autos o cumprimento do acordo e não "
							+ "o fizer, aplica-se multa no valor de 10 (dez) UFM's, de acordo "
							+ "com o Art. 55, §4º do CDC;",
					intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
			document.add(espaco);
			conteudo = new Paragraph("IV - Para tanto, designo o servidor Neri Antonio Garbin para "
					+ "realizar a audiência conciliatória.", intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);
			document.add(espaco);

			for (int i = 0; i < 3; i++)
				document.add(espaco);

			// data
			Paragraph data = new Paragraph(String.format("Pato Branco, %02d de %s de %d",
					movimento.getData().getDayOfMonth(),
					LocalDateUtils.getMesExtenso(movimento.getData().getMonthValue()),
					movimento.getData().getYear()), intFont);
			data.setAlignment(Element.ALIGN_RIGHT);
			document.add(data);

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
