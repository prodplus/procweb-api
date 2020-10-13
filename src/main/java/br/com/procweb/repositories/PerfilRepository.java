package br.com.procweb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.procweb.models.Perfil;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
	
	Optional<Perfil> findByRole(String role);

}
