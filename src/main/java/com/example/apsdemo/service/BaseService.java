package com.example.apsdemo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;


public abstract class BaseService<T extends JpaSpecificationExecutor& JpaRepository> {

    abstract T getMapper();

    public Page findAll(Specification<T> specification, Pageable pageable){
       return getMapper().findAll(specification,pageable);
    }

    public List findAll(Specification<T> specification){
        return getMapper().findAll(specification);
    }

    public List findAll(){
        return getMapper().findAll();
    }

    public Optional findById(Object id){
        return getMapper().findById(id);
    }

    public List findAll(List ids){
        return getMapper().findAllById(ids);
    }

    public void save(Object d){
        getMapper().save(d);
    }

    public void saveAndFlush(Object d){
        getMapper().saveAndFlush(d);
    }

    public void saveAll(Iterable iterable){
        getMapper().saveAll(iterable);
    }

    public void deleteAll(Iterable iterable){
        getMapper().deleteAll(iterable);

    }

    public void delete(Object object){
        getMapper().delete(object);
    }



}
