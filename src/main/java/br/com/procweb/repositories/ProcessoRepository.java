package br.com.procweb.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.procweb.models.Processo;
import br.com.procweb.models.enums.Situacao;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Integer> {

	Page<Processo> findAllByAutosContaining(String autos, Pageable pageable);

	Page<Processo> findAllByConsumidoresDenominacaoContainingIgnoreCaseOrConsumidoresCadastroContaining(
			String param, String param2, Pageable pageable);

	Page<Processo> findAllByFornecedoresFantasiaContainingIgnoreCaseOrFornecedoresRazaoSocialContainingIgnoreCase(
			String param, String param2, Pageable pageable);

	Page<Processo> findAllBySituacao(Situacao situacao, Pageable pageable);

	List<Processo> findAllByDataBetween(LocalDate inicio, LocalDate fim);

	List<Processo> findAllBySituacao(Situacao situacao);

	List<Processo> findAllByAutosContainingAndSituacao(String autos, Situacao situacao);

	Page<Processo> findAllBySituacaoOrSituacao(Situacao situacao, Situacao situacao2,
			Pageable pageable);

	Page<Processo> findAllBySituacaoOrSituacaoAndAutosContaining(Situacao situacao,
			Situacao situacao2, String autos, Pageable pageable);

}
