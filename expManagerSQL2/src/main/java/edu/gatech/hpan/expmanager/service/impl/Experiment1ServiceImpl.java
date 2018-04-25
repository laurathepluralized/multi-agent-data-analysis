package edu.gatech.hpan.expmanager.service.impl;

import edu.gatech.hpan.expmanager.service.Experiment1Service;
import edu.gatech.hpan.expmanager.domain.Experiment1;
import edu.gatech.hpan.expmanager.repository.Experiment1Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Experiment1.
 */
@Service
@Transactional
public class Experiment1ServiceImpl implements Experiment1Service {

    private final Logger log = LoggerFactory.getLogger(Experiment1ServiceImpl.class);

    private final Experiment1Repository experiment1Repository;

    public Experiment1ServiceImpl(Experiment1Repository experiment1Repository) {
        this.experiment1Repository = experiment1Repository;
    }

    /**
     * Save a experiment1.
     *
     * @param experiment1 the entity to save
     * @return the persisted entity
     */
    @Override
    public Experiment1 save(Experiment1 experiment1) {
        log.debug("Request to save Experiment1 : {}", experiment1);
        return experiment1Repository.save(experiment1);
    }

    /**
     * Get all the experiment1S.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Experiment1> findAll(Pageable pageable) {
        log.debug("Request to get all Experiment1S");
        return experiment1Repository.findAll(pageable);
    }

    /**
     * Get one experiment1 by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Experiment1 findOne(Long id) {
        log.debug("Request to get Experiment1 : {}", id);
        return experiment1Repository.findOne(id);
    }

    /**
     * Delete the experiment1 by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Experiment1 : {}", id);
        experiment1Repository.delete(id);
    }
}
