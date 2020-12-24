package com.example.apsdemo.dao.mapper.mybatisMapper;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TestSchedulingMapper {


    @Select("select r.l_Rjrwname ejrwh,r.xh,r.bh,r.jb,r.rwzt,r.csbz,r.csbz,r.hpbz,to_char(r.yqcswcrq,'YYYY-MM-DD') yqcswcrq," +
            "to_char(r.yqhpwcsj,'YYYY-MM-DD') yqhpwcrq,r.ddsl, (select employeename from employee where employeeid = r.sqrid) sqr, " +
            "to_char(r.sqsj,'YYYY-MM-DD') sqsj,r.sqbz,r.sftk,r.tkrq,r.klckrq,r.rwlx, (select producttypename from producttype " +
            "where producttypeid = r.cplx) cplx,r.bz,r.rwly from L_RJRW r where r.L_RJRWNAME = #{secondOrderName}")
    @ResultType(java.util.Map.class)
    List<Map<String,Object>> querySecondOrderInfoByName(String secondOrderName);

}
