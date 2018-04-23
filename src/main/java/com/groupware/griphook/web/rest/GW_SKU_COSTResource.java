package com.groupware.griphook.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.groupware.griphook.domain.GW_SKU_COST;

import com.groupware.griphook.repository.GW_SKU_COSTRepository;
import com.groupware.griphook.web.rest.errors.BadRequestAlertException;
import com.groupware.griphook.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GW_SKU_COST.
 */
@RestController
@RequestMapping("/api")
public class GW_SKU_COSTResource {

    private final Logger log = LoggerFactory.getLogger(GW_SKU_COSTResource.class);

    private static final String ENTITY_NAME = "gW_SKU_COST";

    private final GW_SKU_COSTRepository gW_SKU_COSTRepository;

    public GW_SKU_COSTResource(GW_SKU_COSTRepository gW_SKU_COSTRepository) {
        this.gW_SKU_COSTRepository = gW_SKU_COSTRepository;
    }

    /**
     * POST  /gw-sku-costs : Create a new gW_SKU_COST.
     *
     * @param gW_SKU_COST the gW_SKU_COST to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gW_SKU_COST, or with status 400 (Bad Request) if the gW_SKU_COST has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gw-sku-costs")
    @Timed
    public ResponseEntity<GW_SKU_COST> createGW_SKU_COST(@RequestBody GW_SKU_COST gW_SKU_COST) throws URISyntaxException {
        log.debug("REST request to save GW_SKU_COST : {}", gW_SKU_COST);
        if (gW_SKU_COST.getId() != null) {
            throw new BadRequestAlertException("A new gW_SKU_COST cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GW_SKU_COST result = gW_SKU_COSTRepository.save(gW_SKU_COST);
        return ResponseEntity.created(new URI("/api/gw-sku-costs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gw-sku-costs : Updates an existing gW_SKU_COST.
     *
     * @param gW_SKU_COST the gW_SKU_COST to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gW_SKU_COST,
     * or with status 400 (Bad Request) if the gW_SKU_COST is not valid,
     * or with status 500 (Internal Server Error) if the gW_SKU_COST couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gw-sku-costs")
    @Timed
    public ResponseEntity<GW_SKU_COST> updateGW_SKU_COST(@RequestBody GW_SKU_COST gW_SKU_COST) throws URISyntaxException {
        log.debug("REST request to update GW_SKU_COST : {}", gW_SKU_COST);
        if (gW_SKU_COST.getId() == null) {
            return createGW_SKU_COST(gW_SKU_COST);
        }
        GW_SKU_COST result = gW_SKU_COSTRepository.save(gW_SKU_COST);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gW_SKU_COST.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gw-sku-costs : get all the gW_SKU_COSTS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of gW_SKU_COSTS in body
     */
    @GetMapping("/gw-sku-costs")
    @Timed
    public List<GW_SKU_COST> getAllGW_SKU_COSTS() {
        log.debug("REST request to get all GW_SKU_COSTS");
        return gW_SKU_COSTRepository.findAll();
        }

    /**
     * GET  /gw-sku-costs/:id : get the "id" gW_SKU_COST.
     *
     * @param id the id of the gW_SKU_COST to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gW_SKU_COST, or with status 404 (Not Found)
     */
    @GetMapping("/gw-sku-costs/{id}")
    @Timed
    public ResponseEntity<GW_SKU_COST> getGW_SKU_COST(@PathVariable Long id) {
        log.debug("REST request to get GW_SKU_COST : {}", id);
        GW_SKU_COST gW_SKU_COST = gW_SKU_COSTRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gW_SKU_COST));
    }

    /**
     * DELETE  /gw-sku-costs/:id : delete the "id" gW_SKU_COST.
     *
     * @param id the id of the gW_SKU_COST to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gw-sku-costs/{id}")
    @Timed
    public ResponseEntity<Void> deleteGW_SKU_COST(@PathVariable Long id) {
        log.debug("REST request to delete GW_SKU_COST : {}", id);
        gW_SKU_COSTRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
