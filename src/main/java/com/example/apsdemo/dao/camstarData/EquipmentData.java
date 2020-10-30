package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;


@Data
@MappedSuperclass
public class EquipmentData {

    @Id
    @Column(name = "RESOURCEGROUPID")
    protected String ID;
    @Column(name = "DESCRIPTION")
    protected String description;
    @Column(name = "RESOURCEGROUPNAME")
    protected String name;
    @Column(name = "XTZT")
    protected String status;
    @Column(name = "GD")
    protected String type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EquipmentData)) return false;
        EquipmentData that = (EquipmentData) o;
        return Objects.equals(getID(), that.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }
}
