package edu.gatech.hpan.expmanager.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import edu.gatech.hpan.expmanager.domain.Experiment1;
import edu.gatech.hpan.expmanager.domain.*; // for static metamodels
import edu.gatech.hpan.expmanager.repository.Experiment1Repository;
import edu.gatech.hpan.expmanager.service.dto.Experiment1Criteria;


/**
 * Service for executing complex queries for Experiment1 entities in the database.
 * The main input is a {@link Experiment1Criteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Experiment1} or a {@link Page} of {@link Experiment1} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class Experiment1QueryService extends QueryService<Experiment1> {

    private final Logger log = LoggerFactory.getLogger(Experiment1QueryService.class);


    private final Experiment1Repository experiment1Repository;

    public Experiment1QueryService(Experiment1Repository experiment1Repository) {
        this.experiment1Repository = experiment1Repository;
    }

    /**
     * Return a {@link List} of {@link Experiment1} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Experiment1> findByCriteria(Experiment1Criteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Experiment1> specification = createSpecification(criteria);
        return experiment1Repository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Experiment1} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Experiment1> findByCriteria(Experiment1Criteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Experiment1> specification = createSpecification(criteria);
        return experiment1Repository.findAll(specification, page);
    }

    /**
     * Function to convert Experiment1Criteria to a {@link Specifications}
     */
    private Specifications<Experiment1> createSpecification(Experiment1Criteria criteria) {
        Specifications<Experiment1> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Experiment1_.id));
            }
            if (criteria.getIndex() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIndex(), Experiment1_.index));
            }
        }
        return specification;
    }

}
