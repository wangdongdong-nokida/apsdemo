package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.WaferProductData;
import com.example.apsdemo.dao.mapper.WaferProductMapper;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.WaferProductService;
import com.example.apsdemo.utils.Tools;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "L_XH", catalog = "")
public class WaferProduct extends WaferProductData {

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "PRODUCTID")
    private Product product;

    @ManyToOne(targetEntity = Wafer.class)
    @JoinColumn(name = "L_BHID")
    private Wafer wafer;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Wafer getWafer() {
        return wafer;
    }

    public void setWafer(Wafer wafer) {
        this.wafer = wafer;
    }
}
