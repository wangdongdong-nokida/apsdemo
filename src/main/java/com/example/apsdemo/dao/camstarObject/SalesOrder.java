package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.businessObject.PickingOrder;
import com.example.apsdemo.dao.camstarData.SalesOrderData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "L_DD",catalog = "")
public class SalesOrder extends SalesOrderData {

    private Set<Occupy> occupies=new HashSet<>();

    private LHt lHt;

    private LTgfs lTgfs;


    private Set<PickingOrder> pickingOrders=new HashSet<>();

    @JsonIgnore
    @JoinColumn(name = "bindSalesOrderID",foreignKey = @ForeignKey(name = "",value = ConstraintMode.NO_CONSTRAINT))
    @OneToMany(targetEntity = PickingOrder.class)
    public Set<PickingOrder> getPickingOrders() {
        return pickingOrders;
    }

    public void setPickingOrders(Set<PickingOrder> pickingOrders) {
        this.pickingOrders = pickingOrders;
    }

    @JoinColumn(name = "tgfs",foreignKey = @ForeignKey(name="null",value = ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(targetEntity = LTgfs.class)
    public LTgfs getlTgfs() {
        return lTgfs;
    }

    public void setlTgfs(LTgfs lTgfs) {
        this.lTgfs = lTgfs;
    }

    @JoinColumn(name = "L_HTID",foreignKey = @ForeignKey(name="null",value = ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(targetEntity = LHt.class)
    public LHt getlHt() {
        return lHt;
    }

    public void setlHt(LHt lHt) {
        this.lHt = lHt;
    }

    @JsonIgnore
    @OneToMany(targetEntity = Occupy.class,mappedBy ="salesOrder")
    public Set<Occupy> getOccupies() {
        return occupies;
    }

    public void setOccupies(Set<Occupy> occupies) {
        this.occupies = occupies;
    }
}
