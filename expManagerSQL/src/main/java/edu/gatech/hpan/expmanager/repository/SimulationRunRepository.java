package edu.gatech.hpan.expmanager.repository;

import edu.gatech.hpan.expmanager.domain.SimulationRun;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SimulationRun entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SimulationRunRepository extends JpaRepository<SimulationRun, Long>, JpaSpecificationExecutor<SimulationRun> {

}
