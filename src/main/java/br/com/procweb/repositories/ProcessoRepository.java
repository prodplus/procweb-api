package br.com.procweb.repositories;

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

}
