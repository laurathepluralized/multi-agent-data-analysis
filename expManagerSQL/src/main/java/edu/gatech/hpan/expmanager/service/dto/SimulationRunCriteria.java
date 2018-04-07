package edu.gatech.hpan.expmanager.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the SimulationRun entity. This class is used in SimulationRunResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /simulation-runs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SimulationRunCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter simulationTrial;

    private IntegerFilter index;

    private IntegerFilter team_id;

    private IntegerFilter score;

    private IntegerFilter teamCapture;

    private IntegerFilter nonTeamCapture;

    private IntegerFilter job_num;

    private IntegerFilter task_num;

    private StringFilter results_dir;

    private IntegerFilter num_rows;

    private DoubleFilter align_weight_t_1;

    private DoubleFilter avoid_nonteam_weight_t_1;

    private DoubleFilter avoid_team_weight_t_1;

    private DoubleFilter centroid_weight_t_1;

    private IntegerFilter comms_range_t_1;

    private IntegerFilter fov_az_t_1;

    private IntegerFilter fov_el_t_1;

    private DoubleFilter goal_weight_t_1;

    private IntegerFilter max_pred_speed_t_1;

    private IntegerFilter max_speed_t_1;

    private IntegerFilter sphere_of_influence_t_1;

    private StringFilter motion_model_t_1;

    private IntegerFilter vel_max_t_1;

    private StringFilter motion_model_predator;

    private DoubleFilter pitch_rate_max_predator;

    private IntegerFilter turn_rate_max_predator;

    private IntegerFilter vel_max_predator;

    private DoubleFilter align_weight_t_2_predator;

    private BooleanFilter allow_prey_switching_t_2_predator;

    private StringFilter autonomy_t_2_predator;

    private DoubleFilter avoid_nonteam_weight_t_2_predator;

    private DoubleFilter avoid_team_weight_t_2_predator;

    private IntegerFilter capture_range_t_2_predator;

    private DoubleFilter centroid_weight_t_2_predator;

    private IntegerFilter comms_range_t_2_predator;

    private IntegerFilter max_pred_speed_t_2_predator;

    private IntegerFilter max_speed_t_2_predator;

    private DoubleFilter align_weight_t_3_predator;

    private BooleanFilter allow_prey_switching_t_3_predator;

    private StringFilter autonomy_t_3_predator;

    private DoubleFilter avoid_nonteam_weight_t_3_predator;

    private DoubleFilter avoid_team_weight_t_3_predator;

    private IntegerFilter capture_range_t_3_predator;

    private DoubleFilter centroid_weight_t_3_predator;

    private IntegerFilter comms_range_t_3_predator;

    private IntegerFilter max_pred_speed_t_3_predator;

    private IntegerFilter max_speed_t_3_predator;

    public SimulationRunCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSimulationTrial() {
        return simulationTrial;
    }

    public void setSimulationTrial(StringFilter simulationTrial) {
        this.simulationTrial = simulationTrial;
    }

    public IntegerFilter getIndex() {
        return index;
    }

    public void setIndex(IntegerFilter index) {
        this.index = index;
    }

    public IntegerFilter getTeam_id() {
        return team_id;
    }

    public void setTeam_id(IntegerFilter team_id) {
        this.team_id = team_id;
    }

    public IntegerFilter getScore() {
        return score;
    }

    public void setScore(IntegerFilter score) {
        this.score = score;
    }

    public IntegerFilter getTeamCapture() {
        return teamCapture;
    }

    public void setTeamCapture(IntegerFilter teamCapture) {
        this.teamCapture = teamCapture;
    }

    public IntegerFilter getNonTeamCapture() {
        return nonTeamCapture;
    }

    public void setNonTeamCapture(IntegerFilter nonTeamCapture) {
        this.nonTeamCapture = nonTeamCapture;
    }

    public IntegerFilter getJob_num() {
        return job_num;
    }

    public void setJob_num(IntegerFilter job_num) {
        this.job_num = job_num;
    }

    public IntegerFilter getTask_num() {
        return task_num;
    }

    public void setTask_num(IntegerFilter task_num) {
        this.task_num = task_num;
    }

    public StringFilter getResults_dir() {
        return results_dir;
    }

    public void setResults_dir(StringFilter results_dir) {
        this.results_dir = results_dir;
    }

    public IntegerFilter getNum_rows() {
        return num_rows;
    }

    public void setNum_rows(IntegerFilter num_rows) {
        this.num_rows = num_rows;
    }

    public DoubleFilter getAlign_weight_t_1() {
        return align_weight_t_1;
    }

    public void setAlign_weight_t_1(DoubleFilter align_weight_t_1) {
        this.align_weight_t_1 = align_weight_t_1;
    }

    public DoubleFilter getAvoid_nonteam_weight_t_1() {
        return avoid_nonteam_weight_t_1;
    }

    public void setAvoid_nonteam_weight_t_1(DoubleFilter avoid_nonteam_weight_t_1) {
        this.avoid_nonteam_weight_t_1 = avoid_nonteam_weight_t_1;
    }

    public DoubleFilter getAvoid_team_weight_t_1() {
        return avoid_team_weight_t_1;
    }

    public void setAvoid_team_weight_t_1(DoubleFilter avoid_team_weight_t_1) {
        this.avoid_team_weight_t_1 = avoid_team_weight_t_1;
    }

    public DoubleFilter getCentroid_weight_t_1() {
        return centroid_weight_t_1;
    }

    public void setCentroid_weight_t_1(DoubleFilter centroid_weight_t_1) {
        this.centroid_weight_t_1 = centroid_weight_t_1;
    }

    public IntegerFilter getComms_range_t_1() {
        return comms_range_t_1;
    }

    public void setComms_range_t_1(IntegerFilter comms_range_t_1) {
        this.comms_range_t_1 = comms_range_t_1;
    }

    public IntegerFilter getFov_az_t_1() {
        return fov_az_t_1;
    }

    public void setFov_az_t_1(IntegerFilter fov_az_t_1) {
        this.fov_az_t_1 = fov_az_t_1;
    }

    public IntegerFilter getFov_el_t_1() {
        return fov_el_t_1;
    }

    public void setFov_el_t_1(IntegerFilter fov_el_t_1) {
        this.fov_el_t_1 = fov_el_t_1;
    }

    public DoubleFilter getGoal_weight_t_1() {
        return goal_weight_t_1;
    }

    public void setGoal_weight_t_1(DoubleFilter goal_weight_t_1) {
        this.goal_weight_t_1 = goal_weight_t_1;
    }

    public IntegerFilter getMax_pred_speed_t_1() {
        return max_pred_speed_t_1;
    }

    public void setMax_pred_speed_t_1(IntegerFilter max_pred_speed_t_1) {
        this.max_pred_speed_t_1 = max_pred_speed_t_1;
    }

    public IntegerFilter getMax_speed_t_1() {
        return max_speed_t_1;
    }

    public void setMax_speed_t_1(IntegerFilter max_speed_t_1) {
        this.max_speed_t_1 = max_speed_t_1;
    }

    public IntegerFilter getSphere_of_influence_t_1() {
        return sphere_of_influence_t_1;
    }

    public void setSphere_of_influence_t_1(IntegerFilter sphere_of_influence_t_1) {
        this.sphere_of_influence_t_1 = sphere_of_influence_t_1;
    }

    public StringFilter getMotion_model_t_1() {
        return motion_model_t_1;
    }

    public void setMotion_model_t_1(StringFilter motion_model_t_1) {
        this.motion_model_t_1 = motion_model_t_1;
    }

    public IntegerFilter getVel_max_t_1() {
        return vel_max_t_1;
    }

    public void setVel_max_t_1(IntegerFilter vel_max_t_1) {
        this.vel_max_t_1 = vel_max_t_1;
    }

    public StringFilter getMotion_model_predator() {
        return motion_model_predator;
    }

    public void setMotion_model_predator(StringFilter motion_model_predator) {
        this.motion_model_predator = motion_model_predator;
    }

    public DoubleFilter getPitch_rate_max_predator() {
        return pitch_rate_max_predator;
    }

    public void setPitch_rate_max_predator(DoubleFilter pitch_rate_max_predator) {
        this.pitch_rate_max_predator = pitch_rate_max_predator;
    }

    public IntegerFilter getTurn_rate_max_predator() {
        return turn_rate_max_predator;
    }

    public void setTurn_rate_max_predator(IntegerFilter turn_rate_max_predator) {
        this.turn_rate_max_predator = turn_rate_max_predator;
    }

    public IntegerFilter getVel_max_predator() {
        return vel_max_predator;
    }

    public void setVel_max_predator(IntegerFilter vel_max_predator) {
        this.vel_max_predator = vel_max_predator;
    }

    public DoubleFilter getAlign_weight_t_2_predator() {
        return align_weight_t_2_predator;
    }

    public void setAlign_weight_t_2_predator(DoubleFilter align_weight_t_2_predator) {
        this.align_weight_t_2_predator = align_weight_t_2_predator;
    }

    public BooleanFilter getAllow_prey_switching_t_2_predator() {
        return allow_prey_switching_t_2_predator;
    }

    public void setAllow_prey_switching_t_2_predator(BooleanFilter allow_prey_switching_t_2_predator) {
        this.allow_prey_switching_t_2_predator = allow_prey_switching_t_2_predator;
    }

    public StringFilter getAutonomy_t_2_predator() {
        return autonomy_t_2_predator;
    }

    public void setAutonomy_t_2_predator(StringFilter autonomy_t_2_predator) {
        this.autonomy_t_2_predator = autonomy_t_2_predator;
    }

    public DoubleFilter getAvoid_nonteam_weight_t_2_predator() {
        return avoid_nonteam_weight_t_2_predator;
    }

    public void setAvoid_nonteam_weight_t_2_predator(DoubleFilter avoid_nonteam_weight_t_2_predator) {
        this.avoid_nonteam_weight_t_2_predator = avoid_nonteam_weight_t_2_predator;
    }

    public DoubleFilter getAvoid_team_weight_t_2_predator() {
        return avoid_team_weight_t_2_predator;
    }

    public void setAvoid_team_weight_t_2_predator(DoubleFilter avoid_team_weight_t_2_predator) {
        this.avoid_team_weight_t_2_predator = avoid_team_weight_t_2_predator;
    }

    public IntegerFilter getCapture_range_t_2_predator() {
        return capture_range_t_2_predator;
    }

    public void setCapture_range_t_2_predator(IntegerFilter capture_range_t_2_predator) {
        this.capture_range_t_2_predator = capture_range_t_2_predator;
    }

    public DoubleFilter getCentroid_weight_t_2_predator() {
        return centroid_weight_t_2_predator;
    }

    public void setCentroid_weight_t_2_predator(DoubleFilter centroid_weight_t_2_predator) {
        this.centroid_weight_t_2_predator = centroid_weight_t_2_predator;
    }

    public IntegerFilter getComms_range_t_2_predator() {
        return comms_range_t_2_predator;
    }

    public void setComms_range_t_2_predator(IntegerFilter comms_range_t_2_predator) {
        this.comms_range_t_2_predator = comms_range_t_2_predator;
    }

    public IntegerFilter getMax_pred_speed_t_2_predator() {
        return max_pred_speed_t_2_predator;
    }

    public void setMax_pred_speed_t_2_predator(IntegerFilter max_pred_speed_t_2_predator) {
        this.max_pred_speed_t_2_predator = max_pred_speed_t_2_predator;
    }

    public IntegerFilter getMax_speed_t_2_predator() {
        return max_speed_t_2_predator;
    }

    public void setMax_speed_t_2_predator(IntegerFilter max_speed_t_2_predator) {
        this.max_speed_t_2_predator = max_speed_t_2_predator;
    }

    public DoubleFilter getAlign_weight_t_3_predator() {
        return align_weight_t_3_predator;
    }

    public void setAlign_weight_t_3_predator(DoubleFilter align_weight_t_3_predator) {
        this.align_weight_t_3_predator = align_weight_t_3_predator;
    }

    public BooleanFilter getAllow_prey_switching_t_3_predator() {
        return allow_prey_switching_t_3_predator;
    }

    public void setAllow_prey_switching_t_3_predator(BooleanFilter allow_prey_switching_t_3_predator) {
        this.allow_prey_switching_t_3_predator = allow_prey_switching_t_3_predator;
    }

    public StringFilter getAutonomy_t_3_predator() {
        return autonomy_t_3_predator;
    }

    public void setAutonomy_t_3_predator(StringFilter autonomy_t_3_predator) {
        this.autonomy_t_3_predator = autonomy_t_3_predator;
    }

    public DoubleFilter getAvoid_nonteam_weight_t_3_predator() {
        return avoid_nonteam_weight_t_3_predator;
    }

    public void setAvoid_nonteam_weight_t_3_predator(DoubleFilter avoid_nonteam_weight_t_3_predator) {
        this.avoid_nonteam_weight_t_3_predator = avoid_nonteam_weight_t_3_predator;
    }

    public DoubleFilter getAvoid_team_weight_t_3_predator() {
        return avoid_team_weight_t_3_predator;
    }

    public void setAvoid_team_weight_t_3_predator(DoubleFilter avoid_team_weight_t_3_predator) {
        this.avoid_team_weight_t_3_predator = avoid_team_weight_t_3_predator;
    }

    public IntegerFilter getCapture_range_t_3_predator() {
        return capture_range_t_3_predator;
    }

    public void setCapture_range_t_3_predator(IntegerFilter capture_range_t_3_predator) {
        this.capture_range_t_3_predator = capture_range_t_3_predator;
    }

    public DoubleFilter getCentroid_weight_t_3_predator() {
        return centroid_weight_t_3_predator;
    }

    public void setCentroid_weight_t_3_predator(DoubleFilter centroid_weight_t_3_predator) {
        this.centroid_weight_t_3_predator = centroid_weight_t_3_predator;
    }

    public IntegerFilter getComms_range_t_3_predator() {
        return comms_range_t_3_predator;
    }

    public void setComms_range_t_3_predator(IntegerFilter comms_range_t_3_predator) {
        this.comms_range_t_3_predator = comms_range_t_3_predator;
    }

    public IntegerFilter getMax_pred_speed_t_3_predator() {
        return max_pred_speed_t_3_predator;
    }

    public void setMax_pred_speed_t_3_predator(IntegerFilter max_pred_speed_t_3_predator) {
        this.max_pred_speed_t_3_predator = max_pred_speed_t_3_predator;
    }

    public IntegerFilter getMax_speed_t_3_predator() {
        return max_speed_t_3_predator;
    }

    public void setMax_speed_t_3_predator(IntegerFilter max_speed_t_3_predator) {
        this.max_speed_t_3_predator = max_speed_t_3_predator;
    }

    @Override
    public String toString() {
        return "SimulationRunCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (simulationTrial != null ? "simulationTrial=" + simulationTrial + ", " : "") +
                (index != null ? "index=" + index + ", " : "") +
                (team_id != null ? "team_id=" + team_id + ", " : "") +
                (score != null ? "score=" + score + ", " : "") +
                (teamCapture != null ? "teamCapture=" + teamCapture + ", " : "") +
                (nonTeamCapture != null ? "nonTeamCapture=" + nonTeamCapture + ", " : "") +
                (job_num != null ? "job_num=" + job_num + ", " : "") +
                (task_num != null ? "task_num=" + task_num + ", " : "") +
                (results_dir != null ? "results_dir=" + results_dir + ", " : "") +
                (num_rows != null ? "num_rows=" + num_rows + ", " : "") +
                (align_weight_t_1 != null ? "align_weight_t_1=" + align_weight_t_1 + ", " : "") +
                (avoid_nonteam_weight_t_1 != null ? "avoid_nonteam_weight_t_1=" + avoid_nonteam_weight_t_1 + ", " : "") +
                (avoid_team_weight_t_1 != null ? "avoid_team_weight_t_1=" + avoid_team_weight_t_1 + ", " : "") +
                (centroid_weight_t_1 != null ? "centroid_weight_t_1=" + centroid_weight_t_1 + ", " : "") +
                (comms_range_t_1 != null ? "comms_range_t_1=" + comms_range_t_1 + ", " : "") +
                (fov_az_t_1 != null ? "fov_az_t_1=" + fov_az_t_1 + ", " : "") +
                (fov_el_t_1 != null ? "fov_el_t_1=" + fov_el_t_1 + ", " : "") +
                (goal_weight_t_1 != null ? "goal_weight_t_1=" + goal_weight_t_1 + ", " : "") +
                (max_pred_speed_t_1 != null ? "max_pred_speed_t_1=" + max_pred_speed_t_1 + ", " : "") +
                (max_speed_t_1 != null ? "max_speed_t_1=" + max_speed_t_1 + ", " : "") +
                (sphere_of_influence_t_1 != null ? "sphere_of_influence_t_1=" + sphere_of_influence_t_1 + ", " : "") +
                (motion_model_t_1 != null ? "motion_model_t_1=" + motion_model_t_1 + ", " : "") +
                (vel_max_t_1 != null ? "vel_max_t_1=" + vel_max_t_1 + ", " : "") +
                (motion_model_predator != null ? "motion_model_predator=" + motion_model_predator + ", " : "") +
                (pitch_rate_max_predator != null ? "pitch_rate_max_predator=" + pitch_rate_max_predator + ", " : "") +
                (turn_rate_max_predator != null ? "turn_rate_max_predator=" + turn_rate_max_predator + ", " : "") +
                (vel_max_predator != null ? "vel_max_predator=" + vel_max_predator + ", " : "") +
                (align_weight_t_2_predator != null ? "align_weight_t_2_predator=" + align_weight_t_2_predator + ", " : "") +
                (allow_prey_switching_t_2_predator != null ? "allow_prey_switching_t_2_predator=" + allow_prey_switching_t_2_predator + ", " : "") +
                (autonomy_t_2_predator != null ? "autonomy_t_2_predator=" + autonomy_t_2_predator + ", " : "") +
                (avoid_nonteam_weight_t_2_predator != null ? "avoid_nonteam_weight_t_2_predator=" + avoid_nonteam_weight_t_2_predator + ", " : "") +
                (avoid_team_weight_t_2_predator != null ? "avoid_team_weight_t_2_predator=" + avoid_team_weight_t_2_predator + ", " : "") +
                (capture_range_t_2_predator != null ? "capture_range_t_2_predator=" + capture_range_t_2_predator + ", " : "") +
                (centroid_weight_t_2_predator != null ? "centroid_weight_t_2_predator=" + centroid_weight_t_2_predator + ", " : "") +
                (comms_range_t_2_predator != null ? "comms_range_t_2_predator=" + comms_range_t_2_predator + ", " : "") +
                (max_pred_speed_t_2_predator != null ? "max_pred_speed_t_2_predator=" + max_pred_speed_t_2_predator + ", " : "") +
                (max_speed_t_2_predator != null ? "max_speed_t_2_predator=" + max_speed_t_2_predator + ", " : "") +
                (align_weight_t_3_predator != null ? "align_weight_t_3_predator=" + align_weight_t_3_predator + ", " : "") +
                (allow_prey_switching_t_3_predator != null ? "allow_prey_switching_t_3_predator=" + allow_prey_switching_t_3_predator + ", " : "") +
                (autonomy_t_3_predator != null ? "autonomy_t_3_predator=" + autonomy_t_3_predator + ", " : "") +
                (avoid_nonteam_weight_t_3_predator != null ? "avoid_nonteam_weight_t_3_predator=" + avoid_nonteam_weight_t_3_predator + ", " : "") +
                (avoid_team_weight_t_3_predator != null ? "avoid_team_weight_t_3_predator=" + avoid_team_weight_t_3_predator + ", " : "") +
                (capture_range_t_3_predator != null ? "capture_range_t_3_predator=" + capture_range_t_3_predator + ", " : "") +
                (centroid_weight_t_3_predator != null ? "centroid_weight_t_3_predator=" + centroid_weight_t_3_predator + ", " : "") +
                (comms_range_t_3_predator != null ? "comms_range_t_3_predator=" + comms_range_t_3_predator + ", " : "") +
                (max_pred_speed_t_3_predator != null ? "max_pred_speed_t_3_predator=" + max_pred_speed_t_3_predator + ", " : "") +
                (max_speed_t_3_predator != null ? "max_speed_t_3_predator=" + max_speed_t_3_predator + ", " : "") +
            "}";
    }

}
