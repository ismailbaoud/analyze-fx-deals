package com.progresssoft.analyze_fx_deals.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.progresssoft.analyze_fx_deals.dto.RequestDTO;
import com.progresssoft.analyze_fx_deals.model.Deal;
// import com.progresssoft.analyze_fx_deals.repository.DealRepository;

@Service
public class DealServiceImpl implements DealService {

    // @Autowired
    // private DealRepository dealRepository;

    @Override
    public List<RequestDTO> importDeals(MultipartFile file) throws IOException{

        List<RequestDTO> deals = new ArrayList<>();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(file.getInputStream()));

        String line;
        boolean firstLine = false;

        while ((line = bReader.readLine()) != null) {
            if(firstLine) {
                firstLine = false;
                continue;
            }

            String[] fields = line.split(",");
            if(fields.length != 5) {
                throw new IllegalArgumentException("INvalide row format");
            }

            RequestDTO rDto = new RequestDTO();
            rDto.setDealUniqueId(fields[0]);
            rDto.setFromCurrencyIsoCode(fields[1]);
            rDto.setToCurrencyIsoCode(fields[2]);
            rDto.setDealTimestamp(LocalDateTime.parse(fields[3]));
            rDto.setDealAmount(new BigDecimal(fields[4]));

            deals.add(rDto);
        }
        return deals;
    }

    @Override
    public void saveDeal(Deal d) {
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
