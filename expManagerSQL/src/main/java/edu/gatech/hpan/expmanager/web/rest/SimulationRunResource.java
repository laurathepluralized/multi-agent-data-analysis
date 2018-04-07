package edu.gatech.hpan.expmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import edu.gatech.hpan.expmanager.domain.SimulationRun;
import edu.gatech.hpan.expmanager.service.SimulationRunService;
import edu.gatech.hpan.expmanager.web.rest.errors.BadRequestAlertException;
import edu.gatech.hpan.expmanager.web.rest.util.HeaderUtil;
import edu.gatech.hpan.expmanager.web.rest.util.PaginationUtil;
import edu.gatech.hpan.expmanager.service.dto.SimulationRunCriteria;
import edu.gatech.hpan.expmanager.service.SimulationRunQueryService;
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
 * REST controller for managing SimulationRun.
 */
@RestController
@RequestMapping("/api")
public class SimulationRunResource {

    private final Logger log = LoggerFactory.getLogger(SimulationRunResource.class);

    private static final String ENTITY_NAME = "simulationRun";

    private final SimulationRunService simulationRunService;

    private final SimulationRunQueryService simulationRunQueryService;

    public SimulationRunResource(SimulationRunService simulationRunService, SimulationRunQueryService simulationRunQueryService) {
        this.simulationRunService = simulationRunService;
        this.simulationRunQueryService = simulationRunQueryService;
    }

    /**
     * POST  /simulation-runs : Create a new simulationRun.
     *
     * @param simulationRun the simulationRun to create
     * @return the ResponseEntity with status 201 (Created) and with body the new simulationRun, or with status 400 (Bad Request) if the simulationRun has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/simulation-runs")
    @Timed
    public ResponseEntity<SimulationRun> createSimulationRun(@RequestBody SimulationRun simulationRun) throws URISyntaxException {
        log.debug("REST request to save SimulationRun : {}", simulationRun);
        if (simulationRun.getId() != null) {
            throw new BadRequestAlertException("A new simulationRun cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SimulationRun result = simulationRunService.save(simulationRun);
        return ResponseEntity.created(new URI("/api/simulation-runs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /simulation-runs : Updates an existing simulationRun.
     *
     * @param simulationRun the simulationRun to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated simulationRun,
     * or with status 400 (Bad Request) if the simulationRun is not valid,
     * or with status 500 (Internal Server Error) if the simulationRun couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/simulation-runs")
    @Timed
    public ResponseEntity<SimulationRun> updateSimulationRun(@RequestBody SimulationRun simulationRun) throws URISyntaxException {
        log.debug("REST request to update SimulationRun : {}", simulationRun);
        if (simulationRun.getId() == null) {
            return createSimulationRun(simulationRun);
        }
        SimulationRun result = simulationRunService.save(simulationRun);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, simulationRun.getId().toString()))
            .body(result);
    }

    /**
     * GET  /simulation-runs : get all the simulationRuns.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of simulationRuns in body
     */
    @GetMapping("/simulation-runs")
    @Timed
    public ResponseEntity<List<SimulationRun>> getAllSimulationRuns(SimulationRunCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SimulationRuns by criteria: {}", criteria);
        Page<SimulationRun> page = simulationRunQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/simulation-runs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /simulation-runs/:id : get the "id" simulationRun.
     *
     * @param id the id of the simulationRun to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the simulationRun, or with status 404 (Not Found)
     */
    @GetMapping("/simulation-runs/{id}")
    @Timed
    public ResponseEntity<SimulationRun> getSimulationRun(@PathVariable Long id) {
        log.debug("REST request to get SimulationRun : {}", id);
        SimulationRun simulationRun = simulationRunService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(simulationRun));
    }

    /**
     * DELETE  /simulation-runs/:id : delete the "id" simulationRun.
     *
     * @param id the id of the simulationRun to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/simulation-runs/{id}")
    @Timed
    public ResponseEntity<Void> deleteSimulationRun(@PathVariable Long id) {
        log.debug("REST request to delete SimulationRun : {}", id);
        simulationRunService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
