package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.ProductData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Product extends ProductData {

    @JsonIgnore
    @OneToMany(targetEntity = WaferProduct.class,mappedBy = "product")
    private Set<WaferProduct> wafer_productBases;

    @ManyToOne(targetEntity = ProductBase.class)
    @JoinColumn(name = "PRODUCTBASEID")
    private ProductBase productBase;

    @ManyToOne(targetEntity = CircuitType.class)
    @JoinColumn(name = "DLLXID")
    private CircuitType circuitType;


    @ManyToOne(targetEntity = Employee.class)
    @JoinColumn(name = "SJSID")
    private Employee designer;

    public Set<WaferProduct> getWafer_productBases() {
        return wafer_productBases;
    }

    public void setWafer_productBases(Set<WaferProduct> wafer_productBases) {
        this.wafer_productBases = wafer_productBases;
    }

    public ProductBase getProductBase() {
        return productBase;
    }

    public void setProductBase(ProductBase productBase) {
        this.productBase = productBase;
    }

    public CircuitType getCircuitType() {
        return circuitType;
    }

    public void setCircuitType(CircuitType circuitType) {
        this.circuitType = circuitType;
    }

    public Employee getDesigner() {
        return designer;
    }

    public void setDesigner(Employee designer) {
        this.designer = designer;
    }
}
