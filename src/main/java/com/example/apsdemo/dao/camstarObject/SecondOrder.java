package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.businessObject.ScheduleTestItem;
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
    @OneToMany(targetEntity = ScheduleTestItem.class,mappedBy = "secondOrder")
    private Set<ScheduleTestItem> scheduleTestItems;

    @JoinColumn(name ="sqrid" )
    @ManyToOne(targetEntity = Employee.class)
    private Employee employee;

    @JoinColumn(name ="cjrid" )
    @ManyToOne(targetEntity = Employee.class)
    private Employee cjr;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @JsonIgnore
    @OneToMany(targetEntity = TestScribingCenter.class,mappedBy = "secondOrder")
    private Set<TestScribingCenter> testScribingCenters;

    public Set<ScheduleTestItem> getScheduleTestItems() {
        return scheduleTestItems;
    }

    public void setScheduleTestItems(Set<ScheduleTestItem> scheduleTestItems) {
        this.scheduleTestItems = scheduleTestItems;
    }

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

    public Employee getCjr() {
        return cjr;
    }

    public void setCjr(Employee cjr) {
        this.cjr = cjr;
    }
}
