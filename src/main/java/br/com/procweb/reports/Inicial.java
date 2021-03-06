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
import br.com.procweb.models.enums.TipoPessoa;
import br.com.procweb.utils.LocalDateUtils;
import br.com.procweb.utils.MascaraUtils;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
public class Inicial {

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
			Paragraph data = new Paragraph(String.format("Pato Branco, %02d de %s de %d",
					processo.getData().getDayOfMonth(),
					LocalDateUtils.getMesExtenso(processo.getData().getMonthValue()),
					processo.getData().getYear()), intFont);
			data.setAlignment(Element.ALIGN_RIGHT);
			document.add(data);

			Paragraph identificacao = new Paragraph("Autos nº: " + processo.getAutos(), intFont);
			identificacao.setAlignment(Element.ALIGN_LEFT);
			document.add(identificacao);
			for (Consumidor c : processo.getConsumidores()) {
				identificacao = new Paragraph("Consumidor(a): " + c.getDenominacao(), intFont);
				identificacao.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(identificacao);
				identificacao = new Paragraph("Endereço: " + c.getEndereco().getLogradouro() + ", "
						+ c.getEndereco().getNumero() + ", " + c.getEndereco().getComplemento()
						+ ", " + c.getEndereco().getBairro() + ", " + c.getEndereco().getMunicipio()
						+ ", " + c.getEndereco().getUf() + ", CEP " + c.getEndereco().getCep(),
						intFont);
				identificacao.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(identificacao);
				StringBuilder builder = new StringBuilder();
				for (String f : c.getFones()) {
					if (f.charAt(2) == '9')
						builder.append(MascaraUtils.format("(##) #####-####", f) + "  ");
					else
						builder.append(MascaraUtils.format("(##) ####-####", f) + "  ");
				}
				identificacao = new Paragraph("Fone: " + builder.toString(), intFont);
				identificacao.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(identificacao);
			}
			for (Fornecedor f : processo.getFornecedores()) {
				identificacao = new Paragraph(
						"Fornecedor: " + f.getFantasia() + " (" + f.getRazaoSocial() + ")",
						intFont);
				identificacao.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(identificacao);
			}

			for (int i = 0; i < 2; i++)
				document.add(espaco);

			Paragraph titulo = new Paragraph("HISTÓRICO DA RECLAMAÇÃO", intFont);
			titulo.setAlignment(Element.ALIGN_CENTER);
			document.add(titulo);
			document.add(espaco);

			StringBuilder builder1 = new StringBuilder();
			builder1.append("      O(a) consumidor(a) ");
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

			Paragraph conteudo = new Paragraph(processo.getRelato(), intFont);
			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(conteudo);

			for (int i = 0; i < 2; i++)
				document.add(espaco);

			conteudo = new Paragraph(
					"Consumidor                                               PROCON", intFont);
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
