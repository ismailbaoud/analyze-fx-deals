package com.progresssoft.analyze_fx_deals.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.progresssoft.analyze_fx_deals.dto.RequestDTO;
import com.progresssoft.analyze_fx_deals.model.Deal;
import com.progresssoft.analyze_fx_deals.repository.DealRepository;
import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class DealServiceImpl implements DealService {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private Validator validator;


    @Override
    public List<Deal> importDeals(MultipartFile file) throws IOException{

        List<Deal> deals = new ArrayList<>();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(file.getInputStream()));

        String line;
        boolean firstLine = true;

        while ((line = bReader.readLine()) != null) {
            if(firstLine) {
                firstLine = false;
                continue;
            }

            String[] fields = line.split(",");
            if(fields.length != 5) {
                throw new IllegalArgumentException("Invalid row format");
            }

            RequestDTO rDto = new RequestDTO();
            rDto.setDealUniqueId(fields[0]);
            rDto.setFromCurrencyIsoCode(fields[1]);
            rDto.setToCurrencyIsoCode(fields[2]);
            rDto.setDealTimestamp(LocalDateTime.parse(fields[3]));
            rDto.setDealAmount(new BigDecimal(fields[4]));

            Set<ConstraintViolation<RequestDTO>> violations = validator.validate(rDto);
            if (!violations.isEmpty()) {
                String errorMsg = violations.stream()
                                            .map(ConstraintViolation::getMessage)
                                            .collect(Collectors.joining(", "));
                String msg = "Error in Deal with id : " + rDto.getDealUniqueId() + " " + errorMsg;
                log.warn(msg);
                continue;
            }

            if (dealRepository.existsByDealUniqueId(rDto.getDealUniqueId())) {
                String msg = "Deal with id " + rDto.getDealUniqueId() + " already exists";
                log.warn(msg);
                continue;
            }

            // Manual mapping instead of mapper
            Deal deal = new Deal();
            deal.setDealUniqueId(rDto.getDealUniqueId());
            deal.setFromCurrencyIsoCode(rDto.getFromCurrencyIsoCode());
            deal.setToCurrencyIsoCode(rDto.getToCurrencyIsoCode());
            deal.setDealTimestamp(rDto.getDealTimestamp());
            deal.setDealAmount(rDto.getDealAmount());

            saveDeal(deal);
            String msg = "Deal with id : " + deal.getDealUniqueId() + " created successfully";
            log.info(msg);
            deals.add(deal);
        }
        
        bReader.close();
        return deals;
    }

    @Override
    public void saveDeal(Deal d) { 
        dealRepository.save(d);
    }

    @Override
    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal getDealById(String id) {
        return dealRepository.getDealByDealUniqueId(id);
    }

}
