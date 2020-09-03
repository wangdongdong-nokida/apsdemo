package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.SalesOrderData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "L_DD",catalog = "")
public class SalesOrder extends SalesOrderData {


    private Set<Occupy> occupies=new HashSet<>();

    @JsonIgnore
    @OneToMany(targetEntity = Occupy.class,mappedBy ="salesOrder")
    public Set<Occupy> getOccupies() {
        return occupies;
    }

    public void setOccupies(Set<Occupy> occupies) {
        this.occupies = occupies;
    }
}
