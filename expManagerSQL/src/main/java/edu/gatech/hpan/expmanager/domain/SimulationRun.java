package edu.gatech.hpan.expmanager.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A SimulationRun.
 */
@Entity
@Table(name = "simulation_run")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SimulationRun implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "simulation_trial")
    private String simulationTrial;

    @Column(name = "jhi_index")
    private Integer index;

    @Column(name = "team_id")
    private Integer team_id;

    @Column(name = "score")
    private Integer score;

    @Column(name = "team_capture")
    private Integer teamCapture;

    @Column(name = "non_team_capture")
    private Integer nonTeamCapture;

    @Column(name = "job_num")
    private Integer job_num;

    @Column(name = "task_num")
    private Integer task_num;

    @Column(name = "results_dir")
    private String results_dir;

    @Column(name = "num_rows")
    private Integer num_rows;

    @Column(name = "align_weight_t_1")
    private Double align_weight_t_1;

    @Column(name = "avoid_nonteam_weight_t_1")
    private Double avoid_nonteam_weight_t_1;

    @Column(name = "avoid_team_weight_t_1")
    private Double avoid_team_weight_t_1;

    @Column(name = "centroid_weight_t_1")
    private Double centroid_weight_t_1;

    @Column(name = "comms_range_t_1")
    private Integer comms_range_t_1;

    @Column(name = "fov_az_t_1")
    private Integer fov_az_t_1;

    @Column(name = "fov_el_t_1")
    private Integer fov_el_t_1;

    @Column(name = "goal_weight_t_1")
    private Double goal_weight_t_1;

    @Column(name = "max_pred_speed_t_1")
    private Integer max_pred_speed_t_1;

    @Column(name = "max_speed_t_1")
    private Integer max_speed_t_1;

    @Column(name = "sphere_of_influence_t_1")
    private Integer sphere_of_influence_t_1;

    @Column(name = "motion_model_t_1")
    private String motion_model_t_1;

    @Column(name = "vel_max_t_1")
    private Integer vel_max_t_1;

    @Column(name = "motion_model_predator")
    private String motion_model_predator;

    @Column(name = "pitch_rate_max_predator")
    private Double pitch_rate_max_predator;

    @Column(name = "turn_rate_max_predator")
    private Integer turn_rate_max_predator;

    @Column(name = "vel_max_predator")
    private Integer vel_max_predator;

    @Column(name = "align_weight_t_2_predator")
    private Double align_weight_t_2_predator;

    @Column(name = "allow_prey_switching_t_2_predator")
    private Boolean allow_prey_switching_t_2_predator;

    @Column(name = "autonomy_t_2_predator")
    private String autonomy_t_2_predator;

    @Column(name = "avoid_nonteam_weight_t_2_predator")
    private Double avoid_nonteam_weight_t_2_predator;

    @Column(name = "avoid_team_weight_t_2_predator")
    private Double avoid_team_weight_t_2_predator;

    @Column(name = "capture_range_t_2_predator")
    private Integer capture_range_t_2_predator;

    @Column(name = "centroid_weight_t_2_predator")
    private Double centroid_weight_t_2_predator;

    @Column(name = "comms_range_t_2_predator")
    private Integer comms_range_t_2_predator;

    @Column(name = "max_pred_speed_t_2_predator")
    private Integer max_pred_speed_t_2_predator;

    @Column(name = "max_speed_t_2_predator")
    private Integer max_speed_t_2_predator;

    @Column(name = "align_weight_t_3_predator")
    private Double align_weight_t_3_predator;

    @Column(name = "allow_prey_switching_t_3_predator")
    private Boolean allow_prey_switching_t_3_predator;

    @Column(name = "autonomy_t_3_predator")
    private String autonomy_t_3_predator;

    @Column(name = "avoid_nonteam_weight_t_3_predator")
    private Double avoid_nonteam_weight_t_3_predator;

    @Column(name = "avoid_team_weight_t_3_predator")
    private Double avoid_team_weight_t_3_predator;

    @Column(name = "capture_range_t_3_predator")
    private Integer capture_range_t_3_predator;

    @Column(name = "centroid_weight_t_3_predator")
    private Double centroid_weight_t_3_predator;

    @Column(name = "comms_range_t_3_predator")
    private Integer comms_range_t_3_predator;

    @Column(name = "max_pred_speed_t_3_predator")
    private Integer max_pred_speed_t_3_predator;

    @Column(name = "max_speed_t_3_predator")
    private Integer max_speed_t_3_predator;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSimulationTrial() {
        return simulationTrial;
    }

    public SimulationRun simulationTrial(String simulationTrial) {
        this.simulationTrial = simulationTrial;
        return this;
    }

    public void setSimulationTrial(String simulationTrial) {
        this.simulationTrial = simulationTrial;
    }

    public Integer getIndex() {
        return index;
    }

    public SimulationRun index(Integer index) {
        this.index = index;
        return this;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getTeam_id() {
        return team_id;
    }

    public SimulationRun team_id(Integer team_id) {
        this.team_id = team_id;
        return this;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public Integer getScore() {
        return score;
    }

    public SimulationRun score(Integer score) {
        this.score = score;
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTeamCapture() {
        return teamCapture;
    }

    public SimulationRun teamCapture(Integer teamCapture) {
        this.teamCapture = teamCapture;
        return this;
    }

    public void setTeamCapture(Integer teamCapture) {
        this.teamCapture = teamCapture;
    }

    public Integer getNonTeamCapture() {
        return nonTeamCapture;
    }

    public SimulationRun nonTeamCapture(Integer nonTeamCapture) {
        this.nonTeamCapture = nonTeamCapture;
        return this;
    }

    public void setNonTeamCapture(Integer nonTeamCapture) {
        this.nonTeamCapture = nonTeamCapture;
    }

    public Integer getJob_num() {
        return job_num;
    }

    public SimulationRun job_num(Integer job_num) {
        this.job_num = job_num;
        return this;
    }

    public void setJob_num(Integer job_num) {
        this.job_num = job_num;
    }

    public Integer getTask_num() {
        return task_num;
    }

    public SimulationRun task_num(Integer task_num) {
        this.task_num = task_num;
        return this;
    }

    public void setTask_num(Integer task_num) {
        this.task_num = task_num;
    }

    public String getResults_dir() {
        return results_dir;
    }

    public SimulationRun results_dir(String results_dir) {
        this.results_dir = results_dir;
        return this;
    }

    public void setResults_dir(String results_dir) {
        this.results_dir = results_dir;
    }

    public Integer getNum_rows() {
        return num_rows;
    }

    public SimulationRun num_rows(Integer num_rows) {
        this.num_rows = num_rows;
        return this;
    }

    public void setNum_rows(Integer num_rows) {
        this.num_rows = num_rows;
    }

    public Double getAlign_weight_t_1() {
        return align_weight_t_1;
    }

    public SimulationRun align_weight_t_1(Double align_weight_t_1) {
        this.align_weight_t_1 = align_weight_t_1;
        return this;
    }

    public void setAlign_weight_t_1(Double align_weight_t_1) {
        this.align_weight_t_1 = align_weight_t_1;
    }

    public Double getAvoid_nonteam_weight_t_1() {
        return avoid_nonteam_weight_t_1;
    }

    public SimulationRun avoid_nonteam_weight_t_1(Double avoid_nonteam_weight_t_1) {
        this.avoid_nonteam_weight_t_1 = avoid_nonteam_weight_t_1;
        return this;
    }

    public void setAvoid_nonteam_weight_t_1(Double avoid_nonteam_weight_t_1) {
        this.avoid_nonteam_weight_t_1 = avoid_nonteam_weight_t_1;
    }

    public Double getAvoid_team_weight_t_1() {
        return avoid_team_weight_t_1;
    }

    public SimulationRun avoid_team_weight_t_1(Double avoid_team_weight_t_1) {
        this.avoid_team_weight_t_1 = avoid_team_weight_t_1;
        return this;
    }

    public void setAvoid_team_weight_t_1(Double avoid_team_weight_t_1) {
        this.avoid_team_weight_t_1 = avoid_team_weight_t_1;
    }

    public Double getCentroid_weight_t_1() {
        return centroid_weight_t_1;
    }

    public SimulationRun centroid_weight_t_1(Double centroid_weight_t_1) {
        this.centroid_weight_t_1 = centroid_weight_t_1;
        return this;
    }

    public void setCentroid_weight_t_1(Double centroid_weight_t_1) {
        this.centroid_weight_t_1 = centroid_weight_t_1;
    }

    public Integer getComms_range_t_1() {
        return comms_range_t_1;
    }

    public SimulationRun comms_range_t_1(Integer comms_range_t_1) {
        this.comms_range_t_1 = comms_range_t_1;
        return this;
    }

    public void setComms_range_t_1(Integer comms_range_t_1) {
        this.comms_range_t_1 = comms_range_t_1;
    }

    public Integer getFov_az_t_1() {
        return fov_az_t_1;
    }

    public SimulationRun fov_az_t_1(Integer fov_az_t_1) {
        this.fov_az_t_1 = fov_az_t_1;
        return this;
    }

    public void setFov_az_t_1(Integer fov_az_t_1) {
        this.fov_az_t_1 = fov_az_t_1;
    }

    public Integer getFov_el_t_1() {
        return fov_el_t_1;
    }

    public SimulationRun fov_el_t_1(Integer fov_el_t_1) {
        this.fov_el_t_1 = fov_el_t_1;
        return this;
    }

    public void setFov_el_t_1(Integer fov_el_t_1) {
        this.fov_el_t_1 = fov_el_t_1;
    }

    public Double getGoal_weight_t_1() {
        return goal_weight_t_1;
    }

    public SimulationRun goal_weight_t_1(Double goal_weight_t_1) {
        this.goal_weight_t_1 = goal_weight_t_1;
        return this;
    }

    public void setGoal_weight_t_1(Double goal_weight_t_1) {
        this.goal_weight_t_1 = goal_weight_t_1;
    }

    public Integer getMax_pred_speed_t_1() {
        return max_pred_speed_t_1;
    }

    public SimulationRun max_pred_speed_t_1(Integer max_pred_speed_t_1) {
        this.max_pred_speed_t_1 = max_pred_speed_t_1;
        return this;
    }

    public void setMax_pred_speed_t_1(Integer max_pred_speed_t_1) {
        this.max_pred_speed_t_1 = max_pred_speed_t_1;
    }

    public Integer getMax_speed_t_1() {
        return max_speed_t_1;
    }

    public SimulationRun max_speed_t_1(Integer max_speed_t_1) {
        this.max_speed_t_1 = max_speed_t_1;
        return this;
    }

    public void setMax_speed_t_1(Integer max_speed_t_1) {
        this.max_speed_t_1 = max_speed_t_1;
    }

    public Integer getSphere_of_influence_t_1() {
        return sphere_of_influence_t_1;
    }

    public SimulationRun sphere_of_influence_t_1(Integer sphere_of_influence_t_1) {
        this.sphere_of_influence_t_1 = sphere_of_influence_t_1;
        return this;
    }

    public void setSphere_of_influence_t_1(Integer sphere_of_influence_t_1) {
        this.sphere_of_influence_t_1 = sphere_of_influence_t_1;
    }

    public String getMotion_model_t_1() {
        return motion_model_t_1;
    }

    public SimulationRun motion_model_t_1(String motion_model_t_1) {
        this.motion_model_t_1 = motion_model_t_1;
        return this;
    }

    public void setMotion_model_t_1(String motion_model_t_1) {
        this.motion_model_t_1 = motion_model_t_1;
    }

    public Integer getVel_max_t_1() {
        return vel_max_t_1;
    }

    public SimulationRun vel_max_t_1(Integer vel_max_t_1) {
        this.vel_max_t_1 = vel_max_t_1;
        return this;
    }

    public void setVel_max_t_1(Integer vel_max_t_1) {
        this.vel_max_t_1 = vel_max_t_1;
    }

    public String getMotion_model_predator() {
        return motion_model_predator;
    }

    public SimulationRun motion_model_predator(String motion_model_predator) {
        this.motion_model_predator = motion_model_predator;
        return this;
    }

    public void setMotion_model_predator(String motion_model_predator) {
        this.motion_model_predator = motion_model_predator;
    }

    public Double getPitch_rate_max_predator() {
        return pitch_rate_max_predator;
    }

    public SimulationRun pitch_rate_max_predator(Double pitch_rate_max_predator) {
        this.pitch_rate_max_predator = pitch_rate_max_predator;
        return this;
    }

    public void setPitch_rate_max_predator(Double pitch_rate_max_predator) {
        this.pitch_rate_max_predator = pitch_rate_max_predator;
    }

    public Integer getTurn_rate_max_predator() {
        return turn_rate_max_predator;
    }

    public SimulationRun turn_rate_max_predator(Integer turn_rate_max_predator) {
        this.turn_rate_max_predator = turn_rate_max_predator;
        return this;
    }

    public void setTurn_rate_max_predator(Integer turn_rate_max_predator) {
        this.turn_rate_max_predator = turn_rate_max_predator;
    }

    public Integer getVel_max_predator() {
        return vel_max_predator;
    }

    public SimulationRun vel_max_predator(Integer vel_max_predator) {
        this.vel_max_predator = vel_max_predator;
        return this;
    }

    public void setVel_max_predator(Integer vel_max_predator) {
        this.vel_max_predator = vel_max_predator;
    }

    public Double getAlign_weight_t_2_predator() {
        return align_weight_t_2_predator;
    }

    public SimulationRun align_weight_t_2_predator(Double align_weight_t_2_predator) {
        this.align_weight_t_2_predator = align_weight_t_2_predator;
        return this;
    }

    public void setAlign_weight_t_2_predator(Double align_weight_t_2_predator) {
        this.align_weight_t_2_predator = align_weight_t_2_predator;
    }

    public Boolean isAllow_prey_switching_t_2_predator() {
        return allow_prey_switching_t_2_predator;
    }

    public SimulationRun allow_prey_switching_t_2_predator(Boolean allow_prey_switching_t_2_predator) {
        this.allow_prey_switching_t_2_predator = allow_prey_switching_t_2_predator;
        return this;
    }

    public void setAllow_prey_switching_t_2_predator(Boolean allow_prey_switching_t_2_predator) {
        this.allow_prey_switching_t_2_predator = allow_prey_switching_t_2_predator;
    }

    public String getAutonomy_t_2_predator() {
        return autonomy_t_2_predator;
    }

    public SimulationRun autonomy_t_2_predator(String autonomy_t_2_predator) {
        this.autonomy_t_2_predator = autonomy_t_2_predator;
        return this;
    }

    public void setAutonomy_t_2_predator(String autonomy_t_2_predator) {
        this.autonomy_t_2_predator = autonomy_t_2_predator;
    }

    public Double getAvoid_nonteam_weight_t_2_predator() {
        return avoid_nonteam_weight_t_2_predator;
    }

    public SimulationRun avoid_nonteam_weight_t_2_predator(Double avoid_nonteam_weight_t_2_predator) {
        this.avoid_nonteam_weight_t_2_predator = avoid_nonteam_weight_t_2_predator;
        return this;
    }

    public void setAvoid_nonteam_weight_t_2_predator(Double avoid_nonteam_weight_t_2_predator) {
        this.avoid_nonteam_weight_t_2_predator = avoid_nonteam_weight_t_2_predator;
    }

    public Double getAvoid_team_weight_t_2_predator() {
        return avoid_team_weight_t_2_predator;
    }

    public SimulationRun avoid_team_weight_t_2_predator(Double avoid_team_weight_t_2_predator) {
        this.avoid_team_weight_t_2_predator = avoid_team_weight_t_2_predator;
        return this;
    }

    public void setAvoid_team_weight_t_2_predator(Double avoid_team_weight_t_2_predator) {
        this.avoid_team_weight_t_2_predator = avoid_team_weight_t_2_predator;
    }

    public Integer getCapture_range_t_2_predator() {
        return capture_range_t_2_predator;
    }

    public SimulationRun capture_range_t_2_predator(Integer capture_range_t_2_predator) {
        this.capture_range_t_2_predator = capture_range_t_2_predator;
        return this;
    }

    public void setCapture_range_t_2_predator(Integer capture_range_t_2_predator) {
        this.capture_range_t_2_predator = capture_range_t_2_predator;
    }

    public Double getCentroid_weight_t_2_predator() {
        return centroid_weight_t_2_predator;
    }

    public SimulationRun centroid_weight_t_2_predator(Double centroid_weight_t_2_predator) {
        this.centroid_weight_t_2_predator = centroid_weight_t_2_predator;
        return this;
    }

    public void setCentroid_weight_t_2_predator(Double centroid_weight_t_2_predator) {
        this.centroid_weight_t_2_predator = centroid_weight_t_2_predator;
    }

    public Integer getComms_range_t_2_predator() {
        return comms_range_t_2_predator;
    }

    public SimulationRun comms_range_t_2_predator(Integer comms_range_t_2_predator) {
        this.comms_range_t_2_predator = comms_range_t_2_predator;
        return this;
    }

    public void setComms_range_t_2_predator(Integer comms_range_t_2_predator) {
        this.comms_range_t_2_predator = comms_range_t_2_predator;
    }

    public Integer getMax_pred_speed_t_2_predator() {
        return max_pred_speed_t_2_predator;
    }

    public SimulationRun max_pred_speed_t_2_predator(Integer max_pred_speed_t_2_predator) {
        this.max_pred_speed_t_2_predator = max_pred_speed_t_2_predator;
        return this;
    }

    public void setMax_pred_speed_t_2_predator(Integer max_pred_speed_t_2_predator) {
        this.max_pred_speed_t_2_predator = max_pred_speed_t_2_predator;
    }

    public Integer getMax_speed_t_2_predator() {
        return max_speed_t_2_predator;
    }

    public SimulationRun max_speed_t_2_predator(Integer max_speed_t_2_predator) {
        this.max_speed_t_2_predator = max_speed_t_2_predator;
        return this;
    }

    public void setMax_speed_t_2_predator(Integer max_speed_t_2_predator) {
        this.max_speed_t_2_predator = max_speed_t_2_predator;
    }

    public Double getAlign_weight_t_3_predator() {
        return align_weight_t_3_predator;
    }

    public SimulationRun align_weight_t_3_predator(Double align_weight_t_3_predator) {
        this.align_weight_t_3_predator = align_weight_t_3_predator;
        return this;
    }

    public void setAlign_weight_t_3_predator(Double align_weight_t_3_predator) {
        this.align_weight_t_3_predator = align_weight_t_3_predator;
    }

    public Boolean isAllow_prey_switching_t_3_predator() {
        return allow_prey_switching_t_3_predator;
    }

    public SimulationRun allow_prey_switching_t_3_predator(Boolean allow_prey_switching_t_3_predator) {
        this.allow_prey_switching_t_3_predator = allow_prey_switching_t_3_predator;
        return this;
    }

    public void setAllow_prey_switching_t_3_predator(Boolean allow_prey_switching_t_3_predator) {
        this.allow_prey_switching_t_3_predator = allow_prey_switching_t_3_predator;
    }

    public String getAutonomy_t_3_predator() {
        return autonomy_t_3_predator;
    }

    public SimulationRun autonomy_t_3_predator(String autonomy_t_3_predator) {
        this.autonomy_t_3_predator = autonomy_t_3_predator;
        return this;
    }

    public void setAutonomy_t_3_predator(String autonomy_t_3_predator) {
        this.autonomy_t_3_predator = autonomy_t_3_predator;
    }

    public Double getAvoid_nonteam_weight_t_3_predator() {
        return avoid_nonteam_weight_t_3_predator;
    }

    public SimulationRun avoid_nonteam_weight_t_3_predator(Double avoid_nonteam_weight_t_3_predator) {
        this.avoid_nonteam_weight_t_3_predator = avoid_nonteam_weight_t_3_predator;
        return this;
    }

    public void setAvoid_nonteam_weight_t_3_predator(Double avoid_nonteam_weight_t_3_predator) {
        this.avoid_nonteam_weight_t_3_predator = avoid_nonteam_weight_t_3_predator;
    }

    public Double getAvoid_team_weight_t_3_predator() {
        return avoid_team_weight_t_3_predator;
    }

    public SimulationRun avoid_team_weight_t_3_predator(Double avoid_team_weight_t_3_predator) {
        this.avoid_team_weight_t_3_predator = avoid_team_weight_t_3_predator;
        return this;
    }

    public void setAvoid_team_weight_t_3_predator(Double avoid_team_weight_t_3_predator) {
        this.avoid_team_weight_t_3_predator = avoid_team_weight_t_3_predator;
    }

    public Integer getCapture_range_t_3_predator() {
        return capture_range_t_3_predator;
    }

    public SimulationRun capture_range_t_3_predator(Integer capture_range_t_3_predator) {
        this.capture_range_t_3_predator = capture_range_t_3_predator;
        return this;
    }

    public void setCapture_range_t_3_predator(Integer capture_range_t_3_predator) {
        this.capture_range_t_3_predator = capture_range_t_3_predator;
    }

    public Double getCentroid_weight_t_3_predator() {
        return centroid_weight_t_3_predator;
    }

    public SimulationRun centroid_weight_t_3_predator(Double centroid_weight_t_3_predator) {
        this.centroid_weight_t_3_predator = centroid_weight_t_3_predator;
        return this;
    }

    public void setCentroid_weight_t_3_predator(Double centroid_weight_t_3_predator) {
        this.centroid_weight_t_3_predator = centroid_weight_t_3_predator;
    }

    public Integer getComms_range_t_3_predator() {
        return comms_range_t_3_predator;
    }

    public SimulationRun comms_range_t_3_predator(Integer comms_range_t_3_predator) {
        this.comms_range_t_3_predator = comms_range_t_3_predator;
        return this;
    }

    public void setComms_range_t_3_predator(Integer comms_range_t_3_predator) {
        this.comms_range_t_3_predator = comms_range_t_3_predator;
    }

    public Integer getMax_pred_speed_t_3_predator() {
        return max_pred_speed_t_3_predator;
    }

    public SimulationRun max_pred_speed_t_3_predator(Integer max_pred_speed_t_3_predator) {
        this.max_pred_speed_t_3_predator = max_pred_speed_t_3_predator;
        return this;
    }

    public void setMax_pred_speed_t_3_predator(Integer max_pred_speed_t_3_predator) {
        this.max_pred_speed_t_3_predator = max_pred_speed_t_3_predator;
    }

    public Integer getMax_speed_t_3_predator() {
        return max_speed_t_3_predator;
    }

    public SimulationRun max_speed_t_3_predator(Integer max_speed_t_3_predator) {
        this.max_speed_t_3_predator = max_speed_t_3_predator;
        return this;
    }

    public void setMax_speed_t_3_predator(Integer max_speed_t_3_predator) {
        this.max_speed_t_3_predator = max_speed_t_3_predator;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimulationRun simulationRun = (SimulationRun) o;
        if (simulationRun.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), simulationRun.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SimulationRun{" +
            "id=" + getId() +
            ", simulationTrial='" + getSimulationTrial() + "'" +
            ", index=" + getIndex() +
            ", team_id=" + getTeam_id() +
            ", score=" + getScore() +
            ", teamCapture=" + getTeamCapture() +
            ", nonTeamCapture=" + getNonTeamCapture() +
            ", job_num=" + getJob_num() +
            ", task_num=" + getTask_num() +
            ", results_dir='" + getResults_dir() + "'" +
            ", num_rows=" + getNum_rows() +
            ", align_weight_t_1=" + getAlign_weight_t_1() +
            ", avoid_nonteam_weight_t_1=" + getAvoid_nonteam_weight_t_1() +
            ", avoid_team_weight_t_1=" + getAvoid_team_weight_t_1() +
            ", centroid_weight_t_1=" + getCentroid_weight_t_1() +
            ", comms_range_t_1=" + getComms_range_t_1() +
            ", fov_az_t_1=" + getFov_az_t_1() +
            ", fov_el_t_1=" + getFov_el_t_1() +
            ", goal_weight_t_1=" + getGoal_weight_t_1() +
            ", max_pred_speed_t_1=" + getMax_pred_speed_t_1() +
            ", max_speed_t_1=" + getMax_speed_t_1() +
            ", sphere_of_influence_t_1=" + getSphere_of_influence_t_1() +
            ", motion_model_t_1='" + getMotion_model_t_1() + "'" +
            ", vel_max_t_1=" + getVel_max_t_1() +
            ", motion_model_predator='" + getMotion_model_predator() + "'" +
            ", pitch_rate_max_predator=" + getPitch_rate_max_predator() +
            ", turn_rate_max_predator=" + getTurn_rate_max_predator() +
            ", vel_max_predator=" + getVel_max_predator() +
            ", align_weight_t_2_predator=" + getAlign_weight_t_2_predator() +
            ", allow_prey_switching_t_2_predator='" + isAllow_prey_switching_t_2_predator() + "'" +
            ", autonomy_t_2_predator='" + getAutonomy_t_2_predator() + "'" +
            ", avoid_nonteam_weight_t_2_predator=" + getAvoid_nonteam_weight_t_2_predator() +
            ", avoid_team_weight_t_2_predator=" + getAvoid_team_weight_t_2_predator() +
            ", capture_range_t_2_predator=" + getCapture_range_t_2_predator() +
            ", centroid_weight_t_2_predator=" + getCentroid_weight_t_2_predator() +
            ", comms_range_t_2_predator=" + getComms_range_t_2_predator() +
            ", max_pred_speed_t_2_predator=" + getMax_pred_speed_t_2_predator() +
            ", max_speed_t_2_predator=" + getMax_speed_t_2_predator() +
            ", align_weight_t_3_predator=" + getAlign_weight_t_3_predator() +
            ", allow_prey_switching_t_3_predator='" + isAllow_prey_switching_t_3_predator() + "'" +
            ", autonomy_t_3_predator='" + getAutonomy_t_3_predator() + "'" +
            ", avoid_nonteam_weight_t_3_predator=" + getAvoid_nonteam_weight_t_3_predator() +
            ", avoid_team_weight_t_3_predator=" + getAvoid_team_weight_t_3_predator() +
            ", capture_range_t_3_predator=" + getCapture_range_t_3_predator() +
            ", centroid_weight_t_3_predator=" + getCentroid_weight_t_3_predator() +
            ", comms_range_t_3_predator=" + getComms_range_t_3_predator() +
            ", max_pred_speed_t_3_predator=" + getMax_pred_speed_t_3_predator() +
            ", max_speed_t_3_predator=" + getMax_speed_t_3_predator() +
            "}";
    }
}
