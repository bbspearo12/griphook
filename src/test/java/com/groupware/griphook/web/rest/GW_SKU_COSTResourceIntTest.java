package com.groupware.griphook.web.rest;

import com.groupware.griphook.GriphookApp;

import com.groupware.griphook.domain.GW_SKU_COST;
import com.groupware.griphook.repository.GW_SKU_COSTRepository;
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

import com.groupware.griphook.domain.enumeration.GW_SKU;
/**
 * Test class for the GW_SKU_COSTResource REST controller.
 *
 * @see GW_SKU_COSTResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GriphookApp.class)
public class GW_SKU_COSTResourceIntTest {

    private static final GW_SKU DEFAULT_SKU = GW_SKU.GW_PS_NET_ENG3_SR_ARCHITECT;
    private static final GW_SKU UPDATED_SKU = GW_SKU.GW_PS_NET_ENG2_ARCHITECT;

    private static final Float DEFAULT_COST = 1F;
    private static final Float UPDATED_COST = 2F;

    @Autowired
    private GW_SKU_COSTRepository gW_SKU_COSTRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGW_SKU_COSTMockMvc;

    private GW_SKU_COST gW_SKU_COST;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GW_SKU_COSTResource gW_SKU_COSTResource = new GW_SKU_COSTResource(gW_SKU_COSTRepository);
        this.restGW_SKU_COSTMockMvc = MockMvcBuilders.standaloneSetup(gW_SKU_COSTResource)
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
    public static GW_SKU_COST createEntity(EntityManager em) {
        GW_SKU_COST gW_SKU_COST = new GW_SKU_COST()
            .sku(DEFAULT_SKU)
            .cost(DEFAULT_COST);
        return gW_SKU_COST;
    }

    @Before
    public void initTest() {
        gW_SKU_COST = createEntity(em);
    }

    @Test
    @Transactional
    public void createGW_SKU_COST() throws Exception {
        int databaseSizeBeforeCreate = gW_SKU_COSTRepository.findAll().size();

        // Create the GW_SKU_COST
        restGW_SKU_COSTMockMvc.perform(post("/api/gw-sku-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gW_SKU_COST)))
            .andExpect(status().isCreated());

        // Validate the GW_SKU_COST in the database
        List<GW_SKU_COST> gW_SKU_COSTList = gW_SKU_COSTRepository.findAll();
        assertThat(gW_SKU_COSTList).hasSize(databaseSizeBeforeCreate + 1);
        GW_SKU_COST testGW_SKU_COST = gW_SKU_COSTList.get(gW_SKU_COSTList.size() - 1);
        assertThat(testGW_SKU_COST.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testGW_SKU_COST.getCost()).isEqualTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    public void createGW_SKU_COSTWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gW_SKU_COSTRepository.findAll().size();

        // Create the GW_SKU_COST with an existing ID
        gW_SKU_COST.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGW_SKU_COSTMockMvc.perform(post("/api/gw-sku-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gW_SKU_COST)))
            .andExpect(status().isBadRequest());

        // Validate the GW_SKU_COST in the database
        List<GW_SKU_COST> gW_SKU_COSTList = gW_SKU_COSTRepository.findAll();
        assertThat(gW_SKU_COSTList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGW_SKU_COSTS() throws Exception {
        // Initialize the database
        gW_SKU_COSTRepository.saveAndFlush(gW_SKU_COST);

        // Get all the gW_SKU_COSTList
        restGW_SKU_COSTMockMvc.perform(get("/api/gw-sku-costs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gW_SKU_COST.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU.toString())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())));
    }

    @Test
    @Transactional
    public void getGW_SKU_COST() throws Exception {
        // Initialize the database
        gW_SKU_COSTRepository.saveAndFlush(gW_SKU_COST);

        // Get the gW_SKU_COST
        restGW_SKU_COSTMockMvc.perform(get("/api/gw-sku-costs/{id}", gW_SKU_COST.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gW_SKU_COST.getId().intValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGW_SKU_COST() throws Exception {
        // Get the gW_SKU_COST
        restGW_SKU_COSTMockMvc.perform(get("/api/gw-sku-costs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGW_SKU_COST() throws Exception {
        // Initialize the database
        gW_SKU_COSTRepository.saveAndFlush(gW_SKU_COST);
        int databaseSizeBeforeUpdate = gW_SKU_COSTRepository.findAll().size();

        // Update the gW_SKU_COST
        GW_SKU_COST updatedGW_SKU_COST = gW_SKU_COSTRepository.findOne(gW_SKU_COST.getId());
        // Disconnect from session so that the updates on updatedGW_SKU_COST are not directly saved in db
        em.detach(updatedGW_SKU_COST);
        updatedGW_SKU_COST
            .sku(UPDATED_SKU)
            .cost(UPDATED_COST);

        restGW_SKU_COSTMockMvc.perform(put("/api/gw-sku-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGW_SKU_COST)))
            .andExpect(status().isOk());

        // Validate the GW_SKU_COST in the database
        List<GW_SKU_COST> gW_SKU_COSTList = gW_SKU_COSTRepository.findAll();
        assertThat(gW_SKU_COSTList).hasSize(databaseSizeBeforeUpdate);
        GW_SKU_COST testGW_SKU_COST = gW_SKU_COSTList.get(gW_SKU_COSTList.size() - 1);
        assertThat(testGW_SKU_COST.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testGW_SKU_COST.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    public void updateNonExistingGW_SKU_COST() throws Exception {
        int databaseSizeBeforeUpdate = gW_SKU_COSTRepository.findAll().size();

        // Create the GW_SKU_COST

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGW_SKU_COSTMockMvc.perform(put("/api/gw-sku-costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gW_SKU_COST)))
            .andExpect(status().isCreated());

        // Validate the GW_SKU_COST in the database
        List<GW_SKU_COST> gW_SKU_COSTList = gW_SKU_COSTRepository.findAll();
        assertThat(gW_SKU_COSTList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGW_SKU_COST() throws Exception {
        // Initialize the database
        gW_SKU_COSTRepository.saveAndFlush(gW_SKU_COST);
        int databaseSizeBeforeDelete = gW_SKU_COSTRepository.findAll().size();

        // Get the gW_SKU_COST
        restGW_SKU_COSTMockMvc.perform(delete("/api/gw-sku-costs/{id}", gW_SKU_COST.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GW_SKU_COST> gW_SKU_COSTList = gW_SKU_COSTRepository.findAll();
        assertThat(gW_SKU_COSTList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GW_SKU_COST.class);
        GW_SKU_COST gW_SKU_COST1 = new GW_SKU_COST();
        gW_SKU_COST1.setId(1L);
        GW_SKU_COST gW_SKU_COST2 = new GW_SKU_COST();
        gW_SKU_COST2.setId(gW_SKU_COST1.getId());
        assertThat(gW_SKU_COST1).isEqualTo(gW_SKU_COST2);
        gW_SKU_COST2.setId(2L);
        assertThat(gW_SKU_COST1).isNotEqualTo(gW_SKU_COST2);
        gW_SKU_COST1.setId(null);
        assertThat(gW_SKU_COST1).isNotEqualTo(gW_SKU_COST2);
    }
}
