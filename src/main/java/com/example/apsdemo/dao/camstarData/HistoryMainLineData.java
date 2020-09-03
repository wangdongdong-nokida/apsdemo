package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class HistoryMainLineData {
    @Column(name = "HISTORYMAINLINEID")
    private String ID;
}
