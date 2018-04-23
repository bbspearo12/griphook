package com.groupware.griphook.repository;

import com.groupware.griphook.domain.GW_SKU_COST;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the GW_SKU_COST entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GW_SKU_COSTRepository extends JpaRepository<GW_SKU_COST, Long> {

}
