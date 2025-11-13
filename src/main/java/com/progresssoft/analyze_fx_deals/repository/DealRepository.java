package com.progresssoft.analyze_fx_deals.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.progresssoft.analyze_fx_deals.model.Deal;

public interface DealRepository extends JpaRepository<String,Deal> {
}