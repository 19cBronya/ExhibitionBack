package com.cuit.business.exhibition.domain.dto;

import com.cuit.business.exhibition.domain.Exhibition;
import lombok.Data;

@Data
public class ExhibitionOrder extends Exhibition {

    /*票数*/
    private Integer ticketCount;
}
