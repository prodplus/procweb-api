package br.com.procweb.utils;

import java.util.List;
import java.util.stream.Collectors;

import br.com.procweb.models.Processo;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
public class GeradorAutos {

	/**
	 * Gera a string com o número dos autos.
	 * 
	 * @param processos Lista de todos os processos cadastrados.
	 * @param ano       Ano requerido do processo.
	 * @return String representando os autos.
	 */
	public static String getAutos(List<Processo> processos, Integer ano) {
		List<Processo> processosAno = processos.stream().filter(p -> p.getData().getYear() == ano)
				.collect(Collectors.toList());

		StringBuilder builder = new StringBuilder();
		Integer contador = 0;

		if (processosAno != null && !processosAno.isEmpty()) {
			for (Processo proc : processosAno) {
				Integer tmp = Integer.valueOf(proc.getAutos().split("/")[0]);
				if (tmp > contador) {
					builder = new StringBuilder();
					builder.append(String.format("%03d/%04d", tmp + 1, ano));
				}
			}
		} else {
			builder = new StringBuilder();
			builder.append(String.format("%03d/%04d", 1, ano));
		}

		return builder.toString();
	}

}
