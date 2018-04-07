package edu.gatech.hpan.expmanager.web.rest;

import edu.gatech.hpan.expmanager.ExpManagerSqlApp;

import edu.gatech.hpan.expmanager.domain.SimulationRun;
import edu.gatech.hpan.expmanager.repository.SimulationRunRepository;
import edu.gatech.hpan.expmanager.service.SimulationRunService;
import edu.gatech.hpan.expmanager.web.rest.errors.ExceptionTranslator;
import edu.gatech.hpan.expmanager.service.dto.SimulationRunCriteria;
import edu.gatech.hpan.expmanager.service.SimulationRunQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static edu.gatech.hpan.expmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SimulationRunResource REST controller.
 *
 * @see SimulationRunResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExpManagerSqlApp.class)
public class SimulationRunResourceIntTest {

    private static final String DEFAULT_SIMULATION_TRIAL = "AAAAAAAAAA";
    private static final String UPDATED_SIMULATION_TRIAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_INDEX = 1;
    private static final Integer UPDATED_INDEX = 2;

    private static final Integer DEFAULT_TEAM_ID = 1;
    private static final Integer UPDATED_TEAM_ID = 2;

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final Integer DEFAULT_TEAM_CAPTURE = 1;
    private static final Integer UPDATED_TEAM_CAPTURE = 2;

    private static final Integer DEFAULT_NON_TEAM_CAPTURE = 1;
    private static final Integer UPDATED_NON_TEAM_CAPTURE = 2;

    private static final Integer DEFAULT_JOB_NUM = 1;
    private static final Integer UPDATED_JOB_NUM = 2;

    private static final Integer DEFAULT_TASK_NUM = 1;
    private static final Integer UPDATED_TASK_NUM = 2;

    private static final String DEFAULT_RESULTS_DIR = "AAAAAAAAAA";
    private static final String UPDATED_RESULTS_DIR = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUM_ROWS = 1;
    private static final Integer UPDATED_NUM_ROWS = 2;

    private static final Double DEFAULT_ALIGN_WEIGHT_T_1 = 1D;
    private static final Double UPDATED_ALIGN_WEIGHT_T_1 = 2D;

    private static final Double DEFAULT_AVOID_NONTEAM_WEIGHT_T_1 = 1D;
    private static final Double UPDATED_AVOID_NONTEAM_WEIGHT_T_1 = 2D;

    private static final Double DEFAULT_AVOID_TEAM_WEIGHT_T_1 = 1D;
    private static final Double UPDATED_AVOID_TEAM_WEIGHT_T_1 = 2D;

    private static final Double DEFAULT_CENTROID_WEIGHT_T_1 = 1D;
    private static final Double UPDATED_CENTROID_WEIGHT_T_1 = 2D;

    private static final Integer DEFAULT_COMMS_RANGE_T_1 = 1;
    private static final Integer UPDATED_COMMS_RANGE_T_1 = 2;

    private static final Integer DEFAULT_FOV_AZ_T_1 = 1;
    private static final Integer UPDATED_FOV_AZ_T_1 = 2;

    private static final Integer DEFAULT_FOV_EL_T_1 = 1;
    private static final Integer UPDATED_FOV_EL_T_1 = 2;

    private static final Double DEFAULT_GOAL_WEIGHT_T_1 = 1D;
    private static final Double UPDATED_GOAL_WEIGHT_T_1 = 2D;

    private static final Integer DEFAULT_MAX_PRED_SPEED_T_1 = 1;
    private static final Integer UPDATED_MAX_PRED_SPEED_T_1 = 2;

    private static final Integer DEFAULT_MAX_SPEED_T_1 = 1;
    private static final Integer UPDATED_MAX_SPEED_T_1 = 2;

    private static final Integer DEFAULT_SPHERE_OF_INFLUENCE_T_1 = 1;
    private static final Integer UPDATED_SPHERE_OF_INFLUENCE_T_1 = 2;

    private static final String DEFAULT_MOTION_MODEL_T_1 = "AAAAAAAAAA";
    private static final String UPDATED_MOTION_MODEL_T_1 = "BBBBBBBBBB";

    private static final Integer DEFAULT_VEL_MAX_T_1 = 1;
    private static final Integer UPDATED_VEL_MAX_T_1 = 2;

    private static final String DEFAULT_MOTION_MODEL_PREDATOR = "AAAAAAAAAA";
    private static final String UPDATED_MOTION_MODEL_PREDATOR = "BBBBBBBBBB";

    private static final Double DEFAULT_PITCH_RATE_MAX_PREDATOR = 1D;
    private static final Double UPDATED_PITCH_RATE_MAX_PREDATOR = 2D;

    private static final Integer DEFAULT_TURN_RATE_MAX_PREDATOR = 1;
    private static final Integer UPDATED_TURN_RATE_MAX_PREDATOR = 2;

    private static final Integer DEFAULT_VEL_MAX_PREDATOR = 1;
    private static final Integer UPDATED_VEL_MAX_PREDATOR = 2;

    private static final Double DEFAULT_ALIGN_WEIGHT_T_2_PREDATOR = 1D;
    private static final Double UPDATED_ALIGN_WEIGHT_T_2_PREDATOR = 2D;

    private static final Boolean DEFAULT_ALLOW_PREY_SWITCHING_T_2_PREDATOR = false;
    private static final Boolean UPDATED_ALLOW_PREY_SWITCHING_T_2_PREDATOR = true;

    private static final String DEFAULT_AUTONOMY_T_2_PREDATOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTONOMY_T_2_PREDATOR = "BBBBBBBBBB";

    private static final Double DEFAULT_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR = 1D;
    private static final Double UPDATED_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR = 2D;

    private static final Double DEFAULT_AVOID_TEAM_WEIGHT_T_2_PREDATOR = 1D;
    private static final Double UPDATED_AVOID_TEAM_WEIGHT_T_2_PREDATOR = 2D;

    private static final Integer DEFAULT_CAPTURE_RANGE_T_2_PREDATOR = 1;
    private static final Integer UPDATED_CAPTURE_RANGE_T_2_PREDATOR = 2;

    private static final Double DEFAULT_CENTROID_WEIGHT_T_2_PREDATOR = 1D;
    private static final Double UPDATED_CENTROID_WEIGHT_T_2_PREDATOR = 2D;

    private static final Integer DEFAULT_COMMS_RANGE_T_2_PREDATOR = 1;
    private static final Integer UPDATED_COMMS_RANGE_T_2_PREDATOR = 2;

    private static final Integer DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR = 1;
    private static final Integer UPDATED_MAX_PRED_SPEED_T_2_PREDATOR = 2;

    private static final Integer DEFAULT_MAX_SPEED_T_2_PREDATOR = 1;
    private static final Integer UPDATED_MAX_SPEED_T_2_PREDATOR = 2;

    private static final Double DEFAULT_ALIGN_WEIGHT_T_3_PREDATOR = 1D;
    private static final Double UPDATED_ALIGN_WEIGHT_T_3_PREDATOR = 2D;

    private static final Boolean DEFAULT_ALLOW_PREY_SWITCHING_T_3_PREDATOR = false;
    private static final Boolean UPDATED_ALLOW_PREY_SWITCHING_T_3_PREDATOR = true;

    private static final String DEFAULT_AUTONOMY_T_3_PREDATOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTONOMY_T_3_PREDATOR = "BBBBBBBBBB";

    private static final Double DEFAULT_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR = 1D;
    private static final Double UPDATED_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR = 2D;

    private static final Double DEFAULT_AVOID_TEAM_WEIGHT_T_3_PREDATOR = 1D;
    private static final Double UPDATED_AVOID_TEAM_WEIGHT_T_3_PREDATOR = 2D;

    private static final Integer DEFAULT_CAPTURE_RANGE_T_3_PREDATOR = 1;
    private static final Integer UPDATED_CAPTURE_RANGE_T_3_PREDATOR = 2;

    private static final Double DEFAULT_CENTROID_WEIGHT_T_3_PREDATOR = 1D;
    private static final Double UPDATED_CENTROID_WEIGHT_T_3_PREDATOR = 2D;

    private static final Integer DEFAULT_COMMS_RANGE_T_3_PREDATOR = 1;
    private static final Integer UPDATED_COMMS_RANGE_T_3_PREDATOR = 2;

    private static final Integer DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR = 1;
    private static final Integer UPDATED_MAX_PRED_SPEED_T_3_PREDATOR = 2;

    private static final Integer DEFAULT_MAX_SPEED_T_3_PREDATOR = 1;
    private static final Integer UPDATED_MAX_SPEED_T_3_PREDATOR = 2;

    @Autowired
    private SimulationRunRepository simulationRunRepository;

    @Autowired
    private SimulationRunService simulationRunService;

    @Autowired
    private SimulationRunQueryService simulationRunQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSimulationRunMockMvc;

