package br.com.procweb.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.procweb.models.Consumidor;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Repository
public interface ConsumidorRepository extends JpaRepository<Consumidor, Integer> {

	Page<Consumidor> findAllByDenominacaoContainingIgnoreCase(String denominacao,
			Pageable pageable);

	Optional<Consumidor> findByCadastro(String cadastro);

	Page<Consumidor> findAllByCadastroContaining(String denominacao, Pageable pageable);

}
