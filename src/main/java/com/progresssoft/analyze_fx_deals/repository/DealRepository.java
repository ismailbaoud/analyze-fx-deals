package com.progresssoft.analyze_fx_deals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.progresssoft.analyze_fx_deals.model.Deal;
@Repository
public interface DealRepository extends JpaRepository<Deal,String> {
}