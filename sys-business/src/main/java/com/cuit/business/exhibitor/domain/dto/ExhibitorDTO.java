package com.cuit.business.exhibitor.domain.dto;

import com.cuit.business.exhibitor.domain.Exhibitor;
import lombok.Data;

@Data
public class ExhibitorDTO extends Exhibitor {
    /**
     * 参展商负责人名
     */
    private String exhibitorName;

    /**
     * 展位编号
     */
    private String boothCode;
}
