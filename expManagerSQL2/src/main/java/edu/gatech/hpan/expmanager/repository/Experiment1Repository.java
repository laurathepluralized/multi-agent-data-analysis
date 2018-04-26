package edu.gatech.hpan.expmanager.repository;

import edu.gatech.hpan.expmanager.domain.Experiment1;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Experiment1 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Experiment1Repository extends JpaRepository<Experiment1, Long>, JpaSpecificationExecutor<Experiment1> {

}
