package br.com.procweb.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.procweb.models.Fornecedor;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {

	Optional<Fornecedor> findByFantasia(String fantasia);

	Page<Fornecedor> findAllByFantasiaContainingIgnoreCaseOrRazaoSocialContainingIgnoreCase(
			String fantasia, String razao, Pageable pageable);

	Page<Fornecedor> findAllByCnpjContaining(String fantasia, Pageable pageable);

}
