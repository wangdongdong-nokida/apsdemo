package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.SecondOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecondOrderMapper extends JpaRepository<SecondOrder,String>, JpaSpecificationExecutor {

    @Query(value = "from SecondOrder s left  join ScheduleTestItem i on s.ID=i.secondOrder.ID")
    public List<SecondOrder> findByParams();
}
