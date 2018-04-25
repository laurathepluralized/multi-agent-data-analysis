package edu.gatech.hpan.expmanager.web.rest;

import edu.gatech.hpan.expmanager.ExpManagerSql2App;

import edu.gatech.hpan.expmanager.domain.Experiment1;
import edu.gatech.hpan.expmanager.repository.Experiment1Repository;
import edu.gatech.hpan.expmanager.service.Experiment1Service;
import edu.gatech.hpan.expmanager.web.rest.errors.ExceptionTranslator;
import edu.gatech.hpan.expmanager.service.dto.Experiment1Criteria;
import edu.gatech.hpan.expmanager.service.Experiment1QueryService;

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
 * Test class for the Experiment1Resource REST controller.
 *
 * @see Experiment1Resource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExpManagerSql2App.class)
public class Experiment1ResourceIntTest {

    private static final String DEFAULT_INDEX = "AAAAAAAAAA";
    private static final String UPDATED_INDEX = "BBBBBBBBBB";

    @Autowired
    private Experiment1Repository experiment1Repository;

    @Autowired
    private Experiment1Service experiment1Service;

    @Autowired
    private Experiment1QueryService experiment1QueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExperiment1MockMvc;

    private Experiment1 experiment1;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Experiment1Resource experiment1Resource = new Experiment1Resource(experiment1Service, experiment1QueryService);
        this.restExperiment1MockMvc = MockMvcBuilders.standaloneSetup(experiment1Resource)
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
    public static Experiment1 createEntity(EntityManager em) {
        Experiment1 experiment1 = new Experiment1()
            .index(DEFAULT_INDEX);
        return experiment1;
    }

    @Before
    public void initTest() {
        experiment1 = createEntity(em);
    }

    @Test
    @Transactional
    public void createExperiment1() throws Exception {
        int databaseSizeBeforeCreate = experiment1Repository.findAll().size();

        // Create the Experiment1
        restExperiment1MockMvc.perform(post("/api/experiment-1-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experiment1)))
            .andExpect(status().isCreated());

        // Validate the Experiment1 in the database
        List<Experiment1> experiment1List = experiment1Repository.findAll();
        assertThat(experiment1List).hasSize(databaseSizeBeforeCreate + 1);
        Experiment1 testExperiment1 = experiment1List.get(experiment1List.size() - 1);
        assertThat(testExperiment1.getIndex()).isEqualTo(DEFAULT_INDEX);
    }

    @Test
    @Transactional
    public void createExperiment1WithExistingId() throws Exception {
        int databaseSizeBeforeCreate = experiment1Repository.findAll().size();

        // Create the Experiment1 with an existing ID
        experiment1.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExperiment1MockMvc.perform(post("/api/experiment-1-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experiment1)))
            .andExpect(status().isBadRequest());

        // Validate the Experiment1 in the database
        List<Experiment1> experiment1List = experiment1Repository.findAll();
        assertThat(experiment1List).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllExperiment1S() throws Exception {
        // Initialize the database
        experiment1Repository.saveAndFlush(experiment1);

        // Get all the experiment1List
        restExperiment1MockMvc.perform(get("/api/experiment-1-s?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experiment1.getId().intValue())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX.toString())));
    }

    @Test
    @Transactional
    public void getExperiment1() throws Exception {
        // Initialize the database
        experiment1Repository.saveAndFlush(experiment1);

        // Get the experiment1
        restExperiment1MockMvc.perform(get("/api/experiment-1-s/{id}", experiment1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(experiment1.getId().intValue()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX.toString()));
    }

    @Test
    @Transactional
    public void getAllExperiment1SByIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        experiment1Repository.saveAndFlush(experiment1);

        // Get all the experiment1List where index equals to DEFAULT_INDEX
        defaultExperiment1ShouldBeFound("index.equals=" + DEFAULT_INDEX);

        // Get all the experiment1List where index equals to UPDATED_INDEX
        defaultExperiment1ShouldNotBeFound("index.equals=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void getAllExperiment1SByIndexIsInShouldWork() throws Exception {
        // Initialize the database
        experiment1Repository.saveAndFlush(experiment1);

        // Get all the experiment1List where index in DEFAULT_INDEX or UPDATED_INDEX
        defaultExperiment1ShouldBeFound("index.in=" + DEFAULT_INDEX + "," + UPDATED_INDEX);

        // Get all the experiment1List where index equals to UPDATED_INDEX
        defaultExperiment1ShouldNotBeFound("index.in=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void getAllExperiment1SByIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        experiment1Repository.saveAndFlush(experiment1);

        // Get all the experiment1List where index is not null
        defaultExperiment1ShouldBeFound("index.specified=true");

        // Get all the experiment1List where index is null
        defaultExperiment1ShouldNotBeFound("index.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultExperiment1ShouldBeFound(String filter) throws Exception {
        restExperiment1MockMvc.perform(get("/api/experiment-1-s?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experiment1.getId().intValue())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultExperiment1ShouldNotBeFound(String filter) throws Exception {
        restExperiment1MockMvc.perform(get("/api/experiment-1-s?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingExperiment1() throws Exception {
        // Get the experiment1
        restExperiment1MockMvc.perform(get("/api/experiment-1-s/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExperiment1() throws Exception {
        // Initialize the database
        experiment1Service.save(experiment1);

        int databaseSizeBeforeUpdate = experiment1Repository.findAll().size();

        // Update the experiment1
        Experiment1 updatedExperiment1 = experiment1Repository.findOne(experiment1.getId());
        // Disconnect from session so that the updates on updatedExperiment1 are not directly saved in db
        em.detach(updatedExperiment1);
        updatedExperiment1
            .index(UPDATED_INDEX);

        restExperiment1MockMvc.perform(put("/api/experiment-1-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExperiment1)))
            .andExpect(status().isOk());

        // Validate the Experiment1 in the database
        List<Experiment1> experiment1List = experiment1Repository.findAll();
        assertThat(experiment1List).hasSize(databaseSizeBeforeUpdate);
        Experiment1 testExperiment1 = experiment1List.get(experiment1List.size() - 1);
        assertThat(testExperiment1.getIndex()).isEqualTo(UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void updateNonExistingExperiment1() throws Exception {
        int databaseSizeBeforeUpdate = experiment1Repository.findAll().size();

        // Create the Experiment1

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExperiment1MockMvc.perform(put("/api/experiment-1-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experiment1)))
            .andExpect(status().isCreated());

        // Validate the Experiment1 in the database
        List<Experiment1> experiment1List = experiment1Repository.findAll();
        assertThat(experiment1List).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExperiment1() throws Exception {
        // Initialize the database
        experiment1Service.save(experiment1);

        int databaseSizeBeforeDelete = experiment1Repository.findAll().size();

        // Get the experiment1
        restExperiment1MockMvc.perform(delete("/api/experiment-1-s/{id}", experiment1.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Experiment1> experiment1List = experiment1Repository.findAll();
        assertThat(experiment1List).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Experiment1.class);
        Experiment1 experiment11 = new Experiment1();
        experiment11.setId(1L);
        Experiment1 experiment12 = new Experiment1();
        experiment12.setId(experiment11.getId());
        assertThat(experiment11).isEqualTo(experiment12);
        experiment12.setId(2L);
        assertThat(experiment11).isNotEqualTo(experiment12);
        experiment11.setId(null);
        assertThat(experiment11).isNotEqualTo(experiment12);
    }
}
