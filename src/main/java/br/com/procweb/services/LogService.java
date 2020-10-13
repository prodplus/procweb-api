package br.com.procweb.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.procweb.models.Log;
import br.com.procweb.models.Usuario;
import br.com.procweb.models.enums.TipoLog;
import br.com.procweb.repositories.LogRepository;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Service
public class LogService {

	@Autowired
	private LogRepository logRepository;

	public void insereLog(TipoLog tipo, Integer id, String entidade) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = (Usuario) auth.getPrincipal();
		String mensagem;
		switch (tipo) {
		case INSERCAO:
			mensagem = "inserido";
			break;
		case ATUALIZACAO:
			mensagem = "atualizado";
			break;
		case EXCLUSAO:
			mensagem = "excluído!";
			break;
		default:
			mensagem = "alterado";
		}

		this.logRepository.save(new Log(null, LocalDateTime.now(), usuario,
				String.format("%d %s %s", id, entidade, mensagem), tipo));
	}

}
