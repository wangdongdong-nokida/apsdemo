package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.ProductTypeData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PRODUCTTYPE")
public class ProductType extends ProductTypeData {
    @JsonIgnore
    @OneToMany(targetEntity = SecondOrder.class,fetch = FetchType.LAZY,mappedBy = "productType")
    private Set<SecondOrder> secondOrders=new HashSet<>();

    public Set<SecondOrder> getSecondOrders() {
        return secondOrders;
    }

    public void setSecondOrders(Set<SecondOrder> secondOrders) {
        this.secondOrders = secondOrders;
    }
}
