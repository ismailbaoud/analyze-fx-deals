package com.progresssoft.analyze_fx_deals.mapper;


import org.mapstruct.Mapper;

import com.progresssoft.analyze_fx_deals.dto.RequestDTO;
import com.progresssoft.analyze_fx_deals.model.Deal;

@Mapper(componentModel = "spring")
public interface DealMapper {
    RequestDTO toDto(Deal d);
    Deal toEntity(RequestDTO rDto);
}
