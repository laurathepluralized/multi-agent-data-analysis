package edu.gatech.hpan.expmanager.service;

import edu.gatech.hpan.expmanager.domain.Experiment1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Experiment1.
 */
public interface Experiment1Service {

    /**
     * Save a experiment1.
     *
     * @param experiment1 the entity to save
     * @return the persisted entity
     */
    Experiment1 save(Experiment1 experiment1);

    /**
     * Get all the experiment1S.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Experiment1> findAll(Pageable pageable);

    /**
     * Get the "id" experiment1.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Experiment1 findOne(Long id);

    /**
     * Delete the "id" experiment1.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
