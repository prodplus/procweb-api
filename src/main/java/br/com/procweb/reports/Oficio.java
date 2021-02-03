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
			Font negFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			negFont.setSize(12);
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

			Paragraph subTit = new Paragraph(String.format("DADOS DO(S) CONSUMIDOR(ES)"), negFont);
			subTit.setAlignment(Element.ALIGN_LEFT);
			document.add(subTit);

			// identificação dos consumidores
			for (Consumidor c : processo.getConsumidores()) {
				StringBuilder builder = new StringBuilder();
				for (String f : c.getFones()) {
					if (f.charAt(2) == '9')
						builder.append(MascaraUtils.format("(##) #####-####", f) + "  ");
					else
						builder.append(MascaraUtils.format("(##) ####-####", f) + "  ");
				}
				String cadastro = c.getTipo().equals(TipoPessoa.FISICA)
						? MascaraUtils.format("###.###.###-##", c.getCadastro())
						: MascaraUtils.format("##.###.###/####-##", c.getCadastro());
				Paragraph cons = new Paragraph(String.format(
						"Nome: %s, CPF/CNPJ: %s, Endereço: %s, nº %s, %s, %s, %s, %s, CEP: %s, Fone: %s, email: %s",
						c.getDenominacao(), cadastro, c.getEndereco().getLogradouro(),
						c.getEndereco().getNumero(),
						c.getEndereco().getComplemento() != null ? c.getEndereco().getComplemento()
								: "",
						c.getEndereco().getBairro(), c.getEndereco().getMunicipio(),
						c.getEndereco().getUf(), c.getEndereco().getCep(), builder.toString(),
						c.getEmail()), intFont);
				cons.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(cons);
			}

			document.add(espaco);

			subTit = new Paragraph(String.format("DADOS DO(S) FORNECEDOR(ES)"), negFont);
			subTit.setAlignment(Element.ALIGN_LEFT);
			document.add(subTit);

			// identificação dos fornecedores
			for (Fornecedor f : processo.getFornecedores()) {
				StringBuilder endBuilder = new StringBuilder();
				if (f.getEndereco() != null) {
					if (f.getEndereco().getLogradouro() != null)
						endBuilder.append(f.getEndereco().getLogradouro() + ", ");
					if (f.getEndereco().getNumero() != null)
						endBuilder.append(f.getEndereco().getNumero() + ", ");
					if (f.getEndereco().getComplemento() != null)
						endBuilder.append(f.getEndereco().getComplemento() + ", ");
					if (f.getEndereco().getBairro() != null)
						endBuilder.append(f.getEndereco().getBairro() + ", ");
					if (f.getEndereco().getMunicipio() != null)
						endBuilder.append(f.getEndereco().getMunicipio() + ", ");
					if (f.getEndereco().getUf() != null)
						endBuilder.append(f.getEndereco().getUf());
				} else {
					endBuilder.append("");
				}
				Paragraph forn = new Paragraph(String.format(
						"Razão Social: %s, Nome Fantasia: %s, CNPJ/CPF: %s, Endereço: %s",
						f.getRazaoSocial(), f.getFantasia(),
						MascaraUtils.format("##.###.###/####-##", f.getCnpj()),
						endBuilder.toString()), intFont);
				forn.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(forn);
			}

			document.add(espaco);

			String primeiroPara = "O consumidor acima qualificado compareceu a este PROCON/PR e, "
					+ "apresentando documentação pertinente a reclamação ora formulada, relata os "
					+ "seguintes fatos:";
			Paragraph fixos = new Paragraph(primeiroPara, intFont);
			fixos.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(fixos);
			document.add(espaco);

			// relato dos fatos
			if (processo.getRelato() != null && !processo.getRelato().isEmpty()) {
				Paragraph conteudo = new Paragraph(processo.getRelato(), intFont);
				conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(conteudo);
			} else {
				Paragraph conteudo = new Paragraph("(RELATO INICIAL EM ANEXO)", intFont);
				conteudo.setAlignment(Element.ALIGN_CENTER);
				for (int i = 0; i < 6; i++)
					document.add(espaco);
				document.add(conteudo);
				for (int i = 0; i < 6; i++)
					document.add(espaco);
			}

			document.add(espaco);

			String segundoPara = "Diante do exposto, com base no § 1º do art. 33 do Decreto "
					+ "Federal nº 2187/97, encaminhamos o consumidor citado acima para a "
					+ "resolução do problema relatado, já tendo sido verificada a presença "
					+ "de indícios de procedência, no prazo de 10 (dez) dias.";
			fixos = new Paragraph(segundoPara, intFont);
			fixos.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(fixos);
			document.add(espaco);

			Paragraph aviso = new Paragraph(
					"AS RESPOSTAS DEVEM SER DIRIGIDAS A ESTE PROCON COM PROPOSIÇÃO RESOLUTIVA PARA O CASO.",
					negFont);
			aviso.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(aviso);
			document.add(espaco);

			String tercPara = "Decorrido o prazo, e não havendo solução da reclamação apresentada, "
					+ "este órgão irá instaurar processo administrativo para apurar eventual "
					+ "infração à Lei 8.078/90, bem como para apreciar a fundamentação da "
					+ "reclamação do consumidor, para efeito de sua inclusão nos Cadastros Estadual "
					+ "e Nacional de Reclamação Fundamentada, nos termos do art. 44 do Código "
					+ "de Defesa do Consumidor.";
			fixos = new Paragraph(tercPara, intFont);
			fixos.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(fixos);
			document.add(espaco);

			// data
			Paragraph data = new Paragraph(
					String.format("Pato Branco, %02d de %s de %d", LocalDate.now().getDayOfMonth(),
							LocalDateUtils.getMesExtenso(LocalDate.now().getMonthValue()),
							LocalDate.now().getYear()),
					intFont);
			data.setAlignment(Element.ALIGN_LEFT);
			document.add(data);

			for (int i = 0; i < 2; i++)
				document.add(espaco);

			Paragraph notificacao = new Paragraph("Procon Pato Branco - PR", intFont);
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
