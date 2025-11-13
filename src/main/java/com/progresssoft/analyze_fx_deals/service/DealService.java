package com.progresssoft.analyze_fx_deals.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.progresssoft.analyze_fx_deals.model.Deal;

public interface DealService {

    abstract void importDeals(MultipartFile file);

    abstract void saveDral(Deal d);

    abstract List<Deal> getAllDeals();

    abstract Deal getDealById(String id);
}