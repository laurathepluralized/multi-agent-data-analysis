package edu.gatech.hpan.expmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import edu.gatech.hpan.expmanager.domain.Experiment1;
import edu.gatech.hpan.expmanager.service.Experiment1Service;
import edu.gatech.hpan.expmanager.web.rest.errors.BadRequestAlertException;
import edu.gatech.hpan.expmanager.web.rest.util.HeaderUtil;
import edu.gatech.hpan.expmanager.web.rest.util.PaginationUtil;
import edu.gatech.hpan.expmanager.service.dto.Experiment1Criteria;
import edu.gatech.hpan.expmanager.service.Experiment1QueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Experiment1.
 */
@RestController
@RequestMapping("/api")
public class Experiment1Resource {

    private final Logger log = LoggerFactory.getLogger(Experiment1Resource.class);

    private static final String ENTITY_NAME = "experiment1";

    private final Experiment1Service experiment1Service;

    private final Experiment1QueryService experiment1QueryService;

    public Experiment1Resource(Experiment1Service experiment1Service, Experiment1QueryService experiment1QueryService) {
        this.experiment1Service = experiment1Service;
        this.experiment1QueryService = experiment1QueryService;
    }

    /**
     * POST  /experiment-1-s : Create a new experiment1.
     *
     * @param experiment1 the experiment1 to create
     * @return the ResponseEntity with status 201 (Created) and with body the new experiment1, or with status 400 (Bad Request) if the experiment1 has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/experiment-1-s")
    @Timed
    public ResponseEntity<Experiment1> createExperiment1(@RequestBody Experiment1 experiment1) throws URISyntaxException {
        log.debug("REST request to save Experiment1 : {}", experiment1);
        if (experiment1.getId() != null) {
            throw new BadRequestAlertException("A new experiment1 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Experiment1 result = experiment1Service.save(experiment1);
        return ResponseEntity.created(new URI("/api/experiment-1-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /experiment-1-s : Updates an existing experiment1.
     *
     * @param experiment1 the experiment1 to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated experiment1,
     * or with status 400 (Bad Request) if the experiment1 is not valid,
     * or with status 500 (Internal Server Error) if the experiment1 couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/experiment-1-s")
    @Timed
    public ResponseEntity<Experiment1> updateExperiment1(@RequestBody Experiment1 experiment1) throws URISyntaxException {
        log.debug("REST request to update Experiment1 : {}", experiment1);
        if (experiment1.getId() == null) {
            return createExperiment1(experiment1);
        }
        Experiment1 result = experiment1Service.save(experiment1);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, experiment1.getId().toString()))
            .body(result);
    }

    /**
     * GET  /experiment-1-s : get all the experiment1S.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of experiment1S in body
     */
    @GetMapping("/experiment-1-s")
    @Timed
    public ResponseEntity<List<Experiment1>> getAllExperiment1S(Experiment1Criteria criteria, Pageable pageable) {
        log.debug("REST request to get Experiment1S by criteria: {}", criteria);
        Page<Experiment1> page = experiment1QueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/experiment-1-s");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /experiment-1-s/:id : get the "id" experiment1.
     *
     * @param id the id of the experiment1 to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the experiment1, or with status 404 (Not Found)
     */
    @GetMapping("/experiment-1-s/{id}")
    @Timed
    public ResponseEntity<Experiment1> getExperiment1(@PathVariable Long id) {
        log.debug("REST request to get Experiment1 : {}", id);
        Experiment1 experiment1 = experiment1Service.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(experiment1));
    }

    /**
     * DELETE  /experiment-1-s/:id : delete the "id" experiment1.
     *
     * @param id the id of the experiment1 to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/experiment-1-s/{id}")
    @Timed
    public ResponseEntity<Void> deleteExperiment1(@PathVariable Long id) {
        log.debug("REST request to delete Experiment1 : {}", id);
        experiment1Service.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
