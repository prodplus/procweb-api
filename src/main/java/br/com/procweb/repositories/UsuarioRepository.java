package br.com.procweb.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.procweb.models.Usuario;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	Page<Usuario> findAllByAtivo(boolean ativo, Pageable pageable);

	Optional<Usuario> findByEmail(String email);

}
