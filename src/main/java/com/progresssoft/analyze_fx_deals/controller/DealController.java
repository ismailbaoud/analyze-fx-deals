package com.progresssoft.analyze_fx_deals.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// import com.progresssoft.analyze_fx_deals.dto.ResponseDTO;
import com.progresssoft.analyze_fx_deals.model.Deal;

import com.progresssoft.analyze_fx_deals.service.DealService;

@RestController
@RequestMapping("/api/v1/deals")
public class DealController {
    
    @Autowired
    private DealService dealService;

    @PostMapping("/import")
    public ResponseEntity<List<Deal>> importDealsFile(@RequestParam("file") MultipartFile file) throws Exception{
        List<Deal> deals = dealService.importDeals(file);
        return ResponseEntity.ok().body(deals);
    }

    @GetMapping
    public ResponseEntity<List<Deal>> getAllDeals() {
        List<Deal> deals = dealService.getAllDeals();
        return ResponseEntity.ok().body(deals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deal> getDealById(@PathVariable String id) {
        Deal deal = dealService.getDealById(id);
        return ResponseEntity.ok().body(deal);
    }

}
