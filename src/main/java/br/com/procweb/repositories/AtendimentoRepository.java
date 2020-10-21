package br.com.procweb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.procweb.models.Atendimento;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Integer> {

	Page<Atendimento> findAllByConsumidoresCadastroOrFornecedoresCnpj(String parametro,
			String parametro2, Pageable pageable);

	Page<Atendimento> findAllByConsumidoresDenominacaoContainingIgnoreCaseOrFornecedoresFantasiaContainingIgnoreCaseOrFornecedoresRazaoSocialContainingIgnoreCase(
			String parametro, String parametro2, String parametro3, Pageable pageable);

}
