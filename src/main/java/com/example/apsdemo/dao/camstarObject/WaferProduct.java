package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.WaferProductData;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "L_XH", catalog = "")
public class WaferProduct extends WaferProductData {

//    @ManyToOne(targetEntity = Product.class)
//    @JoinColumn(name = "PRODUCTID")
//    private Product product;

    @ManyToOne(targetEntity = Wafer.class)
    @JoinColumn(name = "L_BHID")
    private Wafer wafer;

//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }

    public Wafer getWafer() {
        return wafer;
    }

    public void setWafer(Wafer wafer) {
        this.wafer = wafer;
    }
}
