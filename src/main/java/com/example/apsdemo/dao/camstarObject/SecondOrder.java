package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.businessObject.TestScribingCenter;
import com.example.apsdemo.dao.camstarData.SecondOrderData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "L_RJRW", catalog = "")
public class SecondOrder extends SecondOrderData {

    @JoinColumn(name = "CPLX")
    @ManyToOne(targetEntity = ProductType.class,fetch = FetchType.EAGER)
    private ProductType productType;

    @JsonIgnore
    @OneToMany(targetEntity = TestScribingCenter.class,mappedBy = "secondOrder")
    private Set<TestScribingCenter> testScribingCenters;

    public Set<TestScribingCenter> getTestScribingCenters() {
        return testScribingCenters;
    }

    public void setTestScribingCenters(Set<TestScribingCenter> testScribingCenters) {
        this.testScribingCenters = testScribingCenters;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