    private SimulationRun simulationRun;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SimulationRunResource simulationRunResource = new SimulationRunResource(simulationRunService, simulationRunQueryService);
        this.restSimulationRunMockMvc = MockMvcBuilders.standaloneSetup(simulationRunResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimulationRun createEntity(EntityManager em) {
        SimulationRun simulationRun = new SimulationRun()
            .simulationTrial(DEFAULT_SIMULATION_TRIAL)
            .index(DEFAULT_INDEX)
            .team_id(DEFAULT_TEAM_ID)
            .score(DEFAULT_SCORE)
            .teamCapture(DEFAULT_TEAM_CAPTURE)
            .nonTeamCapture(DEFAULT_NON_TEAM_CAPTURE)
            .job_num(DEFAULT_JOB_NUM)
            .task_num(DEFAULT_TASK_NUM)
            .results_dir(DEFAULT_RESULTS_DIR)
            .num_rows(DEFAULT_NUM_ROWS)
            .align_weight_t_1(DEFAULT_ALIGN_WEIGHT_T_1)
            .avoid_nonteam_weight_t_1(DEFAULT_AVOID_NONTEAM_WEIGHT_T_1)
            .avoid_team_weight_t_1(DEFAULT_AVOID_TEAM_WEIGHT_T_1)
            .centroid_weight_t_1(DEFAULT_CENTROID_WEIGHT_T_1)
            .comms_range_t_1(DEFAULT_COMMS_RANGE_T_1)
            .fov_az_t_1(DEFAULT_FOV_AZ_T_1)
            .fov_el_t_1(DEFAULT_FOV_EL_T_1)
            .goal_weight_t_1(DEFAULT_GOAL_WEIGHT_T_1)
            .max_pred_speed_t_1(DEFAULT_MAX_PRED_SPEED_T_1)
            .max_speed_t_1(DEFAULT_MAX_SPEED_T_1)
            .sphere_of_influence_t_1(DEFAULT_SPHERE_OF_INFLUENCE_T_1)
            .motion_model_t_1(DEFAULT_MOTION_MODEL_T_1)
            .vel_max_t_1(DEFAULT_VEL_MAX_T_1)
            .motion_model_predator(DEFAULT_MOTION_MODEL_PREDATOR)
            .pitch_rate_max_predator(DEFAULT_PITCH_RATE_MAX_PREDATOR)
            .turn_rate_max_predator(DEFAULT_TURN_RATE_MAX_PREDATOR)
            .vel_max_predator(DEFAULT_VEL_MAX_PREDATOR)
            .align_weight_t_2_predator(DEFAULT_ALIGN_WEIGHT_T_2_PREDATOR)
            .allow_prey_switching_t_2_predator(DEFAULT_ALLOW_PREY_SWITCHING_T_2_PREDATOR)
            .autonomy_t_2_predator(DEFAULT_AUTONOMY_T_2_PREDATOR)
            .avoid_nonteam_weight_t_2_predator(DEFAULT_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR)
            .avoid_team_weight_t_2_predator(DEFAULT_AVOID_TEAM_WEIGHT_T_2_PREDATOR)
            .capture_range_t_2_predator(DEFAULT_CAPTURE_RANGE_T_2_PREDATOR)
            .centroid_weight_t_2_predator(DEFAULT_CENTROID_WEIGHT_T_2_PREDATOR)
            .comms_range_t_2_predator(DEFAULT_COMMS_RANGE_T_2_PREDATOR)
            .max_pred_speed_t_2_predator(DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR)
            .max_speed_t_2_predator(DEFAULT_MAX_SPEED_T_2_PREDATOR)
            .align_weight_t_3_predator(DEFAULT_ALIGN_WEIGHT_T_3_PREDATOR)
            .allow_prey_switching_t_3_predator(DEFAULT_ALLOW_PREY_SWITCHING_T_3_PREDATOR)
            .autonomy_t_3_predator(DEFAULT_AUTONOMY_T_3_PREDATOR)
            .avoid_nonteam_weight_t_3_predator(DEFAULT_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR)
            .avoid_team_weight_t_3_predator(DEFAULT_AVOID_TEAM_WEIGHT_T_3_PREDATOR)
            .capture_range_t_3_predator(DEFAULT_CAPTURE_RANGE_T_3_PREDATOR)
            .centroid_weight_t_3_predator(DEFAULT_CENTROID_WEIGHT_T_3_PREDATOR)
            .comms_range_t_3_predator(DEFAULT_COMMS_RANGE_T_3_PREDATOR)
            .max_pred_speed_t_3_predator(DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR)
            .max_speed_t_3_predator(DEFAULT_MAX_SPEED_T_3_PREDATOR);
        return simulationRun;
    }

    @Before
    public void initTest() {
        simulationRun = createEntity(em);
    }

    @Test
    @Transactional
    public void createSimulationRun() throws Exception {
        int databaseSizeBeforeCreate = simulationRunRepository.findAll().size();

        // Create the SimulationRun
        restSimulationRunMockMvc.perform(post("/api/simulation-runs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(simulationRun)))
            .andExpect(status().isCreated());

        // Validate the SimulationRun in the database
        List<SimulationRun> simulationRunList = simulationRunRepository.findAll();
        assertThat(simulationRunList).hasSize(databaseSizeBeforeCreate + 1);
        SimulationRun testSimulationRun = simulationRunList.get(simulationRunList.size() - 1);
        assertThat(testSimulationRun.getSimulationTrial()).isEqualTo(DEFAULT_SIMULATION_TRIAL);
        assertThat(testSimulationRun.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testSimulationRun.getTeam_id()).isEqualTo(DEFAULT_TEAM_ID);
        assertThat(testSimulationRun.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testSimulationRun.getTeamCapture()).isEqualTo(DEFAULT_TEAM_CAPTURE);
        assertThat(testSimulationRun.getNonTeamCapture()).isEqualTo(DEFAULT_NON_TEAM_CAPTURE);
        assertThat(testSimulationRun.getJob_num()).isEqualTo(DEFAULT_JOB_NUM);
        assertThat(testSimulationRun.getTask_num()).isEqualTo(DEFAULT_TASK_NUM);
        assertThat(testSimulationRun.getResults_dir()).isEqualTo(DEFAULT_RESULTS_DIR);
        assertThat(testSimulationRun.getNum_rows()).isEqualTo(DEFAULT_NUM_ROWS);
        assertThat(testSimulationRun.getAlign_weight_t_1()).isEqualTo(DEFAULT_ALIGN_WEIGHT_T_1);
        assertThat(testSimulationRun.getAvoid_nonteam_weight_t_1()).isEqualTo(DEFAULT_AVOID_NONTEAM_WEIGHT_T_1);
        assertThat(testSimulationRun.getAvoid_team_weight_t_1()).isEqualTo(DEFAULT_AVOID_TEAM_WEIGHT_T_1);
        assertThat(testSimulationRun.getCentroid_weight_t_1()).isEqualTo(DEFAULT_CENTROID_WEIGHT_T_1);
        assertThat(testSimulationRun.getComms_range_t_1()).isEqualTo(DEFAULT_COMMS_RANGE_T_1);
        assertThat(testSimulationRun.getFov_az_t_1()).isEqualTo(DEFAULT_FOV_AZ_T_1);
        assertThat(testSimulationRun.getFov_el_t_1()).isEqualTo(DEFAULT_FOV_EL_T_1);
        assertThat(testSimulationRun.getGoal_weight_t_1()).isEqualTo(DEFAULT_GOAL_WEIGHT_T_1);
        assertThat(testSimulationRun.getMax_pred_speed_t_1()).isEqualTo(DEFAULT_MAX_PRED_SPEED_T_1);
        assertThat(testSimulationRun.getMax_speed_t_1()).isEqualTo(DEFAULT_MAX_SPEED_T_1);
        assertThat(testSimulationRun.getSphere_of_influence_t_1()).isEqualTo(DEFAULT_SPHERE_OF_INFLUENCE_T_1);
        assertThat(testSimulationRun.getMotion_model_t_1()).isEqualTo(DEFAULT_MOTION_MODEL_T_1);
        assertThat(testSimulationRun.getVel_max_t_1()).isEqualTo(DEFAULT_VEL_MAX_T_1);
        assertThat(testSimulationRun.getMotion_model_predator()).isEqualTo(DEFAULT_MOTION_MODEL_PREDATOR);
        assertThat(testSimulationRun.getPitch_rate_max_predator()).isEqualTo(DEFAULT_PITCH_RATE_MAX_PREDATOR);
        assertThat(testSimulationRun.getTurn_rate_max_predator()).isEqualTo(DEFAULT_TURN_RATE_MAX_PREDATOR);
        assertThat(testSimulationRun.getVel_max_predator()).isEqualTo(DEFAULT_VEL_MAX_PREDATOR);
        assertThat(testSimulationRun.getAlign_weight_t_2_predator()).isEqualTo(DEFAULT_ALIGN_WEIGHT_T_2_PREDATOR);
        assertThat(testSimulationRun.isAllow_prey_switching_t_2_predator()).isEqualTo(DEFAULT_ALLOW_PREY_SWITCHING_T_2_PREDATOR);
        assertThat(testSimulationRun.getAutonomy_t_2_predator()).isEqualTo(DEFAULT_AUTONOMY_T_2_PREDATOR);
        assertThat(testSimulationRun.getAvoid_nonteam_weight_t_2_predator()).isEqualTo(DEFAULT_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR);
        assertThat(testSimulationRun.getAvoid_team_weight_t_2_predator()).isEqualTo(DEFAULT_AVOID_TEAM_WEIGHT_T_2_PREDATOR);
        assertThat(testSimulationRun.getCapture_range_t_2_predator()).isEqualTo(DEFAULT_CAPTURE_RANGE_T_2_PREDATOR);
        assertThat(testSimulationRun.getCentroid_weight_t_2_predator()).isEqualTo(DEFAULT_CENTROID_WEIGHT_T_2_PREDATOR);
        assertThat(testSimulationRun.getComms_range_t_2_predator()).isEqualTo(DEFAULT_COMMS_RANGE_T_2_PREDATOR);
        assertThat(testSimulationRun.getMax_pred_speed_t_2_predator()).isEqualTo(DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR);
        assertThat(testSimulationRun.getMax_speed_t_2_predator()).isEqualTo(DEFAULT_MAX_SPEED_T_2_PREDATOR);
        assertThat(testSimulationRun.getAlign_weight_t_3_predator()).isEqualTo(DEFAULT_ALIGN_WEIGHT_T_3_PREDATOR);
        assertThat(testSimulationRun.isAllow_prey_switching_t_3_predator()).isEqualTo(DEFAULT_ALLOW_PREY_SWITCHING_T_3_PREDATOR);
        assertThat(testSimulationRun.getAutonomy_t_3_predator()).isEqualTo(DEFAULT_AUTONOMY_T_3_PREDATOR);
        assertThat(testSimulationRun.getAvoid_nonteam_weight_t_3_predator()).isEqualTo(DEFAULT_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR);
        assertThat(testSimulationRun.getAvoid_team_weight_t_3_predator()).isEqualTo(DEFAULT_AVOID_TEAM_WEIGHT_T_3_PREDATOR);
        assertThat(testSimulationRun.getCapture_range_t_3_predator()).isEqualTo(DEFAULT_CAPTURE_RANGE_T_3_PREDATOR);
        assertThat(testSimulationRun.getCentroid_weight_t_3_predator()).isEqualTo(DEFAULT_CENTROID_WEIGHT_T_3_PREDATOR);
        assertThat(testSimulationRun.getComms_range_t_3_predator()).isEqualTo(DEFAULT_COMMS_RANGE_T_3_PREDATOR);
        assertThat(testSimulationRun.getMax_pred_speed_t_3_predator()).isEqualTo(DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR);
        assertThat(testSimulationRun.getMax_speed_t_3_predator()).isEqualTo(DEFAULT_MAX_SPEED_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void createSimulationRunWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = simulationRunRepository.findAll().size();

        // Create the SimulationRun with an existing ID
        simulationRun.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSimulationRunMockMvc.perform(post("/api/simulation-runs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(simulationRun)))
            .andExpect(status().isBadRequest());

        // Validate the SimulationRun in the database
        List<SimulationRun> simulationRunList = simulationRunRepository.findAll();
        assertThat(simulationRunList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSimulationRuns() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList
        restSimulationRunMockMvc.perform(get("/api/simulation-runs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(simulationRun.getId().intValue())))
            .andExpect(jsonPath("$.[*].simulationTrial").value(hasItem(DEFAULT_SIMULATION_TRIAL.toString())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].team_id").value(hasItem(DEFAULT_TEAM_ID)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].teamCapture").value(hasItem(DEFAULT_TEAM_CAPTURE)))
            .andExpect(jsonPath("$.[*].nonTeamCapture").value(hasItem(DEFAULT_NON_TEAM_CAPTURE)))
            .andExpect(jsonPath("$.[*].job_num").value(hasItem(DEFAULT_JOB_NUM)))
            .andExpect(jsonPath("$.[*].task_num").value(hasItem(DEFAULT_TASK_NUM)))
            .andExpect(jsonPath("$.[*].results_dir").value(hasItem(DEFAULT_RESULTS_DIR.toString())))
            .andExpect(jsonPath("$.[*].num_rows").value(hasItem(DEFAULT_NUM_ROWS)))
            .andExpect(jsonPath("$.[*].align_weight_t_1").value(hasItem(DEFAULT_ALIGN_WEIGHT_T_1.doubleValue())))
            .andExpect(jsonPath("$.[*].avoid_nonteam_weight_t_1").value(hasItem(DEFAULT_AVOID_NONTEAM_WEIGHT_T_1.doubleValue())))
            .andExpect(jsonPath("$.[*].avoid_team_weight_t_1").value(hasItem(DEFAULT_AVOID_TEAM_WEIGHT_T_1.doubleValue())))
            .andExpect(jsonPath("$.[*].centroid_weight_t_1").value(hasItem(DEFAULT_CENTROID_WEIGHT_T_1.doubleValue())))
            .andExpect(jsonPath("$.[*].comms_range_t_1").value(hasItem(DEFAULT_COMMS_RANGE_T_1)))
            .andExpect(jsonPath("$.[*].fov_az_t_1").value(hasItem(DEFAULT_FOV_AZ_T_1)))
            .andExpect(jsonPath("$.[*].fov_el_t_1").value(hasItem(DEFAULT_FOV_EL_T_1)))
            .andExpect(jsonPath("$.[*].goal_weight_t_1").value(hasItem(DEFAULT_GOAL_WEIGHT_T_1.doubleValue())))
            .andExpect(jsonPath("$.[*].max_pred_speed_t_1").value(hasItem(DEFAULT_MAX_PRED_SPEED_T_1)))
            .andExpect(jsonPath("$.[*].max_speed_t_1").value(hasItem(DEFAULT_MAX_SPEED_T_1)))
            .andExpect(jsonPath("$.[*].sphere_of_influence_t_1").value(hasItem(DEFAULT_SPHERE_OF_INFLUENCE_T_1)))
            .andExpect(jsonPath("$.[*].motion_model_t_1").value(hasItem(DEFAULT_MOTION_MODEL_T_1.toString())))
            .andExpect(jsonPath("$.[*].vel_max_t_1").value(hasItem(DEFAULT_VEL_MAX_T_1)))
            .andExpect(jsonPath("$.[*].motion_model_predator").value(hasItem(DEFAULT_MOTION_MODEL_PREDATOR.toString())))
            .andExpect(jsonPath("$.[*].pitch_rate_max_predator").value(hasItem(DEFAULT_PITCH_RATE_MAX_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].turn_rate_max_predator").value(hasItem(DEFAULT_TURN_RATE_MAX_PREDATOR)))
            .andExpect(jsonPath("$.[*].vel_max_predator").value(hasItem(DEFAULT_VEL_MAX_PREDATOR)))
            .andExpect(jsonPath("$.[*].align_weight_t_2_predator").value(hasItem(DEFAULT_ALIGN_WEIGHT_T_2_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].allow_prey_switching_t_2_predator").value(hasItem(DEFAULT_ALLOW_PREY_SWITCHING_T_2_PREDATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].autonomy_t_2_predator").value(hasItem(DEFAULT_AUTONOMY_T_2_PREDATOR.toString())))
            .andExpect(jsonPath("$.[*].avoid_nonteam_weight_t_2_predator").value(hasItem(DEFAULT_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].avoid_team_weight_t_2_predator").value(hasItem(DEFAULT_AVOID_TEAM_WEIGHT_T_2_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].capture_range_t_2_predator").value(hasItem(DEFAULT_CAPTURE_RANGE_T_2_PREDATOR)))
            .andExpect(jsonPath("$.[*].centroid_weight_t_2_predator").value(hasItem(DEFAULT_CENTROID_WEIGHT_T_2_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].comms_range_t_2_predator").value(hasItem(DEFAULT_COMMS_RANGE_T_2_PREDATOR)))
            .andExpect(jsonPath("$.[*].max_pred_speed_t_2_predator").value(hasItem(DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR)))
            .andExpect(jsonPath("$.[*].max_speed_t_2_predator").value(hasItem(DEFAULT_MAX_SPEED_T_2_PREDATOR)))
            .andExpect(jsonPath("$.[*].align_weight_t_3_predator").value(hasItem(DEFAULT_ALIGN_WEIGHT_T_3_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].allow_prey_switching_t_3_predator").value(hasItem(DEFAULT_ALLOW_PREY_SWITCHING_T_3_PREDATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].autonomy_t_3_predator").value(hasItem(DEFAULT_AUTONOMY_T_3_PREDATOR.toString())))
            .andExpect(jsonPath("$.[*].avoid_nonteam_weight_t_3_predator").value(hasItem(DEFAULT_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].avoid_team_weight_t_3_predator").value(hasItem(DEFAULT_AVOID_TEAM_WEIGHT_T_3_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].capture_range_t_3_predator").value(hasItem(DEFAULT_CAPTURE_RANGE_T_3_PREDATOR)))
            .andExpect(jsonPath("$.[*].centroid_weight_t_3_predator").value(hasItem(DEFAULT_CENTROID_WEIGHT_T_3_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].comms_range_t_3_predator").value(hasItem(DEFAULT_COMMS_RANGE_T_3_PREDATOR)))
            .andExpect(jsonPath("$.[*].max_pred_speed_t_3_predator").value(hasItem(DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR)))
            .andExpect(jsonPath("$.[*].max_speed_t_3_predator").value(hasItem(DEFAULT_MAX_SPEED_T_3_PREDATOR)));
    }

    @Test
    @Transactional
    public void getSimulationRun() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get the simulationRun
        restSimulationRunMockMvc.perform(get("/api/simulation-runs/{id}", simulationRun.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(simulationRun.getId().intValue()))
            .andExpect(jsonPath("$.simulationTrial").value(DEFAULT_SIMULATION_TRIAL.toString()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX))
            .andExpect(jsonPath("$.team_id").value(DEFAULT_TEAM_ID))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.teamCapture").value(DEFAULT_TEAM_CAPTURE))
            .andExpect(jsonPath("$.nonTeamCapture").value(DEFAULT_NON_TEAM_CAPTURE))
            .andExpect(jsonPath("$.job_num").value(DEFAULT_JOB_NUM))
            .andExpect(jsonPath("$.task_num").value(DEFAULT_TASK_NUM))
            .andExpect(jsonPath("$.results_dir").value(DEFAULT_RESULTS_DIR.toString()))
            .andExpect(jsonPath("$.num_rows").value(DEFAULT_NUM_ROWS))
            .andExpect(jsonPath("$.align_weight_t_1").value(DEFAULT_ALIGN_WEIGHT_T_1.doubleValue()))
            .andExpect(jsonPath("$.avoid_nonteam_weight_t_1").value(DEFAULT_AVOID_NONTEAM_WEIGHT_T_1.doubleValue()))
            .andExpect(jsonPath("$.avoid_team_weight_t_1").value(DEFAULT_AVOID_TEAM_WEIGHT_T_1.doubleValue()))
            .andExpect(jsonPath("$.centroid_weight_t_1").value(DEFAULT_CENTROID_WEIGHT_T_1.doubleValue()))
            .andExpect(jsonPath("$.comms_range_t_1").value(DEFAULT_COMMS_RANGE_T_1))
            .andExpect(jsonPath("$.fov_az_t_1").value(DEFAULT_FOV_AZ_T_1))
            .andExpect(jsonPath("$.fov_el_t_1").value(DEFAULT_FOV_EL_T_1))
            .andExpect(jsonPath("$.goal_weight_t_1").value(DEFAULT_GOAL_WEIGHT_T_1.doubleValue()))
            .andExpect(jsonPath("$.max_pred_speed_t_1").value(DEFAULT_MAX_PRED_SPEED_T_1))
            .andExpect(jsonPath("$.max_speed_t_1").value(DEFAULT_MAX_SPEED_T_1))
            .andExpect(jsonPath("$.sphere_of_influence_t_1").value(DEFAULT_SPHERE_OF_INFLUENCE_T_1))
            .andExpect(jsonPath("$.motion_model_t_1").value(DEFAULT_MOTION_MODEL_T_1.toString()))
            .andExpect(jsonPath("$.vel_max_t_1").value(DEFAULT_VEL_MAX_T_1))
            .andExpect(jsonPath("$.motion_model_predator").value(DEFAULT_MOTION_MODEL_PREDATOR.toString()))
            .andExpect(jsonPath("$.pitch_rate_max_predator").value(DEFAULT_PITCH_RATE_MAX_PREDATOR.doubleValue()))
            .andExpect(jsonPath("$.turn_rate_max_predator").value(DEFAULT_TURN_RATE_MAX_PREDATOR))
            .andExpect(jsonPath("$.vel_max_predator").value(DEFAULT_VEL_MAX_PREDATOR))
            .andExpect(jsonPath("$.align_weight_t_2_predator").value(DEFAULT_ALIGN_WEIGHT_T_2_PREDATOR.doubleValue()))
            .andExpect(jsonPath("$.allow_prey_switching_t_2_predator").value(DEFAULT_ALLOW_PREY_SWITCHING_T_2_PREDATOR.booleanValue()))
            .andExpect(jsonPath("$.autonomy_t_2_predator").value(DEFAULT_AUTONOMY_T_2_PREDATOR.toString()))
            .andExpect(jsonPath("$.avoid_nonteam_weight_t_2_predator").value(DEFAULT_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR.doubleValue()))
            .andExpect(jsonPath("$.avoid_team_weight_t_2_predator").value(DEFAULT_AVOID_TEAM_WEIGHT_T_2_PREDATOR.doubleValue()))
            .andExpect(jsonPath("$.capture_range_t_2_predator").value(DEFAULT_CAPTURE_RANGE_T_2_PREDATOR))
            .andExpect(jsonPath("$.centroid_weight_t_2_predator").value(DEFAULT_CENTROID_WEIGHT_T_2_PREDATOR.doubleValue()))
            .andExpect(jsonPath("$.comms_range_t_2_predator").value(DEFAULT_COMMS_RANGE_T_2_PREDATOR))
            .andExpect(jsonPath("$.max_pred_speed_t_2_predator").value(DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR))
            .andExpect(jsonPath("$.max_speed_t_2_predator").value(DEFAULT_MAX_SPEED_T_2_PREDATOR))
            .andExpect(jsonPath("$.align_weight_t_3_predator").value(DEFAULT_ALIGN_WEIGHT_T_3_PREDATOR.doubleValue()))
            .andExpect(jsonPath("$.allow_prey_switching_t_3_predator").value(DEFAULT_ALLOW_PREY_SWITCHING_T_3_PREDATOR.booleanValue()))
            .andExpect(jsonPath("$.autonomy_t_3_predator").value(DEFAULT_AUTONOMY_T_3_PREDATOR.toString()))
            .andExpect(jsonPath("$.avoid_nonteam_weight_t_3_predator").value(DEFAULT_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR.doubleValue()))
            .andExpect(jsonPath("$.avoid_team_weight_t_3_predator").value(DEFAULT_AVOID_TEAM_WEIGHT_T_3_PREDATOR.doubleValue()))
            .andExpect(jsonPath("$.capture_range_t_3_predator").value(DEFAULT_CAPTURE_RANGE_T_3_PREDATOR))
            .andExpect(jsonPath("$.centroid_weight_t_3_predator").value(DEFAULT_CENTROID_WEIGHT_T_3_PREDATOR.doubleValue()))
            .andExpect(jsonPath("$.comms_range_t_3_predator").value(DEFAULT_COMMS_RANGE_T_3_PREDATOR))
            .andExpect(jsonPath("$.max_pred_speed_t_3_predator").value(DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR))
            .andExpect(jsonPath("$.max_speed_t_3_predator").value(DEFAULT_MAX_SPEED_T_3_PREDATOR));
    }

    @Test
    @Transactional
    public void getAllSimulationRunsBySimulationTrialIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where simulationTrial equals to DEFAULT_SIMULATION_TRIAL
        defaultSimulationRunShouldBeFound("simulationTrial.equals=" + DEFAULT_SIMULATION_TRIAL);

        // Get all the simulationRunList where simulationTrial equals to UPDATED_SIMULATION_TRIAL
        defaultSimulationRunShouldNotBeFound("simulationTrial.equals=" + UPDATED_SIMULATION_TRIAL);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsBySimulationTrialIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where simulationTrial in DEFAULT_SIMULATION_TRIAL or UPDATED_SIMULATION_TRIAL
        defaultSimulationRunShouldBeFound("simulationTrial.in=" + DEFAULT_SIMULATION_TRIAL + "," + UPDATED_SIMULATION_TRIAL);

        // Get all the simulationRunList where simulationTrial equals to UPDATED_SIMULATION_TRIAL
        defaultSimulationRunShouldNotBeFound("simulationTrial.in=" + UPDATED_SIMULATION_TRIAL);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsBySimulationTrialIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where simulationTrial is not null
        defaultSimulationRunShouldBeFound("simulationTrial.specified=true");

        // Get all the simulationRunList where simulationTrial is null
        defaultSimulationRunShouldNotBeFound("simulationTrial.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where index equals to DEFAULT_INDEX
        defaultSimulationRunShouldBeFound("index.equals=" + DEFAULT_INDEX);

        // Get all the simulationRunList where index equals to UPDATED_INDEX
        defaultSimulationRunShouldNotBeFound("index.equals=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByIndexIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where index in DEFAULT_INDEX or UPDATED_INDEX
        defaultSimulationRunShouldBeFound("index.in=" + DEFAULT_INDEX + "," + UPDATED_INDEX);

        // Get all the simulationRunList where index equals to UPDATED_INDEX
        defaultSimulationRunShouldNotBeFound("index.in=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where index is not null
        defaultSimulationRunShouldBeFound("index.specified=true");

        // Get all the simulationRunList where index is null
        defaultSimulationRunShouldNotBeFound("index.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByIndexIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where index greater than or equals to DEFAULT_INDEX
        defaultSimulationRunShouldBeFound("index.greaterOrEqualThan=" + DEFAULT_INDEX);

        // Get all the simulationRunList where index greater than or equals to UPDATED_INDEX
        defaultSimulationRunShouldNotBeFound("index.greaterOrEqualThan=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByIndexIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where index less than or equals to DEFAULT_INDEX
        defaultSimulationRunShouldNotBeFound("index.lessThan=" + DEFAULT_INDEX);

        // Get all the simulationRunList where index less than or equals to UPDATED_INDEX
        defaultSimulationRunShouldBeFound("index.lessThan=" + UPDATED_INDEX);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByTeam_idIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where team_id equals to DEFAULT_TEAM_ID
        defaultSimulationRunShouldBeFound("team_id.equals=" + DEFAULT_TEAM_ID);

        // Get all the simulationRunList where team_id equals to UPDATED_TEAM_ID
        defaultSimulationRunShouldNotBeFound("team_id.equals=" + UPDATED_TEAM_ID);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTeam_idIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where team_id in DEFAULT_TEAM_ID or UPDATED_TEAM_ID
        defaultSimulationRunShouldBeFound("team_id.in=" + DEFAULT_TEAM_ID + "," + UPDATED_TEAM_ID);

        // Get all the simulationRunList where team_id equals to UPDATED_TEAM_ID
        defaultSimulationRunShouldNotBeFound("team_id.in=" + UPDATED_TEAM_ID);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTeam_idIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where team_id is not null
        defaultSimulationRunShouldBeFound("team_id.specified=true");

        // Get all the simulationRunList where team_id is null
        defaultSimulationRunShouldNotBeFound("team_id.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTeam_idIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where team_id greater than or equals to DEFAULT_TEAM_ID
        defaultSimulationRunShouldBeFound("team_id.greaterOrEqualThan=" + DEFAULT_TEAM_ID);

        // Get all the simulationRunList where team_id greater than or equals to UPDATED_TEAM_ID
        defaultSimulationRunShouldNotBeFound("team_id.greaterOrEqualThan=" + UPDATED_TEAM_ID);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTeam_idIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where team_id less than or equals to DEFAULT_TEAM_ID
        defaultSimulationRunShouldNotBeFound("team_id.lessThan=" + DEFAULT_TEAM_ID);

        // Get all the simulationRunList where team_id less than or equals to UPDATED_TEAM_ID
        defaultSimulationRunShouldBeFound("team_id.lessThan=" + UPDATED_TEAM_ID);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where score equals to DEFAULT_SCORE
        defaultSimulationRunShouldBeFound("score.equals=" + DEFAULT_SCORE);

        // Get all the simulationRunList where score equals to UPDATED_SCORE
        defaultSimulationRunShouldNotBeFound("score.equals=" + UPDATED_SCORE);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByScoreIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where score in DEFAULT_SCORE or UPDATED_SCORE
        defaultSimulationRunShouldBeFound("score.in=" + DEFAULT_SCORE + "," + UPDATED_SCORE);

        // Get all the simulationRunList where score equals to UPDATED_SCORE
        defaultSimulationRunShouldNotBeFound("score.in=" + UPDATED_SCORE);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where score is not null
        defaultSimulationRunShouldBeFound("score.specified=true");

        // Get all the simulationRunList where score is null
        defaultSimulationRunShouldNotBeFound("score.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where score greater than or equals to DEFAULT_SCORE
        defaultSimulationRunShouldBeFound("score.greaterOrEqualThan=" + DEFAULT_SCORE);

        // Get all the simulationRunList where score greater than or equals to UPDATED_SCORE
        defaultSimulationRunShouldNotBeFound("score.greaterOrEqualThan=" + UPDATED_SCORE);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where score less than or equals to DEFAULT_SCORE
        defaultSimulationRunShouldNotBeFound("score.lessThan=" + DEFAULT_SCORE);

        // Get all the simulationRunList where score less than or equals to UPDATED_SCORE
        defaultSimulationRunShouldBeFound("score.lessThan=" + UPDATED_SCORE);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByTeamCaptureIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where teamCapture equals to DEFAULT_TEAM_CAPTURE
        defaultSimulationRunShouldBeFound("teamCapture.equals=" + DEFAULT_TEAM_CAPTURE);

        // Get all the simulationRunList where teamCapture equals to UPDATED_TEAM_CAPTURE
        defaultSimulationRunShouldNotBeFound("teamCapture.equals=" + UPDATED_TEAM_CAPTURE);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTeamCaptureIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where teamCapture in DEFAULT_TEAM_CAPTURE or UPDATED_TEAM_CAPTURE
        defaultSimulationRunShouldBeFound("teamCapture.in=" + DEFAULT_TEAM_CAPTURE + "," + UPDATED_TEAM_CAPTURE);

        // Get all the simulationRunList where teamCapture equals to UPDATED_TEAM_CAPTURE
        defaultSimulationRunShouldNotBeFound("teamCapture.in=" + UPDATED_TEAM_CAPTURE);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTeamCaptureIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where teamCapture is not null
        defaultSimulationRunShouldBeFound("teamCapture.specified=true");

        // Get all the simulationRunList where teamCapture is null
        defaultSimulationRunShouldNotBeFound("teamCapture.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTeamCaptureIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where teamCapture greater than or equals to DEFAULT_TEAM_CAPTURE
        defaultSimulationRunShouldBeFound("teamCapture.greaterOrEqualThan=" + DEFAULT_TEAM_CAPTURE);

        // Get all the simulationRunList where teamCapture greater than or equals to UPDATED_TEAM_CAPTURE
        defaultSimulationRunShouldNotBeFound("teamCapture.greaterOrEqualThan=" + UPDATED_TEAM_CAPTURE);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTeamCaptureIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where teamCapture less than or equals to DEFAULT_TEAM_CAPTURE
        defaultSimulationRunShouldNotBeFound("teamCapture.lessThan=" + DEFAULT_TEAM_CAPTURE);

        // Get all the simulationRunList where teamCapture less than or equals to UPDATED_TEAM_CAPTURE
        defaultSimulationRunShouldBeFound("teamCapture.lessThan=" + UPDATED_TEAM_CAPTURE);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByNonTeamCaptureIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where nonTeamCapture equals to DEFAULT_NON_TEAM_CAPTURE
        defaultSimulationRunShouldBeFound("nonTeamCapture.equals=" + DEFAULT_NON_TEAM_CAPTURE);

        // Get all the simulationRunList where nonTeamCapture equals to UPDATED_NON_TEAM_CAPTURE
        defaultSimulationRunShouldNotBeFound("nonTeamCapture.equals=" + UPDATED_NON_TEAM_CAPTURE);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByNonTeamCaptureIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where nonTeamCapture in DEFAULT_NON_TEAM_CAPTURE or UPDATED_NON_TEAM_CAPTURE
        defaultSimulationRunShouldBeFound("nonTeamCapture.in=" + DEFAULT_NON_TEAM_CAPTURE + "," + UPDATED_NON_TEAM_CAPTURE);

        // Get all the simulationRunList where nonTeamCapture equals to UPDATED_NON_TEAM_CAPTURE
        defaultSimulationRunShouldNotBeFound("nonTeamCapture.in=" + UPDATED_NON_TEAM_CAPTURE);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByNonTeamCaptureIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where nonTeamCapture is not null
        defaultSimulationRunShouldBeFound("nonTeamCapture.specified=true");

        // Get all the simulationRunList where nonTeamCapture is null
        defaultSimulationRunShouldNotBeFound("nonTeamCapture.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByNonTeamCaptureIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where nonTeamCapture greater than or equals to DEFAULT_NON_TEAM_CAPTURE
        defaultSimulationRunShouldBeFound("nonTeamCapture.greaterOrEqualThan=" + DEFAULT_NON_TEAM_CAPTURE);

        // Get all the simulationRunList where nonTeamCapture greater than or equals to UPDATED_NON_TEAM_CAPTURE
        defaultSimulationRunShouldNotBeFound("nonTeamCapture.greaterOrEqualThan=" + UPDATED_NON_TEAM_CAPTURE);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByNonTeamCaptureIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where nonTeamCapture less than or equals to DEFAULT_NON_TEAM_CAPTURE
        defaultSimulationRunShouldNotBeFound("nonTeamCapture.lessThan=" + DEFAULT_NON_TEAM_CAPTURE);

        // Get all the simulationRunList where nonTeamCapture less than or equals to UPDATED_NON_TEAM_CAPTURE
        defaultSimulationRunShouldBeFound("nonTeamCapture.lessThan=" + UPDATED_NON_TEAM_CAPTURE);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByJob_numIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where job_num equals to DEFAULT_JOB_NUM
        defaultSimulationRunShouldBeFound("job_num.equals=" + DEFAULT_JOB_NUM);

        // Get all the simulationRunList where job_num equals to UPDATED_JOB_NUM
        defaultSimulationRunShouldNotBeFound("job_num.equals=" + UPDATED_JOB_NUM);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByJob_numIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where job_num in DEFAULT_JOB_NUM or UPDATED_JOB_NUM
        defaultSimulationRunShouldBeFound("job_num.in=" + DEFAULT_JOB_NUM + "," + UPDATED_JOB_NUM);

        // Get all the simulationRunList where job_num equals to UPDATED_JOB_NUM
        defaultSimulationRunShouldNotBeFound("job_num.in=" + UPDATED_JOB_NUM);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByJob_numIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where job_num is not null
        defaultSimulationRunShouldBeFound("job_num.specified=true");

        // Get all the simulationRunList where job_num is null
        defaultSimulationRunShouldNotBeFound("job_num.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByJob_numIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where job_num greater than or equals to DEFAULT_JOB_NUM
        defaultSimulationRunShouldBeFound("job_num.greaterOrEqualThan=" + DEFAULT_JOB_NUM);

        // Get all the simulationRunList where job_num greater than or equals to UPDATED_JOB_NUM
        defaultSimulationRunShouldNotBeFound("job_num.greaterOrEqualThan=" + UPDATED_JOB_NUM);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByJob_numIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where job_num less than or equals to DEFAULT_JOB_NUM
        defaultSimulationRunShouldNotBeFound("job_num.lessThan=" + DEFAULT_JOB_NUM);

        // Get all the simulationRunList where job_num less than or equals to UPDATED_JOB_NUM
        defaultSimulationRunShouldBeFound("job_num.lessThan=" + UPDATED_JOB_NUM);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByTask_numIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where task_num equals to DEFAULT_TASK_NUM
        defaultSimulationRunShouldBeFound("task_num.equals=" + DEFAULT_TASK_NUM);

        // Get all the simulationRunList where task_num equals to UPDATED_TASK_NUM
        defaultSimulationRunShouldNotBeFound("task_num.equals=" + UPDATED_TASK_NUM);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTask_numIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where task_num in DEFAULT_TASK_NUM or UPDATED_TASK_NUM
        defaultSimulationRunShouldBeFound("task_num.in=" + DEFAULT_TASK_NUM + "," + UPDATED_TASK_NUM);

        // Get all the simulationRunList where task_num equals to UPDATED_TASK_NUM
        defaultSimulationRunShouldNotBeFound("task_num.in=" + UPDATED_TASK_NUM);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTask_numIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where task_num is not null
        defaultSimulationRunShouldBeFound("task_num.specified=true");

        // Get all the simulationRunList where task_num is null
        defaultSimulationRunShouldNotBeFound("task_num.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTask_numIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where task_num greater than or equals to DEFAULT_TASK_NUM
        defaultSimulationRunShouldBeFound("task_num.greaterOrEqualThan=" + DEFAULT_TASK_NUM);

        // Get all the simulationRunList where task_num greater than or equals to UPDATED_TASK_NUM
        defaultSimulationRunShouldNotBeFound("task_num.greaterOrEqualThan=" + UPDATED_TASK_NUM);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTask_numIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where task_num less than or equals to DEFAULT_TASK_NUM
        defaultSimulationRunShouldNotBeFound("task_num.lessThan=" + DEFAULT_TASK_NUM);

        // Get all the simulationRunList where task_num less than or equals to UPDATED_TASK_NUM
        defaultSimulationRunShouldBeFound("task_num.lessThan=" + UPDATED_TASK_NUM);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByResults_dirIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where results_dir equals to DEFAULT_RESULTS_DIR
        defaultSimulationRunShouldBeFound("results_dir.equals=" + DEFAULT_RESULTS_DIR);

        // Get all the simulationRunList where results_dir equals to UPDATED_RESULTS_DIR
        defaultSimulationRunShouldNotBeFound("results_dir.equals=" + UPDATED_RESULTS_DIR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByResults_dirIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where results_dir in DEFAULT_RESULTS_DIR or UPDATED_RESULTS_DIR
        defaultSimulationRunShouldBeFound("results_dir.in=" + DEFAULT_RESULTS_DIR + "," + UPDATED_RESULTS_DIR);

        // Get all the simulationRunList where results_dir equals to UPDATED_RESULTS_DIR
        defaultSimulationRunShouldNotBeFound("results_dir.in=" + UPDATED_RESULTS_DIR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByResults_dirIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where results_dir is not null
        defaultSimulationRunShouldBeFound("results_dir.specified=true");

        // Get all the simulationRunList where results_dir is null
        defaultSimulationRunShouldNotBeFound("results_dir.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByNum_rowsIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where num_rows equals to DEFAULT_NUM_ROWS
        defaultSimulationRunShouldBeFound("num_rows.equals=" + DEFAULT_NUM_ROWS);

        // Get all the simulationRunList where num_rows equals to UPDATED_NUM_ROWS
        defaultSimulationRunShouldNotBeFound("num_rows.equals=" + UPDATED_NUM_ROWS);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByNum_rowsIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where num_rows in DEFAULT_NUM_ROWS or UPDATED_NUM_ROWS
        defaultSimulationRunShouldBeFound("num_rows.in=" + DEFAULT_NUM_ROWS + "," + UPDATED_NUM_ROWS);

        // Get all the simulationRunList where num_rows equals to UPDATED_NUM_ROWS
        defaultSimulationRunShouldNotBeFound("num_rows.in=" + UPDATED_NUM_ROWS);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByNum_rowsIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where num_rows is not null
        defaultSimulationRunShouldBeFound("num_rows.specified=true");

        // Get all the simulationRunList where num_rows is null
        defaultSimulationRunShouldNotBeFound("num_rows.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByNum_rowsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where num_rows greater than or equals to DEFAULT_NUM_ROWS
        defaultSimulationRunShouldBeFound("num_rows.greaterOrEqualThan=" + DEFAULT_NUM_ROWS);

        // Get all the simulationRunList where num_rows greater than or equals to UPDATED_NUM_ROWS
        defaultSimulationRunShouldNotBeFound("num_rows.greaterOrEqualThan=" + UPDATED_NUM_ROWS);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByNum_rowsIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where num_rows less than or equals to DEFAULT_NUM_ROWS
        defaultSimulationRunShouldNotBeFound("num_rows.lessThan=" + DEFAULT_NUM_ROWS);

        // Get all the simulationRunList where num_rows less than or equals to UPDATED_NUM_ROWS
        defaultSimulationRunShouldBeFound("num_rows.lessThan=" + UPDATED_NUM_ROWS);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByAlign_weight_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where align_weight_t_1 equals to DEFAULT_ALIGN_WEIGHT_T_1
        defaultSimulationRunShouldBeFound("align_weight_t_1.equals=" + DEFAULT_ALIGN_WEIGHT_T_1);

        // Get all the simulationRunList where align_weight_t_1 equals to UPDATED_ALIGN_WEIGHT_T_1
        defaultSimulationRunShouldNotBeFound("align_weight_t_1.equals=" + UPDATED_ALIGN_WEIGHT_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAlign_weight_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where align_weight_t_1 in DEFAULT_ALIGN_WEIGHT_T_1 or UPDATED_ALIGN_WEIGHT_T_1
        defaultSimulationRunShouldBeFound("align_weight_t_1.in=" + DEFAULT_ALIGN_WEIGHT_T_1 + "," + UPDATED_ALIGN_WEIGHT_T_1);

        // Get all the simulationRunList where align_weight_t_1 equals to UPDATED_ALIGN_WEIGHT_T_1
        defaultSimulationRunShouldNotBeFound("align_weight_t_1.in=" + UPDATED_ALIGN_WEIGHT_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAlign_weight_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where align_weight_t_1 is not null
        defaultSimulationRunShouldBeFound("align_weight_t_1.specified=true");

        // Get all the simulationRunList where align_weight_t_1 is null
        defaultSimulationRunShouldNotBeFound("align_weight_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_nonteam_weight_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_nonteam_weight_t_1 equals to DEFAULT_AVOID_NONTEAM_WEIGHT_T_1
        defaultSimulationRunShouldBeFound("avoid_nonteam_weight_t_1.equals=" + DEFAULT_AVOID_NONTEAM_WEIGHT_T_1);

        // Get all the simulationRunList where avoid_nonteam_weight_t_1 equals to UPDATED_AVOID_NONTEAM_WEIGHT_T_1
        defaultSimulationRunShouldNotBeFound("avoid_nonteam_weight_t_1.equals=" + UPDATED_AVOID_NONTEAM_WEIGHT_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_nonteam_weight_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_nonteam_weight_t_1 in DEFAULT_AVOID_NONTEAM_WEIGHT_T_1 or UPDATED_AVOID_NONTEAM_WEIGHT_T_1
        defaultSimulationRunShouldBeFound("avoid_nonteam_weight_t_1.in=" + DEFAULT_AVOID_NONTEAM_WEIGHT_T_1 + "," + UPDATED_AVOID_NONTEAM_WEIGHT_T_1);

        // Get all the simulationRunList where avoid_nonteam_weight_t_1 equals to UPDATED_AVOID_NONTEAM_WEIGHT_T_1
        defaultSimulationRunShouldNotBeFound("avoid_nonteam_weight_t_1.in=" + UPDATED_AVOID_NONTEAM_WEIGHT_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_nonteam_weight_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_nonteam_weight_t_1 is not null
        defaultSimulationRunShouldBeFound("avoid_nonteam_weight_t_1.specified=true");

        // Get all the simulationRunList where avoid_nonteam_weight_t_1 is null
        defaultSimulationRunShouldNotBeFound("avoid_nonteam_weight_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_team_weight_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_team_weight_t_1 equals to DEFAULT_AVOID_TEAM_WEIGHT_T_1
        defaultSimulationRunShouldBeFound("avoid_team_weight_t_1.equals=" + DEFAULT_AVOID_TEAM_WEIGHT_T_1);

        // Get all the simulationRunList where avoid_team_weight_t_1 equals to UPDATED_AVOID_TEAM_WEIGHT_T_1
        defaultSimulationRunShouldNotBeFound("avoid_team_weight_t_1.equals=" + UPDATED_AVOID_TEAM_WEIGHT_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_team_weight_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_team_weight_t_1 in DEFAULT_AVOID_TEAM_WEIGHT_T_1 or UPDATED_AVOID_TEAM_WEIGHT_T_1
        defaultSimulationRunShouldBeFound("avoid_team_weight_t_1.in=" + DEFAULT_AVOID_TEAM_WEIGHT_T_1 + "," + UPDATED_AVOID_TEAM_WEIGHT_T_1);

        // Get all the simulationRunList where avoid_team_weight_t_1 equals to UPDATED_AVOID_TEAM_WEIGHT_T_1
        defaultSimulationRunShouldNotBeFound("avoid_team_weight_t_1.in=" + UPDATED_AVOID_TEAM_WEIGHT_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_team_weight_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_team_weight_t_1 is not null
        defaultSimulationRunShouldBeFound("avoid_team_weight_t_1.specified=true");

        // Get all the simulationRunList where avoid_team_weight_t_1 is null
        defaultSimulationRunShouldNotBeFound("avoid_team_weight_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCentroid_weight_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where centroid_weight_t_1 equals to DEFAULT_CENTROID_WEIGHT_T_1
        defaultSimulationRunShouldBeFound("centroid_weight_t_1.equals=" + DEFAULT_CENTROID_WEIGHT_T_1);

        // Get all the simulationRunList where centroid_weight_t_1 equals to UPDATED_CENTROID_WEIGHT_T_1
        defaultSimulationRunShouldNotBeFound("centroid_weight_t_1.equals=" + UPDATED_CENTROID_WEIGHT_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCentroid_weight_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where centroid_weight_t_1 in DEFAULT_CENTROID_WEIGHT_T_1 or UPDATED_CENTROID_WEIGHT_T_1
        defaultSimulationRunShouldBeFound("centroid_weight_t_1.in=" + DEFAULT_CENTROID_WEIGHT_T_1 + "," + UPDATED_CENTROID_WEIGHT_T_1);

        // Get all the simulationRunList where centroid_weight_t_1 equals to UPDATED_CENTROID_WEIGHT_T_1
        defaultSimulationRunShouldNotBeFound("centroid_weight_t_1.in=" + UPDATED_CENTROID_WEIGHT_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCentroid_weight_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where centroid_weight_t_1 is not null
        defaultSimulationRunShouldBeFound("centroid_weight_t_1.specified=true");

        // Get all the simulationRunList where centroid_weight_t_1 is null
        defaultSimulationRunShouldNotBeFound("centroid_weight_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_1 equals to DEFAULT_COMMS_RANGE_T_1
        defaultSimulationRunShouldBeFound("comms_range_t_1.equals=" + DEFAULT_COMMS_RANGE_T_1);

        // Get all the simulationRunList where comms_range_t_1 equals to UPDATED_COMMS_RANGE_T_1
        defaultSimulationRunShouldNotBeFound("comms_range_t_1.equals=" + UPDATED_COMMS_RANGE_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_1 in DEFAULT_COMMS_RANGE_T_1 or UPDATED_COMMS_RANGE_T_1
        defaultSimulationRunShouldBeFound("comms_range_t_1.in=" + DEFAULT_COMMS_RANGE_T_1 + "," + UPDATED_COMMS_RANGE_T_1);

        // Get all the simulationRunList where comms_range_t_1 equals to UPDATED_COMMS_RANGE_T_1
        defaultSimulationRunShouldNotBeFound("comms_range_t_1.in=" + UPDATED_COMMS_RANGE_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_1 is not null
        defaultSimulationRunShouldBeFound("comms_range_t_1.specified=true");

        // Get all the simulationRunList where comms_range_t_1 is null
        defaultSimulationRunShouldNotBeFound("comms_range_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_1IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_1 greater than or equals to DEFAULT_COMMS_RANGE_T_1
        defaultSimulationRunShouldBeFound("comms_range_t_1.greaterOrEqualThan=" + DEFAULT_COMMS_RANGE_T_1);

        // Get all the simulationRunList where comms_range_t_1 greater than or equals to UPDATED_COMMS_RANGE_T_1
        defaultSimulationRunShouldNotBeFound("comms_range_t_1.greaterOrEqualThan=" + UPDATED_COMMS_RANGE_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_1IsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_1 less than or equals to DEFAULT_COMMS_RANGE_T_1
        defaultSimulationRunShouldNotBeFound("comms_range_t_1.lessThan=" + DEFAULT_COMMS_RANGE_T_1);

        // Get all the simulationRunList where comms_range_t_1 less than or equals to UPDATED_COMMS_RANGE_T_1
        defaultSimulationRunShouldBeFound("comms_range_t_1.lessThan=" + UPDATED_COMMS_RANGE_T_1);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByFov_az_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where fov_az_t_1 equals to DEFAULT_FOV_AZ_T_1
        defaultSimulationRunShouldBeFound("fov_az_t_1.equals=" + DEFAULT_FOV_AZ_T_1);

        // Get all the simulationRunList where fov_az_t_1 equals to UPDATED_FOV_AZ_T_1
        defaultSimulationRunShouldNotBeFound("fov_az_t_1.equals=" + UPDATED_FOV_AZ_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByFov_az_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where fov_az_t_1 in DEFAULT_FOV_AZ_T_1 or UPDATED_FOV_AZ_T_1
        defaultSimulationRunShouldBeFound("fov_az_t_1.in=" + DEFAULT_FOV_AZ_T_1 + "," + UPDATED_FOV_AZ_T_1);

        // Get all the simulationRunList where fov_az_t_1 equals to UPDATED_FOV_AZ_T_1
        defaultSimulationRunShouldNotBeFound("fov_az_t_1.in=" + UPDATED_FOV_AZ_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByFov_az_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where fov_az_t_1 is not null
        defaultSimulationRunShouldBeFound("fov_az_t_1.specified=true");

        // Get all the simulationRunList where fov_az_t_1 is null
        defaultSimulationRunShouldNotBeFound("fov_az_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByFov_az_t_1IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where fov_az_t_1 greater than or equals to DEFAULT_FOV_AZ_T_1
        defaultSimulationRunShouldBeFound("fov_az_t_1.greaterOrEqualThan=" + DEFAULT_FOV_AZ_T_1);

        // Get all the simulationRunList where fov_az_t_1 greater than or equals to UPDATED_FOV_AZ_T_1
        defaultSimulationRunShouldNotBeFound("fov_az_t_1.greaterOrEqualThan=" + UPDATED_FOV_AZ_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByFov_az_t_1IsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where fov_az_t_1 less than or equals to DEFAULT_FOV_AZ_T_1
        defaultSimulationRunShouldNotBeFound("fov_az_t_1.lessThan=" + DEFAULT_FOV_AZ_T_1);

        // Get all the simulationRunList where fov_az_t_1 less than or equals to UPDATED_FOV_AZ_T_1
        defaultSimulationRunShouldBeFound("fov_az_t_1.lessThan=" + UPDATED_FOV_AZ_T_1);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByFov_el_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where fov_el_t_1 equals to DEFAULT_FOV_EL_T_1
        defaultSimulationRunShouldBeFound("fov_el_t_1.equals=" + DEFAULT_FOV_EL_T_1);

        // Get all the simulationRunList where fov_el_t_1 equals to UPDATED_FOV_EL_T_1
        defaultSimulationRunShouldNotBeFound("fov_el_t_1.equals=" + UPDATED_FOV_EL_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByFov_el_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where fov_el_t_1 in DEFAULT_FOV_EL_T_1 or UPDATED_FOV_EL_T_1
        defaultSimulationRunShouldBeFound("fov_el_t_1.in=" + DEFAULT_FOV_EL_T_1 + "," + UPDATED_FOV_EL_T_1);

        // Get all the simulationRunList where fov_el_t_1 equals to UPDATED_FOV_EL_T_1
        defaultSimulationRunShouldNotBeFound("fov_el_t_1.in=" + UPDATED_FOV_EL_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByFov_el_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where fov_el_t_1 is not null
        defaultSimulationRunShouldBeFound("fov_el_t_1.specified=true");

        // Get all the simulationRunList where fov_el_t_1 is null
        defaultSimulationRunShouldNotBeFound("fov_el_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByFov_el_t_1IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where fov_el_t_1 greater than or equals to DEFAULT_FOV_EL_T_1
        defaultSimulationRunShouldBeFound("fov_el_t_1.greaterOrEqualThan=" + DEFAULT_FOV_EL_T_1);

        // Get all the simulationRunList where fov_el_t_1 greater than or equals to UPDATED_FOV_EL_T_1
        defaultSimulationRunShouldNotBeFound("fov_el_t_1.greaterOrEqualThan=" + UPDATED_FOV_EL_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByFov_el_t_1IsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where fov_el_t_1 less than or equals to DEFAULT_FOV_EL_T_1
        defaultSimulationRunShouldNotBeFound("fov_el_t_1.lessThan=" + DEFAULT_FOV_EL_T_1);

        // Get all the simulationRunList where fov_el_t_1 less than or equals to UPDATED_FOV_EL_T_1
        defaultSimulationRunShouldBeFound("fov_el_t_1.lessThan=" + UPDATED_FOV_EL_T_1);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByGoal_weight_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where goal_weight_t_1 equals to DEFAULT_GOAL_WEIGHT_T_1
        defaultSimulationRunShouldBeFound("goal_weight_t_1.equals=" + DEFAULT_GOAL_WEIGHT_T_1);

        // Get all the simulationRunList where goal_weight_t_1 equals to UPDATED_GOAL_WEIGHT_T_1
        defaultSimulationRunShouldNotBeFound("goal_weight_t_1.equals=" + UPDATED_GOAL_WEIGHT_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByGoal_weight_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where goal_weight_t_1 in DEFAULT_GOAL_WEIGHT_T_1 or UPDATED_GOAL_WEIGHT_T_1
        defaultSimulationRunShouldBeFound("goal_weight_t_1.in=" + DEFAULT_GOAL_WEIGHT_T_1 + "," + UPDATED_GOAL_WEIGHT_T_1);

        // Get all the simulationRunList where goal_weight_t_1 equals to UPDATED_GOAL_WEIGHT_T_1
        defaultSimulationRunShouldNotBeFound("goal_weight_t_1.in=" + UPDATED_GOAL_WEIGHT_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByGoal_weight_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where goal_weight_t_1 is not null
        defaultSimulationRunShouldBeFound("goal_weight_t_1.specified=true");

        // Get all the simulationRunList where goal_weight_t_1 is null
        defaultSimulationRunShouldNotBeFound("goal_weight_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_1 equals to DEFAULT_MAX_PRED_SPEED_T_1
        defaultSimulationRunShouldBeFound("max_pred_speed_t_1.equals=" + DEFAULT_MAX_PRED_SPEED_T_1);

        // Get all the simulationRunList where max_pred_speed_t_1 equals to UPDATED_MAX_PRED_SPEED_T_1
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_1.equals=" + UPDATED_MAX_PRED_SPEED_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_1 in DEFAULT_MAX_PRED_SPEED_T_1 or UPDATED_MAX_PRED_SPEED_T_1
        defaultSimulationRunShouldBeFound("max_pred_speed_t_1.in=" + DEFAULT_MAX_PRED_SPEED_T_1 + "," + UPDATED_MAX_PRED_SPEED_T_1);

        // Get all the simulationRunList where max_pred_speed_t_1 equals to UPDATED_MAX_PRED_SPEED_T_1
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_1.in=" + UPDATED_MAX_PRED_SPEED_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_1 is not null
        defaultSimulationRunShouldBeFound("max_pred_speed_t_1.specified=true");

        // Get all the simulationRunList where max_pred_speed_t_1 is null
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_1IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_1 greater than or equals to DEFAULT_MAX_PRED_SPEED_T_1
        defaultSimulationRunShouldBeFound("max_pred_speed_t_1.greaterOrEqualThan=" + DEFAULT_MAX_PRED_SPEED_T_1);

        // Get all the simulationRunList where max_pred_speed_t_1 greater than or equals to UPDATED_MAX_PRED_SPEED_T_1
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_1.greaterOrEqualThan=" + UPDATED_MAX_PRED_SPEED_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_1IsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_1 less than or equals to DEFAULT_MAX_PRED_SPEED_T_1
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_1.lessThan=" + DEFAULT_MAX_PRED_SPEED_T_1);

        // Get all the simulationRunList where max_pred_speed_t_1 less than or equals to UPDATED_MAX_PRED_SPEED_T_1
        defaultSimulationRunShouldBeFound("max_pred_speed_t_1.lessThan=" + UPDATED_MAX_PRED_SPEED_T_1);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_1 equals to DEFAULT_MAX_SPEED_T_1
        defaultSimulationRunShouldBeFound("max_speed_t_1.equals=" + DEFAULT_MAX_SPEED_T_1);

        // Get all the simulationRunList where max_speed_t_1 equals to UPDATED_MAX_SPEED_T_1
        defaultSimulationRunShouldNotBeFound("max_speed_t_1.equals=" + UPDATED_MAX_SPEED_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_1 in DEFAULT_MAX_SPEED_T_1 or UPDATED_MAX_SPEED_T_1
        defaultSimulationRunShouldBeFound("max_speed_t_1.in=" + DEFAULT_MAX_SPEED_T_1 + "," + UPDATED_MAX_SPEED_T_1);

        // Get all the simulationRunList where max_speed_t_1 equals to UPDATED_MAX_SPEED_T_1
        defaultSimulationRunShouldNotBeFound("max_speed_t_1.in=" + UPDATED_MAX_SPEED_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_1 is not null
        defaultSimulationRunShouldBeFound("max_speed_t_1.specified=true");

        // Get all the simulationRunList where max_speed_t_1 is null
        defaultSimulationRunShouldNotBeFound("max_speed_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_1IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_1 greater than or equals to DEFAULT_MAX_SPEED_T_1
        defaultSimulationRunShouldBeFound("max_speed_t_1.greaterOrEqualThan=" + DEFAULT_MAX_SPEED_T_1);

        // Get all the simulationRunList where max_speed_t_1 greater than or equals to UPDATED_MAX_SPEED_T_1
        defaultSimulationRunShouldNotBeFound("max_speed_t_1.greaterOrEqualThan=" + UPDATED_MAX_SPEED_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_1IsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_1 less than or equals to DEFAULT_MAX_SPEED_T_1
        defaultSimulationRunShouldNotBeFound("max_speed_t_1.lessThan=" + DEFAULT_MAX_SPEED_T_1);

        // Get all the simulationRunList where max_speed_t_1 less than or equals to UPDATED_MAX_SPEED_T_1
        defaultSimulationRunShouldBeFound("max_speed_t_1.lessThan=" + UPDATED_MAX_SPEED_T_1);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsBySphere_of_influence_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where sphere_of_influence_t_1 equals to DEFAULT_SPHERE_OF_INFLUENCE_T_1
        defaultSimulationRunShouldBeFound("sphere_of_influence_t_1.equals=" + DEFAULT_SPHERE_OF_INFLUENCE_T_1);

        // Get all the simulationRunList where sphere_of_influence_t_1 equals to UPDATED_SPHERE_OF_INFLUENCE_T_1
        defaultSimulationRunShouldNotBeFound("sphere_of_influence_t_1.equals=" + UPDATED_SPHERE_OF_INFLUENCE_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsBySphere_of_influence_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where sphere_of_influence_t_1 in DEFAULT_SPHERE_OF_INFLUENCE_T_1 or UPDATED_SPHERE_OF_INFLUENCE_T_1
        defaultSimulationRunShouldBeFound("sphere_of_influence_t_1.in=" + DEFAULT_SPHERE_OF_INFLUENCE_T_1 + "," + UPDATED_SPHERE_OF_INFLUENCE_T_1);

        // Get all the simulationRunList where sphere_of_influence_t_1 equals to UPDATED_SPHERE_OF_INFLUENCE_T_1
        defaultSimulationRunShouldNotBeFound("sphere_of_influence_t_1.in=" + UPDATED_SPHERE_OF_INFLUENCE_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsBySphere_of_influence_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where sphere_of_influence_t_1 is not null
        defaultSimulationRunShouldBeFound("sphere_of_influence_t_1.specified=true");

        // Get all the simulationRunList where sphere_of_influence_t_1 is null
        defaultSimulationRunShouldNotBeFound("sphere_of_influence_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsBySphere_of_influence_t_1IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where sphere_of_influence_t_1 greater than or equals to DEFAULT_SPHERE_OF_INFLUENCE_T_1
        defaultSimulationRunShouldBeFound("sphere_of_influence_t_1.greaterOrEqualThan=" + DEFAULT_SPHERE_OF_INFLUENCE_T_1);

        // Get all the simulationRunList where sphere_of_influence_t_1 greater than or equals to UPDATED_SPHERE_OF_INFLUENCE_T_1
        defaultSimulationRunShouldNotBeFound("sphere_of_influence_t_1.greaterOrEqualThan=" + UPDATED_SPHERE_OF_INFLUENCE_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsBySphere_of_influence_t_1IsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where sphere_of_influence_t_1 less than or equals to DEFAULT_SPHERE_OF_INFLUENCE_T_1
        defaultSimulationRunShouldNotBeFound("sphere_of_influence_t_1.lessThan=" + DEFAULT_SPHERE_OF_INFLUENCE_T_1);

        // Get all the simulationRunList where sphere_of_influence_t_1 less than or equals to UPDATED_SPHERE_OF_INFLUENCE_T_1
        defaultSimulationRunShouldBeFound("sphere_of_influence_t_1.lessThan=" + UPDATED_SPHERE_OF_INFLUENCE_T_1);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByMotion_model_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where motion_model_t_1 equals to DEFAULT_MOTION_MODEL_T_1
        defaultSimulationRunShouldBeFound("motion_model_t_1.equals=" + DEFAULT_MOTION_MODEL_T_1);

        // Get all the simulationRunList where motion_model_t_1 equals to UPDATED_MOTION_MODEL_T_1
        defaultSimulationRunShouldNotBeFound("motion_model_t_1.equals=" + UPDATED_MOTION_MODEL_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMotion_model_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where motion_model_t_1 in DEFAULT_MOTION_MODEL_T_1 or UPDATED_MOTION_MODEL_T_1
        defaultSimulationRunShouldBeFound("motion_model_t_1.in=" + DEFAULT_MOTION_MODEL_T_1 + "," + UPDATED_MOTION_MODEL_T_1);

        // Get all the simulationRunList where motion_model_t_1 equals to UPDATED_MOTION_MODEL_T_1
        defaultSimulationRunShouldNotBeFound("motion_model_t_1.in=" + UPDATED_MOTION_MODEL_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMotion_model_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where motion_model_t_1 is not null
        defaultSimulationRunShouldBeFound("motion_model_t_1.specified=true");

        // Get all the simulationRunList where motion_model_t_1 is null
        defaultSimulationRunShouldNotBeFound("motion_model_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByVel_max_t_1IsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where vel_max_t_1 equals to DEFAULT_VEL_MAX_T_1
        defaultSimulationRunShouldBeFound("vel_max_t_1.equals=" + DEFAULT_VEL_MAX_T_1);

        // Get all the simulationRunList where vel_max_t_1 equals to UPDATED_VEL_MAX_T_1
        defaultSimulationRunShouldNotBeFound("vel_max_t_1.equals=" + UPDATED_VEL_MAX_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByVel_max_t_1IsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where vel_max_t_1 in DEFAULT_VEL_MAX_T_1 or UPDATED_VEL_MAX_T_1
        defaultSimulationRunShouldBeFound("vel_max_t_1.in=" + DEFAULT_VEL_MAX_T_1 + "," + UPDATED_VEL_MAX_T_1);

        // Get all the simulationRunList where vel_max_t_1 equals to UPDATED_VEL_MAX_T_1
        defaultSimulationRunShouldNotBeFound("vel_max_t_1.in=" + UPDATED_VEL_MAX_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByVel_max_t_1IsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where vel_max_t_1 is not null
        defaultSimulationRunShouldBeFound("vel_max_t_1.specified=true");

        // Get all the simulationRunList where vel_max_t_1 is null
        defaultSimulationRunShouldNotBeFound("vel_max_t_1.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByVel_max_t_1IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where vel_max_t_1 greater than or equals to DEFAULT_VEL_MAX_T_1
        defaultSimulationRunShouldBeFound("vel_max_t_1.greaterOrEqualThan=" + DEFAULT_VEL_MAX_T_1);

        // Get all the simulationRunList where vel_max_t_1 greater than or equals to UPDATED_VEL_MAX_T_1
        defaultSimulationRunShouldNotBeFound("vel_max_t_1.greaterOrEqualThan=" + UPDATED_VEL_MAX_T_1);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByVel_max_t_1IsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where vel_max_t_1 less than or equals to DEFAULT_VEL_MAX_T_1
        defaultSimulationRunShouldNotBeFound("vel_max_t_1.lessThan=" + DEFAULT_VEL_MAX_T_1);

        // Get all the simulationRunList where vel_max_t_1 less than or equals to UPDATED_VEL_MAX_T_1
        defaultSimulationRunShouldBeFound("vel_max_t_1.lessThan=" + UPDATED_VEL_MAX_T_1);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByMotion_model_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where motion_model_predator equals to DEFAULT_MOTION_MODEL_PREDATOR
        defaultSimulationRunShouldBeFound("motion_model_predator.equals=" + DEFAULT_MOTION_MODEL_PREDATOR);

        // Get all the simulationRunList where motion_model_predator equals to UPDATED_MOTION_MODEL_PREDATOR
        defaultSimulationRunShouldNotBeFound("motion_model_predator.equals=" + UPDATED_MOTION_MODEL_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMotion_model_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where motion_model_predator in DEFAULT_MOTION_MODEL_PREDATOR or UPDATED_MOTION_MODEL_PREDATOR
        defaultSimulationRunShouldBeFound("motion_model_predator.in=" + DEFAULT_MOTION_MODEL_PREDATOR + "," + UPDATED_MOTION_MODEL_PREDATOR);

        // Get all the simulationRunList where motion_model_predator equals to UPDATED_MOTION_MODEL_PREDATOR
        defaultSimulationRunShouldNotBeFound("motion_model_predator.in=" + UPDATED_MOTION_MODEL_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMotion_model_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where motion_model_predator is not null
        defaultSimulationRunShouldBeFound("motion_model_predator.specified=true");

        // Get all the simulationRunList where motion_model_predator is null
        defaultSimulationRunShouldNotBeFound("motion_model_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByPitch_rate_max_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where pitch_rate_max_predator equals to DEFAULT_PITCH_RATE_MAX_PREDATOR
        defaultSimulationRunShouldBeFound("pitch_rate_max_predator.equals=" + DEFAULT_PITCH_RATE_MAX_PREDATOR);

        // Get all the simulationRunList where pitch_rate_max_predator equals to UPDATED_PITCH_RATE_MAX_PREDATOR
        defaultSimulationRunShouldNotBeFound("pitch_rate_max_predator.equals=" + UPDATED_PITCH_RATE_MAX_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByPitch_rate_max_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where pitch_rate_max_predator in DEFAULT_PITCH_RATE_MAX_PREDATOR or UPDATED_PITCH_RATE_MAX_PREDATOR
        defaultSimulationRunShouldBeFound("pitch_rate_max_predator.in=" + DEFAULT_PITCH_RATE_MAX_PREDATOR + "," + UPDATED_PITCH_RATE_MAX_PREDATOR);

        // Get all the simulationRunList where pitch_rate_max_predator equals to UPDATED_PITCH_RATE_MAX_PREDATOR
        defaultSimulationRunShouldNotBeFound("pitch_rate_max_predator.in=" + UPDATED_PITCH_RATE_MAX_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByPitch_rate_max_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where pitch_rate_max_predator is not null
        defaultSimulationRunShouldBeFound("pitch_rate_max_predator.specified=true");

        // Get all the simulationRunList where pitch_rate_max_predator is null
        defaultSimulationRunShouldNotBeFound("pitch_rate_max_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTurn_rate_max_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where turn_rate_max_predator equals to DEFAULT_TURN_RATE_MAX_PREDATOR
        defaultSimulationRunShouldBeFound("turn_rate_max_predator.equals=" + DEFAULT_TURN_RATE_MAX_PREDATOR);

        // Get all the simulationRunList where turn_rate_max_predator equals to UPDATED_TURN_RATE_MAX_PREDATOR
        defaultSimulationRunShouldNotBeFound("turn_rate_max_predator.equals=" + UPDATED_TURN_RATE_MAX_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTurn_rate_max_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where turn_rate_max_predator in DEFAULT_TURN_RATE_MAX_PREDATOR or UPDATED_TURN_RATE_MAX_PREDATOR
        defaultSimulationRunShouldBeFound("turn_rate_max_predator.in=" + DEFAULT_TURN_RATE_MAX_PREDATOR + "," + UPDATED_TURN_RATE_MAX_PREDATOR);

        // Get all the simulationRunList where turn_rate_max_predator equals to UPDATED_TURN_RATE_MAX_PREDATOR
        defaultSimulationRunShouldNotBeFound("turn_rate_max_predator.in=" + UPDATED_TURN_RATE_MAX_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTurn_rate_max_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where turn_rate_max_predator is not null
        defaultSimulationRunShouldBeFound("turn_rate_max_predator.specified=true");

        // Get all the simulationRunList where turn_rate_max_predator is null
        defaultSimulationRunShouldNotBeFound("turn_rate_max_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTurn_rate_max_predatorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where turn_rate_max_predator greater than or equals to DEFAULT_TURN_RATE_MAX_PREDATOR
        defaultSimulationRunShouldBeFound("turn_rate_max_predator.greaterOrEqualThan=" + DEFAULT_TURN_RATE_MAX_PREDATOR);

        // Get all the simulationRunList where turn_rate_max_predator greater than or equals to UPDATED_TURN_RATE_MAX_PREDATOR
        defaultSimulationRunShouldNotBeFound("turn_rate_max_predator.greaterOrEqualThan=" + UPDATED_TURN_RATE_MAX_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByTurn_rate_max_predatorIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where turn_rate_max_predator less than or equals to DEFAULT_TURN_RATE_MAX_PREDATOR
        defaultSimulationRunShouldNotBeFound("turn_rate_max_predator.lessThan=" + DEFAULT_TURN_RATE_MAX_PREDATOR);

        // Get all the simulationRunList where turn_rate_max_predator less than or equals to UPDATED_TURN_RATE_MAX_PREDATOR
        defaultSimulationRunShouldBeFound("turn_rate_max_predator.lessThan=" + UPDATED_TURN_RATE_MAX_PREDATOR);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByVel_max_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where vel_max_predator equals to DEFAULT_VEL_MAX_PREDATOR
        defaultSimulationRunShouldBeFound("vel_max_predator.equals=" + DEFAULT_VEL_MAX_PREDATOR);

        // Get all the simulationRunList where vel_max_predator equals to UPDATED_VEL_MAX_PREDATOR
        defaultSimulationRunShouldNotBeFound("vel_max_predator.equals=" + UPDATED_VEL_MAX_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByVel_max_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where vel_max_predator in DEFAULT_VEL_MAX_PREDATOR or UPDATED_VEL_MAX_PREDATOR
        defaultSimulationRunShouldBeFound("vel_max_predator.in=" + DEFAULT_VEL_MAX_PREDATOR + "," + UPDATED_VEL_MAX_PREDATOR);

        // Get all the simulationRunList where vel_max_predator equals to UPDATED_VEL_MAX_PREDATOR
        defaultSimulationRunShouldNotBeFound("vel_max_predator.in=" + UPDATED_VEL_MAX_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByVel_max_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where vel_max_predator is not null
        defaultSimulationRunShouldBeFound("vel_max_predator.specified=true");

        // Get all the simulationRunList where vel_max_predator is null
        defaultSimulationRunShouldNotBeFound("vel_max_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByVel_max_predatorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where vel_max_predator greater than or equals to DEFAULT_VEL_MAX_PREDATOR
        defaultSimulationRunShouldBeFound("vel_max_predator.greaterOrEqualThan=" + DEFAULT_VEL_MAX_PREDATOR);

        // Get all the simulationRunList where vel_max_predator greater than or equals to UPDATED_VEL_MAX_PREDATOR
        defaultSimulationRunShouldNotBeFound("vel_max_predator.greaterOrEqualThan=" + UPDATED_VEL_MAX_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByVel_max_predatorIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where vel_max_predator less than or equals to DEFAULT_VEL_MAX_PREDATOR
        defaultSimulationRunShouldNotBeFound("vel_max_predator.lessThan=" + DEFAULT_VEL_MAX_PREDATOR);

        // Get all the simulationRunList where vel_max_predator less than or equals to UPDATED_VEL_MAX_PREDATOR
        defaultSimulationRunShouldBeFound("vel_max_predator.lessThan=" + UPDATED_VEL_MAX_PREDATOR);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByAlign_weight_t_2_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where align_weight_t_2_predator equals to DEFAULT_ALIGN_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("align_weight_t_2_predator.equals=" + DEFAULT_ALIGN_WEIGHT_T_2_PREDATOR);

        // Get all the simulationRunList where align_weight_t_2_predator equals to UPDATED_ALIGN_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("align_weight_t_2_predator.equals=" + UPDATED_ALIGN_WEIGHT_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAlign_weight_t_2_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where align_weight_t_2_predator in DEFAULT_ALIGN_WEIGHT_T_2_PREDATOR or UPDATED_ALIGN_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("align_weight_t_2_predator.in=" + DEFAULT_ALIGN_WEIGHT_T_2_PREDATOR + "," + UPDATED_ALIGN_WEIGHT_T_2_PREDATOR);

        // Get all the simulationRunList where align_weight_t_2_predator equals to UPDATED_ALIGN_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("align_weight_t_2_predator.in=" + UPDATED_ALIGN_WEIGHT_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAlign_weight_t_2_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where align_weight_t_2_predator is not null
        defaultSimulationRunShouldBeFound("align_weight_t_2_predator.specified=true");

        // Get all the simulationRunList where align_weight_t_2_predator is null
        defaultSimulationRunShouldNotBeFound("align_weight_t_2_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAllow_prey_switching_t_2_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where allow_prey_switching_t_2_predator equals to DEFAULT_ALLOW_PREY_SWITCHING_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("allow_prey_switching_t_2_predator.equals=" + DEFAULT_ALLOW_PREY_SWITCHING_T_2_PREDATOR);

        // Get all the simulationRunList where allow_prey_switching_t_2_predator equals to UPDATED_ALLOW_PREY_SWITCHING_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("allow_prey_switching_t_2_predator.equals=" + UPDATED_ALLOW_PREY_SWITCHING_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAllow_prey_switching_t_2_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where allow_prey_switching_t_2_predator in DEFAULT_ALLOW_PREY_SWITCHING_T_2_PREDATOR or UPDATED_ALLOW_PREY_SWITCHING_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("allow_prey_switching_t_2_predator.in=" + DEFAULT_ALLOW_PREY_SWITCHING_T_2_PREDATOR + "," + UPDATED_ALLOW_PREY_SWITCHING_T_2_PREDATOR);

        // Get all the simulationRunList where allow_prey_switching_t_2_predator equals to UPDATED_ALLOW_PREY_SWITCHING_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("allow_prey_switching_t_2_predator.in=" + UPDATED_ALLOW_PREY_SWITCHING_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAllow_prey_switching_t_2_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where allow_prey_switching_t_2_predator is not null
        defaultSimulationRunShouldBeFound("allow_prey_switching_t_2_predator.specified=true");

        // Get all the simulationRunList where allow_prey_switching_t_2_predator is null
        defaultSimulationRunShouldNotBeFound("allow_prey_switching_t_2_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAutonomy_t_2_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where autonomy_t_2_predator equals to DEFAULT_AUTONOMY_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("autonomy_t_2_predator.equals=" + DEFAULT_AUTONOMY_T_2_PREDATOR);

        // Get all the simulationRunList where autonomy_t_2_predator equals to UPDATED_AUTONOMY_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("autonomy_t_2_predator.equals=" + UPDATED_AUTONOMY_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAutonomy_t_2_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where autonomy_t_2_predator in DEFAULT_AUTONOMY_T_2_PREDATOR or UPDATED_AUTONOMY_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("autonomy_t_2_predator.in=" + DEFAULT_AUTONOMY_T_2_PREDATOR + "," + UPDATED_AUTONOMY_T_2_PREDATOR);

        // Get all the simulationRunList where autonomy_t_2_predator equals to UPDATED_AUTONOMY_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("autonomy_t_2_predator.in=" + UPDATED_AUTONOMY_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAutonomy_t_2_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where autonomy_t_2_predator is not null
        defaultSimulationRunShouldBeFound("autonomy_t_2_predator.specified=true");

        // Get all the simulationRunList where autonomy_t_2_predator is null
        defaultSimulationRunShouldNotBeFound("autonomy_t_2_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_nonteam_weight_t_2_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_nonteam_weight_t_2_predator equals to DEFAULT_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("avoid_nonteam_weight_t_2_predator.equals=" + DEFAULT_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR);

        // Get all the simulationRunList where avoid_nonteam_weight_t_2_predator equals to UPDATED_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("avoid_nonteam_weight_t_2_predator.equals=" + UPDATED_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_nonteam_weight_t_2_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_nonteam_weight_t_2_predator in DEFAULT_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR or UPDATED_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("avoid_nonteam_weight_t_2_predator.in=" + DEFAULT_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR + "," + UPDATED_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR);

        // Get all the simulationRunList where avoid_nonteam_weight_t_2_predator equals to UPDATED_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("avoid_nonteam_weight_t_2_predator.in=" + UPDATED_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_nonteam_weight_t_2_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_nonteam_weight_t_2_predator is not null
        defaultSimulationRunShouldBeFound("avoid_nonteam_weight_t_2_predator.specified=true");

        // Get all the simulationRunList where avoid_nonteam_weight_t_2_predator is null
        defaultSimulationRunShouldNotBeFound("avoid_nonteam_weight_t_2_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_team_weight_t_2_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_team_weight_t_2_predator equals to DEFAULT_AVOID_TEAM_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("avoid_team_weight_t_2_predator.equals=" + DEFAULT_AVOID_TEAM_WEIGHT_T_2_PREDATOR);

        // Get all the simulationRunList where avoid_team_weight_t_2_predator equals to UPDATED_AVOID_TEAM_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("avoid_team_weight_t_2_predator.equals=" + UPDATED_AVOID_TEAM_WEIGHT_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_team_weight_t_2_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_team_weight_t_2_predator in DEFAULT_AVOID_TEAM_WEIGHT_T_2_PREDATOR or UPDATED_AVOID_TEAM_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("avoid_team_weight_t_2_predator.in=" + DEFAULT_AVOID_TEAM_WEIGHT_T_2_PREDATOR + "," + UPDATED_AVOID_TEAM_WEIGHT_T_2_PREDATOR);

        // Get all the simulationRunList where avoid_team_weight_t_2_predator equals to UPDATED_AVOID_TEAM_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("avoid_team_weight_t_2_predator.in=" + UPDATED_AVOID_TEAM_WEIGHT_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_team_weight_t_2_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_team_weight_t_2_predator is not null
        defaultSimulationRunShouldBeFound("avoid_team_weight_t_2_predator.specified=true");

        // Get all the simulationRunList where avoid_team_weight_t_2_predator is null
        defaultSimulationRunShouldNotBeFound("avoid_team_weight_t_2_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCapture_range_t_2_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where capture_range_t_2_predator equals to DEFAULT_CAPTURE_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("capture_range_t_2_predator.equals=" + DEFAULT_CAPTURE_RANGE_T_2_PREDATOR);

        // Get all the simulationRunList where capture_range_t_2_predator equals to UPDATED_CAPTURE_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("capture_range_t_2_predator.equals=" + UPDATED_CAPTURE_RANGE_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCapture_range_t_2_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where capture_range_t_2_predator in DEFAULT_CAPTURE_RANGE_T_2_PREDATOR or UPDATED_CAPTURE_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("capture_range_t_2_predator.in=" + DEFAULT_CAPTURE_RANGE_T_2_PREDATOR + "," + UPDATED_CAPTURE_RANGE_T_2_PREDATOR);

        // Get all the simulationRunList where capture_range_t_2_predator equals to UPDATED_CAPTURE_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("capture_range_t_2_predator.in=" + UPDATED_CAPTURE_RANGE_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCapture_range_t_2_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where capture_range_t_2_predator is not null
        defaultSimulationRunShouldBeFound("capture_range_t_2_predator.specified=true");

        // Get all the simulationRunList where capture_range_t_2_predator is null
        defaultSimulationRunShouldNotBeFound("capture_range_t_2_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCapture_range_t_2_predatorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where capture_range_t_2_predator greater than or equals to DEFAULT_CAPTURE_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("capture_range_t_2_predator.greaterOrEqualThan=" + DEFAULT_CAPTURE_RANGE_T_2_PREDATOR);

        // Get all the simulationRunList where capture_range_t_2_predator greater than or equals to UPDATED_CAPTURE_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("capture_range_t_2_predator.greaterOrEqualThan=" + UPDATED_CAPTURE_RANGE_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCapture_range_t_2_predatorIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where capture_range_t_2_predator less than or equals to DEFAULT_CAPTURE_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("capture_range_t_2_predator.lessThan=" + DEFAULT_CAPTURE_RANGE_T_2_PREDATOR);

        // Get all the simulationRunList where capture_range_t_2_predator less than or equals to UPDATED_CAPTURE_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("capture_range_t_2_predator.lessThan=" + UPDATED_CAPTURE_RANGE_T_2_PREDATOR);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByCentroid_weight_t_2_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where centroid_weight_t_2_predator equals to DEFAULT_CENTROID_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("centroid_weight_t_2_predator.equals=" + DEFAULT_CENTROID_WEIGHT_T_2_PREDATOR);

        // Get all the simulationRunList where centroid_weight_t_2_predator equals to UPDATED_CENTROID_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("centroid_weight_t_2_predator.equals=" + UPDATED_CENTROID_WEIGHT_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCentroid_weight_t_2_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where centroid_weight_t_2_predator in DEFAULT_CENTROID_WEIGHT_T_2_PREDATOR or UPDATED_CENTROID_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("centroid_weight_t_2_predator.in=" + DEFAULT_CENTROID_WEIGHT_T_2_PREDATOR + "," + UPDATED_CENTROID_WEIGHT_T_2_PREDATOR);

        // Get all the simulationRunList where centroid_weight_t_2_predator equals to UPDATED_CENTROID_WEIGHT_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("centroid_weight_t_2_predator.in=" + UPDATED_CENTROID_WEIGHT_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCentroid_weight_t_2_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where centroid_weight_t_2_predator is not null
        defaultSimulationRunShouldBeFound("centroid_weight_t_2_predator.specified=true");

        // Get all the simulationRunList where centroid_weight_t_2_predator is null
        defaultSimulationRunShouldNotBeFound("centroid_weight_t_2_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_2_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_2_predator equals to DEFAULT_COMMS_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("comms_range_t_2_predator.equals=" + DEFAULT_COMMS_RANGE_T_2_PREDATOR);

        // Get all the simulationRunList where comms_range_t_2_predator equals to UPDATED_COMMS_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("comms_range_t_2_predator.equals=" + UPDATED_COMMS_RANGE_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_2_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_2_predator in DEFAULT_COMMS_RANGE_T_2_PREDATOR or UPDATED_COMMS_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("comms_range_t_2_predator.in=" + DEFAULT_COMMS_RANGE_T_2_PREDATOR + "," + UPDATED_COMMS_RANGE_T_2_PREDATOR);

        // Get all the simulationRunList where comms_range_t_2_predator equals to UPDATED_COMMS_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("comms_range_t_2_predator.in=" + UPDATED_COMMS_RANGE_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_2_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_2_predator is not null
        defaultSimulationRunShouldBeFound("comms_range_t_2_predator.specified=true");

        // Get all the simulationRunList where comms_range_t_2_predator is null
        defaultSimulationRunShouldNotBeFound("comms_range_t_2_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_2_predatorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_2_predator greater than or equals to DEFAULT_COMMS_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("comms_range_t_2_predator.greaterOrEqualThan=" + DEFAULT_COMMS_RANGE_T_2_PREDATOR);

        // Get all the simulationRunList where comms_range_t_2_predator greater than or equals to UPDATED_COMMS_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("comms_range_t_2_predator.greaterOrEqualThan=" + UPDATED_COMMS_RANGE_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_2_predatorIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_2_predator less than or equals to DEFAULT_COMMS_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("comms_range_t_2_predator.lessThan=" + DEFAULT_COMMS_RANGE_T_2_PREDATOR);

        // Get all the simulationRunList where comms_range_t_2_predator less than or equals to UPDATED_COMMS_RANGE_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("comms_range_t_2_predator.lessThan=" + UPDATED_COMMS_RANGE_T_2_PREDATOR);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_2_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_2_predator equals to DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("max_pred_speed_t_2_predator.equals=" + DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR);

        // Get all the simulationRunList where max_pred_speed_t_2_predator equals to UPDATED_MAX_PRED_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_2_predator.equals=" + UPDATED_MAX_PRED_SPEED_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_2_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_2_predator in DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR or UPDATED_MAX_PRED_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("max_pred_speed_t_2_predator.in=" + DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR + "," + UPDATED_MAX_PRED_SPEED_T_2_PREDATOR);

        // Get all the simulationRunList where max_pred_speed_t_2_predator equals to UPDATED_MAX_PRED_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_2_predator.in=" + UPDATED_MAX_PRED_SPEED_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_2_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_2_predator is not null
        defaultSimulationRunShouldBeFound("max_pred_speed_t_2_predator.specified=true");

        // Get all the simulationRunList where max_pred_speed_t_2_predator is null
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_2_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_2_predatorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_2_predator greater than or equals to DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("max_pred_speed_t_2_predator.greaterOrEqualThan=" + DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR);

        // Get all the simulationRunList where max_pred_speed_t_2_predator greater than or equals to UPDATED_MAX_PRED_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_2_predator.greaterOrEqualThan=" + UPDATED_MAX_PRED_SPEED_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_2_predatorIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_2_predator less than or equals to DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_2_predator.lessThan=" + DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR);

        // Get all the simulationRunList where max_pred_speed_t_2_predator less than or equals to UPDATED_MAX_PRED_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("max_pred_speed_t_2_predator.lessThan=" + UPDATED_MAX_PRED_SPEED_T_2_PREDATOR);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_2_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_2_predator equals to DEFAULT_MAX_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("max_speed_t_2_predator.equals=" + DEFAULT_MAX_SPEED_T_2_PREDATOR);

        // Get all the simulationRunList where max_speed_t_2_predator equals to UPDATED_MAX_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_speed_t_2_predator.equals=" + UPDATED_MAX_SPEED_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_2_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_2_predator in DEFAULT_MAX_SPEED_T_2_PREDATOR or UPDATED_MAX_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("max_speed_t_2_predator.in=" + DEFAULT_MAX_SPEED_T_2_PREDATOR + "," + UPDATED_MAX_SPEED_T_2_PREDATOR);

        // Get all the simulationRunList where max_speed_t_2_predator equals to UPDATED_MAX_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_speed_t_2_predator.in=" + UPDATED_MAX_SPEED_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_2_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_2_predator is not null
        defaultSimulationRunShouldBeFound("max_speed_t_2_predator.specified=true");

        // Get all the simulationRunList where max_speed_t_2_predator is null
        defaultSimulationRunShouldNotBeFound("max_speed_t_2_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_2_predatorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_2_predator greater than or equals to DEFAULT_MAX_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("max_speed_t_2_predator.greaterOrEqualThan=" + DEFAULT_MAX_SPEED_T_2_PREDATOR);

        // Get all the simulationRunList where max_speed_t_2_predator greater than or equals to UPDATED_MAX_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_speed_t_2_predator.greaterOrEqualThan=" + UPDATED_MAX_SPEED_T_2_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_2_predatorIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_2_predator less than or equals to DEFAULT_MAX_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_speed_t_2_predator.lessThan=" + DEFAULT_MAX_SPEED_T_2_PREDATOR);

        // Get all the simulationRunList where max_speed_t_2_predator less than or equals to UPDATED_MAX_SPEED_T_2_PREDATOR
        defaultSimulationRunShouldBeFound("max_speed_t_2_predator.lessThan=" + UPDATED_MAX_SPEED_T_2_PREDATOR);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByAlign_weight_t_3_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where align_weight_t_3_predator equals to DEFAULT_ALIGN_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("align_weight_t_3_predator.equals=" + DEFAULT_ALIGN_WEIGHT_T_3_PREDATOR);

        // Get all the simulationRunList where align_weight_t_3_predator equals to UPDATED_ALIGN_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("align_weight_t_3_predator.equals=" + UPDATED_ALIGN_WEIGHT_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAlign_weight_t_3_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where align_weight_t_3_predator in DEFAULT_ALIGN_WEIGHT_T_3_PREDATOR or UPDATED_ALIGN_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("align_weight_t_3_predator.in=" + DEFAULT_ALIGN_WEIGHT_T_3_PREDATOR + "," + UPDATED_ALIGN_WEIGHT_T_3_PREDATOR);

        // Get all the simulationRunList where align_weight_t_3_predator equals to UPDATED_ALIGN_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("align_weight_t_3_predator.in=" + UPDATED_ALIGN_WEIGHT_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAlign_weight_t_3_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where align_weight_t_3_predator is not null
        defaultSimulationRunShouldBeFound("align_weight_t_3_predator.specified=true");

        // Get all the simulationRunList where align_weight_t_3_predator is null
        defaultSimulationRunShouldNotBeFound("align_weight_t_3_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAllow_prey_switching_t_3_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where allow_prey_switching_t_3_predator equals to DEFAULT_ALLOW_PREY_SWITCHING_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("allow_prey_switching_t_3_predator.equals=" + DEFAULT_ALLOW_PREY_SWITCHING_T_3_PREDATOR);

        // Get all the simulationRunList where allow_prey_switching_t_3_predator equals to UPDATED_ALLOW_PREY_SWITCHING_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("allow_prey_switching_t_3_predator.equals=" + UPDATED_ALLOW_PREY_SWITCHING_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAllow_prey_switching_t_3_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where allow_prey_switching_t_3_predator in DEFAULT_ALLOW_PREY_SWITCHING_T_3_PREDATOR or UPDATED_ALLOW_PREY_SWITCHING_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("allow_prey_switching_t_3_predator.in=" + DEFAULT_ALLOW_PREY_SWITCHING_T_3_PREDATOR + "," + UPDATED_ALLOW_PREY_SWITCHING_T_3_PREDATOR);

        // Get all the simulationRunList where allow_prey_switching_t_3_predator equals to UPDATED_ALLOW_PREY_SWITCHING_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("allow_prey_switching_t_3_predator.in=" + UPDATED_ALLOW_PREY_SWITCHING_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAllow_prey_switching_t_3_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where allow_prey_switching_t_3_predator is not null
        defaultSimulationRunShouldBeFound("allow_prey_switching_t_3_predator.specified=true");

        // Get all the simulationRunList where allow_prey_switching_t_3_predator is null
        defaultSimulationRunShouldNotBeFound("allow_prey_switching_t_3_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAutonomy_t_3_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where autonomy_t_3_predator equals to DEFAULT_AUTONOMY_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("autonomy_t_3_predator.equals=" + DEFAULT_AUTONOMY_T_3_PREDATOR);

        // Get all the simulationRunList where autonomy_t_3_predator equals to UPDATED_AUTONOMY_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("autonomy_t_3_predator.equals=" + UPDATED_AUTONOMY_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAutonomy_t_3_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where autonomy_t_3_predator in DEFAULT_AUTONOMY_T_3_PREDATOR or UPDATED_AUTONOMY_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("autonomy_t_3_predator.in=" + DEFAULT_AUTONOMY_T_3_PREDATOR + "," + UPDATED_AUTONOMY_T_3_PREDATOR);

        // Get all the simulationRunList where autonomy_t_3_predator equals to UPDATED_AUTONOMY_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("autonomy_t_3_predator.in=" + UPDATED_AUTONOMY_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAutonomy_t_3_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where autonomy_t_3_predator is not null
        defaultSimulationRunShouldBeFound("autonomy_t_3_predator.specified=true");

        // Get all the simulationRunList where autonomy_t_3_predator is null
        defaultSimulationRunShouldNotBeFound("autonomy_t_3_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_nonteam_weight_t_3_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_nonteam_weight_t_3_predator equals to DEFAULT_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("avoid_nonteam_weight_t_3_predator.equals=" + DEFAULT_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR);

        // Get all the simulationRunList where avoid_nonteam_weight_t_3_predator equals to UPDATED_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("avoid_nonteam_weight_t_3_predator.equals=" + UPDATED_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_nonteam_weight_t_3_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_nonteam_weight_t_3_predator in DEFAULT_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR or UPDATED_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("avoid_nonteam_weight_t_3_predator.in=" + DEFAULT_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR + "," + UPDATED_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR);

        // Get all the simulationRunList where avoid_nonteam_weight_t_3_predator equals to UPDATED_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("avoid_nonteam_weight_t_3_predator.in=" + UPDATED_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_nonteam_weight_t_3_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_nonteam_weight_t_3_predator is not null
        defaultSimulationRunShouldBeFound("avoid_nonteam_weight_t_3_predator.specified=true");

        // Get all the simulationRunList where avoid_nonteam_weight_t_3_predator is null
        defaultSimulationRunShouldNotBeFound("avoid_nonteam_weight_t_3_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_team_weight_t_3_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_team_weight_t_3_predator equals to DEFAULT_AVOID_TEAM_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("avoid_team_weight_t_3_predator.equals=" + DEFAULT_AVOID_TEAM_WEIGHT_T_3_PREDATOR);

        // Get all the simulationRunList where avoid_team_weight_t_3_predator equals to UPDATED_AVOID_TEAM_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("avoid_team_weight_t_3_predator.equals=" + UPDATED_AVOID_TEAM_WEIGHT_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_team_weight_t_3_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_team_weight_t_3_predator in DEFAULT_AVOID_TEAM_WEIGHT_T_3_PREDATOR or UPDATED_AVOID_TEAM_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("avoid_team_weight_t_3_predator.in=" + DEFAULT_AVOID_TEAM_WEIGHT_T_3_PREDATOR + "," + UPDATED_AVOID_TEAM_WEIGHT_T_3_PREDATOR);

        // Get all the simulationRunList where avoid_team_weight_t_3_predator equals to UPDATED_AVOID_TEAM_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("avoid_team_weight_t_3_predator.in=" + UPDATED_AVOID_TEAM_WEIGHT_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByAvoid_team_weight_t_3_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where avoid_team_weight_t_3_predator is not null
        defaultSimulationRunShouldBeFound("avoid_team_weight_t_3_predator.specified=true");

        // Get all the simulationRunList where avoid_team_weight_t_3_predator is null
        defaultSimulationRunShouldNotBeFound("avoid_team_weight_t_3_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCapture_range_t_3_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where capture_range_t_3_predator equals to DEFAULT_CAPTURE_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("capture_range_t_3_predator.equals=" + DEFAULT_CAPTURE_RANGE_T_3_PREDATOR);

        // Get all the simulationRunList where capture_range_t_3_predator equals to UPDATED_CAPTURE_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("capture_range_t_3_predator.equals=" + UPDATED_CAPTURE_RANGE_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCapture_range_t_3_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where capture_range_t_3_predator in DEFAULT_CAPTURE_RANGE_T_3_PREDATOR or UPDATED_CAPTURE_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("capture_range_t_3_predator.in=" + DEFAULT_CAPTURE_RANGE_T_3_PREDATOR + "," + UPDATED_CAPTURE_RANGE_T_3_PREDATOR);

        // Get all the simulationRunList where capture_range_t_3_predator equals to UPDATED_CAPTURE_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("capture_range_t_3_predator.in=" + UPDATED_CAPTURE_RANGE_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCapture_range_t_3_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where capture_range_t_3_predator is not null
        defaultSimulationRunShouldBeFound("capture_range_t_3_predator.specified=true");

        // Get all the simulationRunList where capture_range_t_3_predator is null
        defaultSimulationRunShouldNotBeFound("capture_range_t_3_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCapture_range_t_3_predatorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where capture_range_t_3_predator greater than or equals to DEFAULT_CAPTURE_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("capture_range_t_3_predator.greaterOrEqualThan=" + DEFAULT_CAPTURE_RANGE_T_3_PREDATOR);

        // Get all the simulationRunList where capture_range_t_3_predator greater than or equals to UPDATED_CAPTURE_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("capture_range_t_3_predator.greaterOrEqualThan=" + UPDATED_CAPTURE_RANGE_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCapture_range_t_3_predatorIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where capture_range_t_3_predator less than or equals to DEFAULT_CAPTURE_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("capture_range_t_3_predator.lessThan=" + DEFAULT_CAPTURE_RANGE_T_3_PREDATOR);

        // Get all the simulationRunList where capture_range_t_3_predator less than or equals to UPDATED_CAPTURE_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("capture_range_t_3_predator.lessThan=" + UPDATED_CAPTURE_RANGE_T_3_PREDATOR);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByCentroid_weight_t_3_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where centroid_weight_t_3_predator equals to DEFAULT_CENTROID_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("centroid_weight_t_3_predator.equals=" + DEFAULT_CENTROID_WEIGHT_T_3_PREDATOR);

        // Get all the simulationRunList where centroid_weight_t_3_predator equals to UPDATED_CENTROID_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("centroid_weight_t_3_predator.equals=" + UPDATED_CENTROID_WEIGHT_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCentroid_weight_t_3_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where centroid_weight_t_3_predator in DEFAULT_CENTROID_WEIGHT_T_3_PREDATOR or UPDATED_CENTROID_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("centroid_weight_t_3_predator.in=" + DEFAULT_CENTROID_WEIGHT_T_3_PREDATOR + "," + UPDATED_CENTROID_WEIGHT_T_3_PREDATOR);

        // Get all the simulationRunList where centroid_weight_t_3_predator equals to UPDATED_CENTROID_WEIGHT_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("centroid_weight_t_3_predator.in=" + UPDATED_CENTROID_WEIGHT_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByCentroid_weight_t_3_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where centroid_weight_t_3_predator is not null
        defaultSimulationRunShouldBeFound("centroid_weight_t_3_predator.specified=true");

        // Get all the simulationRunList where centroid_weight_t_3_predator is null
        defaultSimulationRunShouldNotBeFound("centroid_weight_t_3_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_3_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_3_predator equals to DEFAULT_COMMS_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("comms_range_t_3_predator.equals=" + DEFAULT_COMMS_RANGE_T_3_PREDATOR);

        // Get all the simulationRunList where comms_range_t_3_predator equals to UPDATED_COMMS_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("comms_range_t_3_predator.equals=" + UPDATED_COMMS_RANGE_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_3_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_3_predator in DEFAULT_COMMS_RANGE_T_3_PREDATOR or UPDATED_COMMS_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("comms_range_t_3_predator.in=" + DEFAULT_COMMS_RANGE_T_3_PREDATOR + "," + UPDATED_COMMS_RANGE_T_3_PREDATOR);

        // Get all the simulationRunList where comms_range_t_3_predator equals to UPDATED_COMMS_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("comms_range_t_3_predator.in=" + UPDATED_COMMS_RANGE_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_3_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_3_predator is not null
        defaultSimulationRunShouldBeFound("comms_range_t_3_predator.specified=true");

        // Get all the simulationRunList where comms_range_t_3_predator is null
        defaultSimulationRunShouldNotBeFound("comms_range_t_3_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_3_predatorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_3_predator greater than or equals to DEFAULT_COMMS_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("comms_range_t_3_predator.greaterOrEqualThan=" + DEFAULT_COMMS_RANGE_T_3_PREDATOR);

        // Get all the simulationRunList where comms_range_t_3_predator greater than or equals to UPDATED_COMMS_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("comms_range_t_3_predator.greaterOrEqualThan=" + UPDATED_COMMS_RANGE_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByComms_range_t_3_predatorIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where comms_range_t_3_predator less than or equals to DEFAULT_COMMS_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("comms_range_t_3_predator.lessThan=" + DEFAULT_COMMS_RANGE_T_3_PREDATOR);

        // Get all the simulationRunList where comms_range_t_3_predator less than or equals to UPDATED_COMMS_RANGE_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("comms_range_t_3_predator.lessThan=" + UPDATED_COMMS_RANGE_T_3_PREDATOR);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_3_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_3_predator equals to DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("max_pred_speed_t_3_predator.equals=" + DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR);

        // Get all the simulationRunList where max_pred_speed_t_3_predator equals to UPDATED_MAX_PRED_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_3_predator.equals=" + UPDATED_MAX_PRED_SPEED_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_3_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_3_predator in DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR or UPDATED_MAX_PRED_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("max_pred_speed_t_3_predator.in=" + DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR + "," + UPDATED_MAX_PRED_SPEED_T_3_PREDATOR);

        // Get all the simulationRunList where max_pred_speed_t_3_predator equals to UPDATED_MAX_PRED_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_3_predator.in=" + UPDATED_MAX_PRED_SPEED_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_3_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_3_predator is not null
        defaultSimulationRunShouldBeFound("max_pred_speed_t_3_predator.specified=true");

        // Get all the simulationRunList where max_pred_speed_t_3_predator is null
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_3_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_3_predatorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_3_predator greater than or equals to DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("max_pred_speed_t_3_predator.greaterOrEqualThan=" + DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR);

        // Get all the simulationRunList where max_pred_speed_t_3_predator greater than or equals to UPDATED_MAX_PRED_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_3_predator.greaterOrEqualThan=" + UPDATED_MAX_PRED_SPEED_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_pred_speed_t_3_predatorIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_pred_speed_t_3_predator less than or equals to DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_pred_speed_t_3_predator.lessThan=" + DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR);

        // Get all the simulationRunList where max_pred_speed_t_3_predator less than or equals to UPDATED_MAX_PRED_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("max_pred_speed_t_3_predator.lessThan=" + UPDATED_MAX_PRED_SPEED_T_3_PREDATOR);
    }


    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_3_predatorIsEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_3_predator equals to DEFAULT_MAX_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("max_speed_t_3_predator.equals=" + DEFAULT_MAX_SPEED_T_3_PREDATOR);

        // Get all the simulationRunList where max_speed_t_3_predator equals to UPDATED_MAX_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_speed_t_3_predator.equals=" + UPDATED_MAX_SPEED_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_3_predatorIsInShouldWork() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_3_predator in DEFAULT_MAX_SPEED_T_3_PREDATOR or UPDATED_MAX_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("max_speed_t_3_predator.in=" + DEFAULT_MAX_SPEED_T_3_PREDATOR + "," + UPDATED_MAX_SPEED_T_3_PREDATOR);

        // Get all the simulationRunList where max_speed_t_3_predator equals to UPDATED_MAX_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_speed_t_3_predator.in=" + UPDATED_MAX_SPEED_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_3_predatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_3_predator is not null
        defaultSimulationRunShouldBeFound("max_speed_t_3_predator.specified=true");

        // Get all the simulationRunList where max_speed_t_3_predator is null
        defaultSimulationRunShouldNotBeFound("max_speed_t_3_predator.specified=false");
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_3_predatorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_3_predator greater than or equals to DEFAULT_MAX_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("max_speed_t_3_predator.greaterOrEqualThan=" + DEFAULT_MAX_SPEED_T_3_PREDATOR);

        // Get all the simulationRunList where max_speed_t_3_predator greater than or equals to UPDATED_MAX_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_speed_t_3_predator.greaterOrEqualThan=" + UPDATED_MAX_SPEED_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void getAllSimulationRunsByMax_speed_t_3_predatorIsLessThanSomething() throws Exception {
        // Initialize the database
        simulationRunRepository.saveAndFlush(simulationRun);

        // Get all the simulationRunList where max_speed_t_3_predator less than or equals to DEFAULT_MAX_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldNotBeFound("max_speed_t_3_predator.lessThan=" + DEFAULT_MAX_SPEED_T_3_PREDATOR);

        // Get all the simulationRunList where max_speed_t_3_predator less than or equals to UPDATED_MAX_SPEED_T_3_PREDATOR
        defaultSimulationRunShouldBeFound("max_speed_t_3_predator.lessThan=" + UPDATED_MAX_SPEED_T_3_PREDATOR);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSimulationRunShouldBeFound(String filter) throws Exception {
        restSimulationRunMockMvc.perform(get("/api/simulation-runs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(simulationRun.getId().intValue())))
            .andExpect(jsonPath("$.[*].simulationTrial").value(hasItem(DEFAULT_SIMULATION_TRIAL.toString())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].team_id").value(hasItem(DEFAULT_TEAM_ID)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].teamCapture").value(hasItem(DEFAULT_TEAM_CAPTURE)))
            .andExpect(jsonPath("$.[*].nonTeamCapture").value(hasItem(DEFAULT_NON_TEAM_CAPTURE)))
            .andExpect(jsonPath("$.[*].job_num").value(hasItem(DEFAULT_JOB_NUM)))
            .andExpect(jsonPath("$.[*].task_num").value(hasItem(DEFAULT_TASK_NUM)))
            .andExpect(jsonPath("$.[*].results_dir").value(hasItem(DEFAULT_RESULTS_DIR.toString())))
            .andExpect(jsonPath("$.[*].num_rows").value(hasItem(DEFAULT_NUM_ROWS)))
            .andExpect(jsonPath("$.[*].align_weight_t_1").value(hasItem(DEFAULT_ALIGN_WEIGHT_T_1.doubleValue())))
            .andExpect(jsonPath("$.[*].avoid_nonteam_weight_t_1").value(hasItem(DEFAULT_AVOID_NONTEAM_WEIGHT_T_1.doubleValue())))
            .andExpect(jsonPath("$.[*].avoid_team_weight_t_1").value(hasItem(DEFAULT_AVOID_TEAM_WEIGHT_T_1.doubleValue())))
            .andExpect(jsonPath("$.[*].centroid_weight_t_1").value(hasItem(DEFAULT_CENTROID_WEIGHT_T_1.doubleValue())))
            .andExpect(jsonPath("$.[*].comms_range_t_1").value(hasItem(DEFAULT_COMMS_RANGE_T_1)))
            .andExpect(jsonPath("$.[*].fov_az_t_1").value(hasItem(DEFAULT_FOV_AZ_T_1)))
            .andExpect(jsonPath("$.[*].fov_el_t_1").value(hasItem(DEFAULT_FOV_EL_T_1)))
            .andExpect(jsonPath("$.[*].goal_weight_t_1").value(hasItem(DEFAULT_GOAL_WEIGHT_T_1.doubleValue())))
            .andExpect(jsonPath("$.[*].max_pred_speed_t_1").value(hasItem(DEFAULT_MAX_PRED_SPEED_T_1)))
            .andExpect(jsonPath("$.[*].max_speed_t_1").value(hasItem(DEFAULT_MAX_SPEED_T_1)))
            .andExpect(jsonPath("$.[*].sphere_of_influence_t_1").value(hasItem(DEFAULT_SPHERE_OF_INFLUENCE_T_1)))
            .andExpect(jsonPath("$.[*].motion_model_t_1").value(hasItem(DEFAULT_MOTION_MODEL_T_1.toString())))
            .andExpect(jsonPath("$.[*].vel_max_t_1").value(hasItem(DEFAULT_VEL_MAX_T_1)))
            .andExpect(jsonPath("$.[*].motion_model_predator").value(hasItem(DEFAULT_MOTION_MODEL_PREDATOR.toString())))
            .andExpect(jsonPath("$.[*].pitch_rate_max_predator").value(hasItem(DEFAULT_PITCH_RATE_MAX_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].turn_rate_max_predator").value(hasItem(DEFAULT_TURN_RATE_MAX_PREDATOR)))
            .andExpect(jsonPath("$.[*].vel_max_predator").value(hasItem(DEFAULT_VEL_MAX_PREDATOR)))
            .andExpect(jsonPath("$.[*].align_weight_t_2_predator").value(hasItem(DEFAULT_ALIGN_WEIGHT_T_2_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].allow_prey_switching_t_2_predator").value(hasItem(DEFAULT_ALLOW_PREY_SWITCHING_T_2_PREDATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].autonomy_t_2_predator").value(hasItem(DEFAULT_AUTONOMY_T_2_PREDATOR.toString())))
            .andExpect(jsonPath("$.[*].avoid_nonteam_weight_t_2_predator").value(hasItem(DEFAULT_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].avoid_team_weight_t_2_predator").value(hasItem(DEFAULT_AVOID_TEAM_WEIGHT_T_2_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].capture_range_t_2_predator").value(hasItem(DEFAULT_CAPTURE_RANGE_T_2_PREDATOR)))
            .andExpect(jsonPath("$.[*].centroid_weight_t_2_predator").value(hasItem(DEFAULT_CENTROID_WEIGHT_T_2_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].comms_range_t_2_predator").value(hasItem(DEFAULT_COMMS_RANGE_T_2_PREDATOR)))
            .andExpect(jsonPath("$.[*].max_pred_speed_t_2_predator").value(hasItem(DEFAULT_MAX_PRED_SPEED_T_2_PREDATOR)))
            .andExpect(jsonPath("$.[*].max_speed_t_2_predator").value(hasItem(DEFAULT_MAX_SPEED_T_2_PREDATOR)))
            .andExpect(jsonPath("$.[*].align_weight_t_3_predator").value(hasItem(DEFAULT_ALIGN_WEIGHT_T_3_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].allow_prey_switching_t_3_predator").value(hasItem(DEFAULT_ALLOW_PREY_SWITCHING_T_3_PREDATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].autonomy_t_3_predator").value(hasItem(DEFAULT_AUTONOMY_T_3_PREDATOR.toString())))
            .andExpect(jsonPath("$.[*].avoid_nonteam_weight_t_3_predator").value(hasItem(DEFAULT_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].avoid_team_weight_t_3_predator").value(hasItem(DEFAULT_AVOID_TEAM_WEIGHT_T_3_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].capture_range_t_3_predator").value(hasItem(DEFAULT_CAPTURE_RANGE_T_3_PREDATOR)))
            .andExpect(jsonPath("$.[*].centroid_weight_t_3_predator").value(hasItem(DEFAULT_CENTROID_WEIGHT_T_3_PREDATOR.doubleValue())))
            .andExpect(jsonPath("$.[*].comms_range_t_3_predator").value(hasItem(DEFAULT_COMMS_RANGE_T_3_PREDATOR)))
            .andExpect(jsonPath("$.[*].max_pred_speed_t_3_predator").value(hasItem(DEFAULT_MAX_PRED_SPEED_T_3_PREDATOR)))
            .andExpect(jsonPath("$.[*].max_speed_t_3_predator").value(hasItem(DEFAULT_MAX_SPEED_T_3_PREDATOR)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSimulationRunShouldNotBeFound(String filter) throws Exception {
        restSimulationRunMockMvc.perform(get("/api/simulation-runs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSimulationRun() throws Exception {
        // Get the simulationRun
        restSimulationRunMockMvc.perform(get("/api/simulation-runs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSimulationRun() throws Exception {
        // Initialize the database
        simulationRunService.save(simulationRun);

        int databaseSizeBeforeUpdate = simulationRunRepository.findAll().size();

        // Update the simulationRun
        SimulationRun updatedSimulationRun = simulationRunRepository.findOne(simulationRun.getId());
        // Disconnect from session so that the updates on updatedSimulationRun are not directly saved in db
        em.detach(updatedSimulationRun);
        updatedSimulationRun
            .simulationTrial(UPDATED_SIMULATION_TRIAL)
            .index(UPDATED_INDEX)
            .team_id(UPDATED_TEAM_ID)
            .score(UPDATED_SCORE)
            .teamCapture(UPDATED_TEAM_CAPTURE)
            .nonTeamCapture(UPDATED_NON_TEAM_CAPTURE)
            .job_num(UPDATED_JOB_NUM)
            .task_num(UPDATED_TASK_NUM)
            .results_dir(UPDATED_RESULTS_DIR)
            .num_rows(UPDATED_NUM_ROWS)
            .align_weight_t_1(UPDATED_ALIGN_WEIGHT_T_1)
            .avoid_nonteam_weight_t_1(UPDATED_AVOID_NONTEAM_WEIGHT_T_1)
            .avoid_team_weight_t_1(UPDATED_AVOID_TEAM_WEIGHT_T_1)
            .centroid_weight_t_1(UPDATED_CENTROID_WEIGHT_T_1)
            .comms_range_t_1(UPDATED_COMMS_RANGE_T_1)
            .fov_az_t_1(UPDATED_FOV_AZ_T_1)
            .fov_el_t_1(UPDATED_FOV_EL_T_1)
            .goal_weight_t_1(UPDATED_GOAL_WEIGHT_T_1)
            .max_pred_speed_t_1(UPDATED_MAX_PRED_SPEED_T_1)
            .max_speed_t_1(UPDATED_MAX_SPEED_T_1)
            .sphere_of_influence_t_1(UPDATED_SPHERE_OF_INFLUENCE_T_1)
            .motion_model_t_1(UPDATED_MOTION_MODEL_T_1)
            .vel_max_t_1(UPDATED_VEL_MAX_T_1)
            .motion_model_predator(UPDATED_MOTION_MODEL_PREDATOR)
            .pitch_rate_max_predator(UPDATED_PITCH_RATE_MAX_PREDATOR)
            .turn_rate_max_predator(UPDATED_TURN_RATE_MAX_PREDATOR)
            .vel_max_predator(UPDATED_VEL_MAX_PREDATOR)
            .align_weight_t_2_predator(UPDATED_ALIGN_WEIGHT_T_2_PREDATOR)
            .allow_prey_switching_t_2_predator(UPDATED_ALLOW_PREY_SWITCHING_T_2_PREDATOR)
            .autonomy_t_2_predator(UPDATED_AUTONOMY_T_2_PREDATOR)
            .avoid_nonteam_weight_t_2_predator(UPDATED_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR)
            .avoid_team_weight_t_2_predator(UPDATED_AVOID_TEAM_WEIGHT_T_2_PREDATOR)
            .capture_range_t_2_predator(UPDATED_CAPTURE_RANGE_T_2_PREDATOR)
            .centroid_weight_t_2_predator(UPDATED_CENTROID_WEIGHT_T_2_PREDATOR)
            .comms_range_t_2_predator(UPDATED_COMMS_RANGE_T_2_PREDATOR)
            .max_pred_speed_t_2_predator(UPDATED_MAX_PRED_SPEED_T_2_PREDATOR)
            .max_speed_t_2_predator(UPDATED_MAX_SPEED_T_2_PREDATOR)
            .align_weight_t_3_predator(UPDATED_ALIGN_WEIGHT_T_3_PREDATOR)
            .allow_prey_switching_t_3_predator(UPDATED_ALLOW_PREY_SWITCHING_T_3_PREDATOR)
            .autonomy_t_3_predator(UPDATED_AUTONOMY_T_3_PREDATOR)
            .avoid_nonteam_weight_t_3_predator(UPDATED_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR)
            .avoid_team_weight_t_3_predator(UPDATED_AVOID_TEAM_WEIGHT_T_3_PREDATOR)
            .capture_range_t_3_predator(UPDATED_CAPTURE_RANGE_T_3_PREDATOR)
            .centroid_weight_t_3_predator(UPDATED_CENTROID_WEIGHT_T_3_PREDATOR)
            .comms_range_t_3_predator(UPDATED_COMMS_RANGE_T_3_PREDATOR)
            .max_pred_speed_t_3_predator(UPDATED_MAX_PRED_SPEED_T_3_PREDATOR)
            .max_speed_t_3_predator(UPDATED_MAX_SPEED_T_3_PREDATOR);

        restSimulationRunMockMvc.perform(put("/api/simulation-runs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSimulationRun)))
            .andExpect(status().isOk());

        // Validate the SimulationRun in the database
        List<SimulationRun> simulationRunList = simulationRunRepository.findAll();
        assertThat(simulationRunList).hasSize(databaseSizeBeforeUpdate);
        SimulationRun testSimulationRun = simulationRunList.get(simulationRunList.size() - 1);
        assertThat(testSimulationRun.getSimulationTrial()).isEqualTo(UPDATED_SIMULATION_TRIAL);
        assertThat(testSimulationRun.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testSimulationRun.getTeam_id()).isEqualTo(UPDATED_TEAM_ID);
        assertThat(testSimulationRun.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testSimulationRun.getTeamCapture()).isEqualTo(UPDATED_TEAM_CAPTURE);
        assertThat(testSimulationRun.getNonTeamCapture()).isEqualTo(UPDATED_NON_TEAM_CAPTURE);
        assertThat(testSimulationRun.getJob_num()).isEqualTo(UPDATED_JOB_NUM);
        assertThat(testSimulationRun.getTask_num()).isEqualTo(UPDATED_TASK_NUM);
        assertThat(testSimulationRun.getResults_dir()).isEqualTo(UPDATED_RESULTS_DIR);
        assertThat(testSimulationRun.getNum_rows()).isEqualTo(UPDATED_NUM_ROWS);
        assertThat(testSimulationRun.getAlign_weight_t_1()).isEqualTo(UPDATED_ALIGN_WEIGHT_T_1);
        assertThat(testSimulationRun.getAvoid_nonteam_weight_t_1()).isEqualTo(UPDATED_AVOID_NONTEAM_WEIGHT_T_1);
        assertThat(testSimulationRun.getAvoid_team_weight_t_1()).isEqualTo(UPDATED_AVOID_TEAM_WEIGHT_T_1);
        assertThat(testSimulationRun.getCentroid_weight_t_1()).isEqualTo(UPDATED_CENTROID_WEIGHT_T_1);
        assertThat(testSimulationRun.getComms_range_t_1()).isEqualTo(UPDATED_COMMS_RANGE_T_1);
        assertThat(testSimulationRun.getFov_az_t_1()).isEqualTo(UPDATED_FOV_AZ_T_1);
        assertThat(testSimulationRun.getFov_el_t_1()).isEqualTo(UPDATED_FOV_EL_T_1);
        assertThat(testSimulationRun.getGoal_weight_t_1()).isEqualTo(UPDATED_GOAL_WEIGHT_T_1);
        assertThat(testSimulationRun.getMax_pred_speed_t_1()).isEqualTo(UPDATED_MAX_PRED_SPEED_T_1);
        assertThat(testSimulationRun.getMax_speed_t_1()).isEqualTo(UPDATED_MAX_SPEED_T_1);
        assertThat(testSimulationRun.getSphere_of_influence_t_1()).isEqualTo(UPDATED_SPHERE_OF_INFLUENCE_T_1);
        assertThat(testSimulationRun.getMotion_model_t_1()).isEqualTo(UPDATED_MOTION_MODEL_T_1);
        assertThat(testSimulationRun.getVel_max_t_1()).isEqualTo(UPDATED_VEL_MAX_T_1);
        assertThat(testSimulationRun.getMotion_model_predator()).isEqualTo(UPDATED_MOTION_MODEL_PREDATOR);
        assertThat(testSimulationRun.getPitch_rate_max_predator()).isEqualTo(UPDATED_PITCH_RATE_MAX_PREDATOR);
        assertThat(testSimulationRun.getTurn_rate_max_predator()).isEqualTo(UPDATED_TURN_RATE_MAX_PREDATOR);
        assertThat(testSimulationRun.getVel_max_predator()).isEqualTo(UPDATED_VEL_MAX_PREDATOR);
        assertThat(testSimulationRun.getAlign_weight_t_2_predator()).isEqualTo(UPDATED_ALIGN_WEIGHT_T_2_PREDATOR);
        assertThat(testSimulationRun.isAllow_prey_switching_t_2_predator()).isEqualTo(UPDATED_ALLOW_PREY_SWITCHING_T_2_PREDATOR);
        assertThat(testSimulationRun.getAutonomy_t_2_predator()).isEqualTo(UPDATED_AUTONOMY_T_2_PREDATOR);
        assertThat(testSimulationRun.getAvoid_nonteam_weight_t_2_predator()).isEqualTo(UPDATED_AVOID_NONTEAM_WEIGHT_T_2_PREDATOR);
        assertThat(testSimulationRun.getAvoid_team_weight_t_2_predator()).isEqualTo(UPDATED_AVOID_TEAM_WEIGHT_T_2_PREDATOR);
        assertThat(testSimulationRun.getCapture_range_t_2_predator()).isEqualTo(UPDATED_CAPTURE_RANGE_T_2_PREDATOR);
        assertThat(testSimulationRun.getCentroid_weight_t_2_predator()).isEqualTo(UPDATED_CENTROID_WEIGHT_T_2_PREDATOR);
        assertThat(testSimulationRun.getComms_range_t_2_predator()).isEqualTo(UPDATED_COMMS_RANGE_T_2_PREDATOR);
        assertThat(testSimulationRun.getMax_pred_speed_t_2_predator()).isEqualTo(UPDATED_MAX_PRED_SPEED_T_2_PREDATOR);
        assertThat(testSimulationRun.getMax_speed_t_2_predator()).isEqualTo(UPDATED_MAX_SPEED_T_2_PREDATOR);
        assertThat(testSimulationRun.getAlign_weight_t_3_predator()).isEqualTo(UPDATED_ALIGN_WEIGHT_T_3_PREDATOR);
        assertThat(testSimulationRun.isAllow_prey_switching_t_3_predator()).isEqualTo(UPDATED_ALLOW_PREY_SWITCHING_T_3_PREDATOR);
        assertThat(testSimulationRun.getAutonomy_t_3_predator()).isEqualTo(UPDATED_AUTONOMY_T_3_PREDATOR);
        assertThat(testSimulationRun.getAvoid_nonteam_weight_t_3_predator()).isEqualTo(UPDATED_AVOID_NONTEAM_WEIGHT_T_3_PREDATOR);
        assertThat(testSimulationRun.getAvoid_team_weight_t_3_predator()).isEqualTo(UPDATED_AVOID_TEAM_WEIGHT_T_3_PREDATOR);
        assertThat(testSimulationRun.getCapture_range_t_3_predator()).isEqualTo(UPDATED_CAPTURE_RANGE_T_3_PREDATOR);
        assertThat(testSimulationRun.getCentroid_weight_t_3_predator()).isEqualTo(UPDATED_CENTROID_WEIGHT_T_3_PREDATOR);
        assertThat(testSimulationRun.getComms_range_t_3_predator()).isEqualTo(UPDATED_COMMS_RANGE_T_3_PREDATOR);
        assertThat(testSimulationRun.getMax_pred_speed_t_3_predator()).isEqualTo(UPDATED_MAX_PRED_SPEED_T_3_PREDATOR);
        assertThat(testSimulationRun.getMax_speed_t_3_predator()).isEqualTo(UPDATED_MAX_SPEED_T_3_PREDATOR);
    }

    @Test
    @Transactional
    public void updateNonExistingSimulationRun() throws Exception {
        int databaseSizeBeforeUpdate = simulationRunRepository.findAll().size();

        // Create the SimulationRun

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSimulationRunMockMvc.perform(put("/api/simulation-runs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(simulationRun)))
            .andExpect(status().isCreated());

        // Validate the SimulationRun in the database
        List<SimulationRun> simulationRunList = simulationRunRepository.findAll();
        assertThat(simulationRunList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSimulationRun() throws Exception {
        // Initialize the database
        simulationRunService.save(simulationRun);

        int databaseSizeBeforeDelete = simulationRunRepository.findAll().size();

        // Get the simulationRun
        restSimulationRunMockMvc.perform(delete("/api/simulation-runs/{id}", simulationRun.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SimulationRun> simulationRunList = simulationRunRepository.findAll();
        assertThat(simulationRunList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SimulationRun.class);
        SimulationRun simulationRun1 = new SimulationRun();
        simulationRun1.setId(1L);
        SimulationRun simulationRun2 = new SimulationRun();
        simulationRun2.setId(simulationRun1.getId());
        assertThat(simulationRun1).isEqualTo(simulationRun2);
        simulationRun2.setId(2L);
        assertThat(simulationRun1).isNotEqualTo(simulationRun2);
        simulationRun1.setId(null);
        assertThat(simulationRun1).isNotEqualTo(simulationRun2);
    }
}
