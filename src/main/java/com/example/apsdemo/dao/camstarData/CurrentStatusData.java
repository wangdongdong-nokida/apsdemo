package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class CurrentStatusData {
    @Id
    @Column(name = "CURRENTSTATUSID")
    private String ID;
}
