package com.cuit.business.position.domain;

import lombok.Data;

@Data
public class City {
    private Integer id;

    private Integer code;

    private String name;

    private Integer pid;

    private String state;
}
