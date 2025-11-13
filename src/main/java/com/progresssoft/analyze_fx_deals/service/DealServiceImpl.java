package com.progresssoft.analyze_fx_deals.service;

import java.util.List;

import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.progresssoft.analyze_fx_deals.dto.ResponseDTO;
import com.progresssoft.analyze_fx_deals.model.Deal;
// import com.progresssoft.analyze_fx_deals.repository.DealRepository;

@Service
public class DealServiceImpl implements DealService {

    // @Autowired
    // private DealRepository dealRepository;

    @Override
    public void importDeals(MultipartFile file) {
        throw new IllegalStateException("Unimplemented method 'importDeals'");
    }

    @Override
    public void saveDral(Deal d) {
        throw new IllegalStateException("Unimplemented method 'saveDral'");
    }

    @Override
    public List<Deal> getAllDeals() {
        throw new IllegalStateException("Unimplemented method 'getAllDeals'");
    }

    @Override
    public Deal getDealById(String id) {
        throw new IllegalStateException("Unimplemented method 'getDealById'");
    }
    
}
