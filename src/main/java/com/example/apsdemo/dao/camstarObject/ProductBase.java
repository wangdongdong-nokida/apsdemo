package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.ProductBaseData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "PRODUCTBASE",catalog = "")
public class ProductBase extends ProductBaseData {

    @JsonIgnore
    @OneToMany(targetEntity = Product.class,mappedBy = "productBase")
    private Set<Product> products;

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
