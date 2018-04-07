package edu.gatech.hpan.expmanager.service;

import edu.gatech.hpan.expmanager.domain.SimulationRun;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing SimulationRun.
 */
public interface SimulationRunService {

    /**
     * Save a simulationRun.
     *
     * @param simulationRun the entity to save
     * @return the persisted entity
     */
    SimulationRun save(SimulationRun simulationRun);

    /**
     * Get all the simulationRuns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SimulationRun> findAll(Pageable pageable);

    /**
     * Get the "id" simulationRun.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SimulationRun findOne(Long id);

    /**
     * Delete the "id" simulationRun.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
