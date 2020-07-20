package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.SecondOrderData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "L_RJRW", catalog = "")
public class SecondOrder extends SecondOrderData {

    @JoinColumn(name = "CPLX")
    @ManyToOne(targetEntity = ProductType.class,fetch = FetchType.EAGER)
    private ProductType productType;

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
