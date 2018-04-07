package edu.gatech.hpan.expmanager.service.impl;

import edu.gatech.hpan.expmanager.service.SimulationRunService;
import edu.gatech.hpan.expmanager.domain.SimulationRun;
import edu.gatech.hpan.expmanager.repository.SimulationRunRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing SimulationRun.
 */
@Service
@Transactional
public class SimulationRunServiceImpl implements SimulationRunService {

    private final Logger log = LoggerFactory.getLogger(SimulationRunServiceImpl.class);

    private final SimulationRunRepository simulationRunRepository;

    public SimulationRunServiceImpl(SimulationRunRepository simulationRunRepository) {
        this.simulationRunRepository = simulationRunRepository;
    }

    /**
     * Save a simulationRun.
     *
     * @param simulationRun the entity to save
     * @return the persisted entity
     */
    @Override
    public SimulationRun save(SimulationRun simulationRun) {
        log.debug("Request to save SimulationRun : {}", simulationRun);
        return simulationRunRepository.save(simulationRun);
    }

    /**
     * Get all the simulationRuns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SimulationRun> findAll(Pageable pageable) {
        log.debug("Request to get all SimulationRuns");
        return simulationRunRepository.findAll(pageable);
    }

    /**
     * Get one simulationRun by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SimulationRun findOne(Long id) {
        log.debug("Request to get SimulationRun : {}", id);
        return simulationRunRepository.findOne(id);
    }

    /**
     * Delete the simulationRun by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SimulationRun : {}", id);
        simulationRunRepository.delete(id);
    }
}
