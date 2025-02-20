package com.cuit.business.exhibitionExhibitor.domain;
import com.cuit.system.base.BaseEntity;
import lombok.Data;


@Data
public class ExhibitionExhibitor extends BaseEntity {

    /*展览馆主键id*/
    private Long hid;

    /*展览会主键id*/
    private Long eid;

    /*参展商主键id*/
    private Long etorid;
}
