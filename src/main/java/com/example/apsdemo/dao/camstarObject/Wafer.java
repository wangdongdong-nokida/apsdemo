package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.WaferData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "L_BH", catalog = "")
public class Wafer extends WaferData {
    @JsonIgnore
    @OneToMany(targetEntity = WaferProduct.class,mappedBy = "wafer")
    private Set<WaferProduct> wafer_productBases;

    @JsonIgnore
    @OneToMany(targetEntity = WaferFather.class,mappedBy ="positionFirst" )
    private Set<WaferFather> fathersFirset;

    @JsonIgnore
    @OneToMany(targetEntity = WaferFather.class,mappedBy ="positionSecond" )
    private Set<WaferFather> fathersSecond;

    @JsonIgnore
    @OneToMany(targetEntity = WaferFather.class,mappedBy ="positionThird" )
    private Set<WaferFather> fathersThird;

    @JsonIgnore
    @OneToMany(targetEntity = WaferFather.class,mappedBy ="positionFourth" )
    private Set<WaferFather> fathersForth;

    public Set<WaferFather> getFathersFirset() {
        return fathersFirset;
    }

    public void setFathersFirset(Set<WaferFather> fathersFirset) {
        this.fathersFirset = fathersFirset;
    }

    public Set<WaferFather> getFathersSecond() {
        return fathersSecond;
    }

    public void setFathersSecond(Set<WaferFather> fathersSecond) {
        this.fathersSecond = fathersSecond;
    }

    public Set<WaferFather> getFathersThird() {
        return fathersThird;
    }

    public void setFathersThird(Set<WaferFather> fathersThird) {
        this.fathersThird = fathersThird;
    }

    public Set<WaferFather> getFathersForth() {
        return fathersForth;
    }

    public void setFathersForth(Set<WaferFather> fathersForth) {
        this.fathersForth = fathersForth;
    }

    public Set<WaferProduct> getWafer_productBases() {
        return wafer_productBases;
    }

    public void setWafer_productBases(Set<WaferProduct> wafer_productBases) {
        this.wafer_productBases = wafer_productBases;
    }
}
