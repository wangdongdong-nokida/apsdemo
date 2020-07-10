package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.WaferFatherData;

import javax.persistence.*;

@Entity
@Table(name = "L_FBH", catalog = "")
public class WaferFather extends WaferFatherData {
    @ManyToOne (targetEntity = Wafer.class)
    @JoinColumn(name = "BH1ID")
    private Wafer positionFirst;

    @ManyToOne(targetEntity = Wafer.class)
    @JoinColumn(name = "BH2ID")
    private Wafer positionSecond;

    @ManyToOne(targetEntity = Wafer.class)
    @JoinColumn(name = "BH3ID")
    private Wafer positionThird;

    @ManyToOne(targetEntity = Wafer.class)
    @JoinColumn(name = "BH4ID")
    private Wafer positionFourth;


    public Wafer getPositionFirst() {
        return positionFirst;
    }

    public void setPositionFirst(Wafer positionFirst) {
        this.positionFirst = positionFirst;
    }

    public Wafer getPositionSecond() {
        return positionSecond;
    }

    public void setPositionSecond(Wafer positionSecond) {
        this.positionSecond = positionSecond;
    }

    public Wafer getPositionThird() {
        return positionThird;
    }

    public void setPositionThird(Wafer positionThird) {
        this.positionThird = positionThird;
    }

    public Wafer getPositionFourth() {
        return positionFourth;
    }

    public void setPositionFourth(Wafer positionFourth) {
        this.positionFourth = positionFourth;
    }
}
