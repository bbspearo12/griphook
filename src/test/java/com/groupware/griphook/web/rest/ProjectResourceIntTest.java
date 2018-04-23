package com.groupware.griphook.web.rest;

import com.groupware.griphook.GriphookApp;

import com.groupware.griphook.domain.Project;
import com.groupware.griphook.repository.ProjectRepository;
import com.groupware.griphook.web.rest.errors.ExceptionTranslator;

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

import static com.groupware.griphook.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectResource REST controller.
 *
 * @see ProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GriphookApp.class)
public class ProjectResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUT_MANAGER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUT_MANAGER = "BBBBBBBBBB";

    private static final String DEFAULT_PRESALES_ARCHITECT = "AAAAAAAAAA";
    private static final String UPDATED_PRESALES_ARCHITECT = "BBBBBBBBBB";

    private static final Float DEFAULT_DEFAULT_PROJECT_MARGIN = 1F;
    private static final Float UPDATED_DEFAULT_PROJECT_MARGIN = 2F;

    private static final Float DEFAULT_SUBCONTRACT_PROJECT_MARGIN = 1F;
    private static final Float UPDATED_SUBCONTRACT_PROJECT_MARGIN = 2F;

    private static final Float DEFAULT_PMPERCENTAGE = 1F;
    private static final Float UPDATED_PMPERCENTAGE = 2F;

    private static final Float DEFAULT_RISK = 1F;
    private static final Float UPDATED_RISK = 2F;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectMockMvc;

    private Project project;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectResource projectResource = new ProjectResource(projectRepository);
        this.restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
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
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .name(DEFAULT_NAME)
            .accoutManager(DEFAULT_ACCOUT_MANAGER)
            .presalesArchitect(DEFAULT_PRESALES_ARCHITECT)
            .defaultProjectMargin(DEFAULT_DEFAULT_PROJECT_MARGIN)
            .subcontractProjectMargin(DEFAULT_SUBCONTRACT_PROJECT_MARGIN)
            .pmpercentage(DEFAULT_PMPERCENTAGE)
            .risk(DEFAULT_RISK);
        return project;
    }

    @Before
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    public void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProject.getAccoutManager()).isEqualTo(DEFAULT_ACCOUT_MANAGER);
        assertThat(testProject.getPresalesArchitect()).isEqualTo(DEFAULT_PRESALES_ARCHITECT);
        assertThat(testProject.getDefaultProjectMargin()).isEqualTo(DEFAULT_DEFAULT_PROJECT_MARGIN);
        assertThat(testProject.getSubcontractProjectMargin()).isEqualTo(DEFAULT_SUBCONTRACT_PROJECT_MARGIN);
        assertThat(testProject.getPmpercentage()).isEqualTo(DEFAULT_PMPERCENTAGE);
        assertThat(testProject.getRisk()).isEqualTo(DEFAULT_RISK);
    }

    @Test
    @Transactional
    public void createProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project with an existing ID
        project.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].accoutManager").value(hasItem(DEFAULT_ACCOUT_MANAGER.toString())))
            .andExpect(jsonPath("$.[*].presalesArchitect").value(hasItem(DEFAULT_PRESALES_ARCHITECT.toString())))
            .andExpect(jsonPath("$.[*].defaultProjectMargin").value(hasItem(DEFAULT_DEFAULT_PROJECT_MARGIN.doubleValue())))
            .andExpect(jsonPath("$.[*].subcontractProjectMargin").value(hasItem(DEFAULT_SUBCONTRACT_PROJECT_MARGIN.doubleValue())))
            .andExpect(jsonPath("$.[*].pmpercentage").value(hasItem(DEFAULT_PMPERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].risk").value(hasItem(DEFAULT_RISK.doubleValue())));
    }

    @Test
    @Transactional
    public void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.accoutManager").value(DEFAULT_ACCOUT_MANAGER.toString()))
            .andExpect(jsonPath("$.presalesArchitect").value(DEFAULT_PRESALES_ARCHITECT.toString()))
            .andExpect(jsonPath("$.defaultProjectMargin").value(DEFAULT_DEFAULT_PROJECT_MARGIN.doubleValue()))
            .andExpect(jsonPath("$.subcontractProjectMargin").value(DEFAULT_SUBCONTRACT_PROJECT_MARGIN.doubleValue()))
            .andExpect(jsonPath("$.pmpercentage").value(DEFAULT_PMPERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.risk").value(DEFAULT_RISK.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findOne(project.getId());
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject);
        updatedProject
            .name(UPDATED_NAME)
            .accoutManager(UPDATED_ACCOUT_MANAGER)
            .presalesArchitect(UPDATED_PRESALES_ARCHITECT)
            .defaultProjectMargin(UPDATED_DEFAULT_PROJECT_MARGIN)
            .subcontractProjectMargin(UPDATED_SUBCONTRACT_PROJECT_MARGIN)
            .pmpercentage(UPDATED_PMPERCENTAGE)
            .risk(UPDATED_RISK);

        restProjectMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProject)))
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProject.getAccoutManager()).isEqualTo(UPDATED_ACCOUT_MANAGER);
        assertThat(testProject.getPresalesArchitect()).isEqualTo(UPDATED_PRESALES_ARCHITECT);
        assertThat(testProject.getDefaultProjectMargin()).isEqualTo(UPDATED_DEFAULT_PROJECT_MARGIN);
        assertThat(testProject.getSubcontractProjectMargin()).isEqualTo(UPDATED_SUBCONTRACT_PROJECT_MARGIN);
        assertThat(testProject.getPmpercentage()).isEqualTo(UPDATED_PMPERCENTAGE);
        assertThat(testProject.getRisk()).isEqualTo(UPDATED_RISK);
    }

    @Test
    @Transactional
    public void updateNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Create the Project

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Get the project
        restProjectMockMvc.perform(delete("/api/projects/{id}", project.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = new Project();
        project1.setId(1L);
        Project project2 = new Project();
        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);
        project2.setId(2L);
        assertThat(project1).isNotEqualTo(project2);
        project1.setId(null);
        assertThat(project1).isNotEqualTo(project2);
    }
}
