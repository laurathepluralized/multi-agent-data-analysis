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

import edu.gatech.hpan.expmanager.domain.SimulationRun;
import edu.gatech.hpan.expmanager.domain.*; // for static metamodels
import edu.gatech.hpan.expmanager.repository.SimulationRunRepository;
import edu.gatech.hpan.expmanager.service.dto.SimulationRunCriteria;


/**
 * Service for executing complex queries for SimulationRun entities in the database.
 * The main input is a {@link SimulationRunCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SimulationRun} or a {@link Page} of {@link SimulationRun} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SimulationRunQueryService extends QueryService<SimulationRun> {

    private final Logger log = LoggerFactory.getLogger(SimulationRunQueryService.class);


    private final SimulationRunRepository simulationRunRepository;

    public SimulationRunQueryService(SimulationRunRepository simulationRunRepository) {
        this.simulationRunRepository = simulationRunRepository;
    }

    /**
     * Return a {@link List} of {@link SimulationRun} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SimulationRun> findByCriteria(SimulationRunCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<SimulationRun> specification = createSpecification(criteria);
        return simulationRunRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SimulationRun} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SimulationRun> findByCriteria(SimulationRunCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<SimulationRun> specification = createSpecification(criteria);
        return simulationRunRepository.findAll(specification, page);
    }

    /**
     * Function to convert SimulationRunCriteria to a {@link Specifications}
     */
    private Specifications<SimulationRun> createSpecification(SimulationRunCriteria criteria) {
        Specifications<SimulationRun> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SimulationRun_.id));
            }
            if (criteria.getSimulationTrial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSimulationTrial(), SimulationRun_.simulationTrial));
            }
            if (criteria.getIndex() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIndex(), SimulationRun_.index));
            }
            if (criteria.getTeam_id() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTeam_id(), SimulationRun_.team_id));
            }
            if (criteria.getScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScore(), SimulationRun_.score));
            }
            if (criteria.getTeamCapture() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTeamCapture(), SimulationRun_.teamCapture));
            }
            if (criteria.getNonTeamCapture() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNonTeamCapture(), SimulationRun_.nonTeamCapture));
            }
            if (criteria.getJob_num() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJob_num(), SimulationRun_.job_num));
            }
            if (criteria.getTask_num() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTask_num(), SimulationRun_.task_num));
            }
            if (criteria.getResults_dir() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResults_dir(), SimulationRun_.results_dir));
            }
            if (criteria.getNum_rows() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNum_rows(), SimulationRun_.num_rows));
            }
            if (criteria.getAlign_weight_t_1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAlign_weight_t_1(), SimulationRun_.align_weight_t_1));
            }
            if (criteria.getAvoid_nonteam_weight_t_1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvoid_nonteam_weight_t_1(), SimulationRun_.avoid_nonteam_weight_t_1));
            }
            if (criteria.getAvoid_team_weight_t_1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvoid_team_weight_t_1(), SimulationRun_.avoid_team_weight_t_1));
            }
            if (criteria.getCentroid_weight_t_1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCentroid_weight_t_1(), SimulationRun_.centroid_weight_t_1));
            }
            if (criteria.getComms_range_t_1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getComms_range_t_1(), SimulationRun_.comms_range_t_1));
            }
            if (criteria.getFov_az_t_1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFov_az_t_1(), SimulationRun_.fov_az_t_1));
            }
            if (criteria.getFov_el_t_1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFov_el_t_1(), SimulationRun_.fov_el_t_1));
            }
            if (criteria.getGoal_weight_t_1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGoal_weight_t_1(), SimulationRun_.goal_weight_t_1));
            }
            if (criteria.getMax_pred_speed_t_1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMax_pred_speed_t_1(), SimulationRun_.max_pred_speed_t_1));
            }
            if (criteria.getMax_speed_t_1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMax_speed_t_1(), SimulationRun_.max_speed_t_1));
            }
            if (criteria.getSphere_of_influence_t_1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSphere_of_influence_t_1(), SimulationRun_.sphere_of_influence_t_1));
            }
            if (criteria.getMotion_model_t_1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMotion_model_t_1(), SimulationRun_.motion_model_t_1));
            }
            if (criteria.getVel_max_t_1() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVel_max_t_1(), SimulationRun_.vel_max_t_1));
            }
            if (criteria.getMotion_model_predator() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMotion_model_predator(), SimulationRun_.motion_model_predator));
            }
            if (criteria.getPitch_rate_max_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPitch_rate_max_predator(), SimulationRun_.pitch_rate_max_predator));
            }
            if (criteria.getTurn_rate_max_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTurn_rate_max_predator(), SimulationRun_.turn_rate_max_predator));
            }
            if (criteria.getVel_max_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVel_max_predator(), SimulationRun_.vel_max_predator));
            }
            if (criteria.getAlign_weight_t_2_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAlign_weight_t_2_predator(), SimulationRun_.align_weight_t_2_predator));
            }
            if (criteria.getAllow_prey_switching_t_2_predator() != null) {
                specification = specification.and(buildSpecification(criteria.getAllow_prey_switching_t_2_predator(), SimulationRun_.allow_prey_switching_t_2_predator));
            }
            if (criteria.getAutonomy_t_2_predator() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAutonomy_t_2_predator(), SimulationRun_.autonomy_t_2_predator));
            }
            if (criteria.getAvoid_nonteam_weight_t_2_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvoid_nonteam_weight_t_2_predator(), SimulationRun_.avoid_nonteam_weight_t_2_predator));
            }
            if (criteria.getAvoid_team_weight_t_2_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvoid_team_weight_t_2_predator(), SimulationRun_.avoid_team_weight_t_2_predator));
            }
            if (criteria.getCapture_range_t_2_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCapture_range_t_2_predator(), SimulationRun_.capture_range_t_2_predator));
            }
            if (criteria.getCentroid_weight_t_2_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCentroid_weight_t_2_predator(), SimulationRun_.centroid_weight_t_2_predator));
            }
            if (criteria.getComms_range_t_2_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getComms_range_t_2_predator(), SimulationRun_.comms_range_t_2_predator));
            }
            if (criteria.getMax_pred_speed_t_2_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMax_pred_speed_t_2_predator(), SimulationRun_.max_pred_speed_t_2_predator));
            }
            if (criteria.getMax_speed_t_2_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMax_speed_t_2_predator(), SimulationRun_.max_speed_t_2_predator));
            }
            if (criteria.getAlign_weight_t_3_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAlign_weight_t_3_predator(), SimulationRun_.align_weight_t_3_predator));
            }
            if (criteria.getAllow_prey_switching_t_3_predator() != null) {
                specification = specification.and(buildSpecification(criteria.getAllow_prey_switching_t_3_predator(), SimulationRun_.allow_prey_switching_t_3_predator));
            }
            if (criteria.getAutonomy_t_3_predator() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAutonomy_t_3_predator(), SimulationRun_.autonomy_t_3_predator));
            }
            if (criteria.getAvoid_nonteam_weight_t_3_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvoid_nonteam_weight_t_3_predator(), SimulationRun_.avoid_nonteam_weight_t_3_predator));
            }
            if (criteria.getAvoid_team_weight_t_3_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvoid_team_weight_t_3_predator(), SimulationRun_.avoid_team_weight_t_3_predator));
            }
            if (criteria.getCapture_range_t_3_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCapture_range_t_3_predator(), SimulationRun_.capture_range_t_3_predator));
            }
            if (criteria.getCentroid_weight_t_3_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCentroid_weight_t_3_predator(), SimulationRun_.centroid_weight_t_3_predator));
            }
            if (criteria.getComms_range_t_3_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getComms_range_t_3_predator(), SimulationRun_.comms_range_t_3_predator));
            }
            if (criteria.getMax_pred_speed_t_3_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMax_pred_speed_t_3_predator(), SimulationRun_.max_pred_speed_t_3_predator));
            }
            if (criteria.getMax_speed_t_3_predator() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMax_speed_t_3_predator(), SimulationRun_.max_speed_t_3_predator));
            }
        }
        return specification;
    }

}
