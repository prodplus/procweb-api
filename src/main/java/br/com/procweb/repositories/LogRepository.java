package br.com.procweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.procweb.models.Log;

/**
 * 
 * @author ©Marlon F. Garcia
 *
 */
@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {

}
