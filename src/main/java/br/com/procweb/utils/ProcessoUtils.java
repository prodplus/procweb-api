package br.com.procweb.utils;

import java.util.Collections;
import java.util.List;

import br.com.procweb.models.auxiliares.Movimento;
import br.com.procweb.models.enums.Situacao;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
public class ProcessoUtils {

	/**
	 * Verifica última localização para ajustar o processo.
	 * 
	 * @param movimentacao
	 * @return
	 */
	public static Situacao handleSituacao(List<Movimento> movimentacao) {
		if (movimentacao.isEmpty())
			return Situacao.AUTUADO;
		Collections.sort(movimentacao);
		Situacao para = movimentacao.get(0).getPara();
		if (para.equals(Situacao.DESPACHO))
			return Situacao.AUTUADO;
		return para;
	}

}
