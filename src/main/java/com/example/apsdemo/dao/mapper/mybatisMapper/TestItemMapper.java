package com.example.apsdemo.dao.mapper.mybatisMapper;

import com.example.apsdemo.dao.dto.FileDto;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Blob;
import java.util.List;
import java.util.Map;

@Repository
public interface TestItemMapper {

    @Select("select distinct l_yjrwname, pb.productname, y.bh, to_char(y.dhrq,'YYYY-MM-DD hh24:mi') dhrq, y.dhsl, y.dlxh, y.hbbj, to_char(y.jjsj,'YYYY-MM-DD hh24:mi') jjsj, " +
            "to_char(y.jssj,'YYYY-MM-DD hh24:mi') jssj, to_char(y.kssj,'YYYY-MM-DD hh24:mi') kssj, y.l_yjrwid id, y.lpbz, y.lpdh, y.lpsl, y.sfjj, y.sflp, y.sjsbz, y.sjssh, " +
            "y.wczt, y.flmc, y.khmc, y.ps, y.sfdh, y.xqsl, to_char(y.yfhrq,'YYYY-MM-DD hh24:mi') yfhrq, y.dhbz, y.bz, to_char(y.jhjq,'YYYY-MM-DD hh24:mi') jhjq " +
            "from L_YJRW y " +
            "left join PRODUCT p on p.productid = y.cpxhid " +
            "left join PRODUCTBASE pb on pb.productbaseid = p.productbaseid " +
            "inner join l_rjrw r on r.l_rjrwid = y.l_rjrwid " +
            "where 1=1 and r.l_rjrwid = #{secondOrderId} " +
            "order by l_yjrwname desc")
    @ResultType(java.util.Map.class)
    List<Map<String,Object>> findYjrwBySecondOrderId(String secondOrderId);

    @Select("select dd.bh, dd.bz, dd.cqfj, to_char(dd.ddjsrq,'YYYY-MM-DD hh24:mi') ddjsrq, ht.ddlx, dd.ddsl, dd.ddzt, " +
            "dd.dgsl, dd.fhsl, ht.gch, ht.l_htname hth, dd.htsm, to_char(dd.jfqx,'YYYY-MM-DD hh24:mi') jfqx, dd.jjbz, " +
            "to_char(dd.jjsj,'YYYY-MM-DD hh24:mi') jjsj, dd.jldwid, dd.jzdw, to_char(dd.jzsj,'YYYY-MM-DD hh24:mi') jzsj, dd.khjc, dd.l_ddid id, dd.l_ddname, " +
            "ht. kh l_khname, p.productid l_xhid, ht.lxdh, ht.lxr, to_char(ht.qdrq,'YYYY-MM-DD hh24:mi') qdrq, dd.qrbz, dd.sfdpa, " +
            "dd.sfecsx, dd.sffkzdd, dd.sfjj, dd.sfjz, dd.sfxp, dd.sfys, dd.sfzdgc, dd.tcqps, " +
            "tgfs.l_tgfsname tgfs, dd.tgxgcl, dd.xh, dd.xqsl, ht.xslx, xspq.l_xspqname xspq, " +
            "dd.ycdhs, to_char(dd.yfhrq,'YYYY-MM-DD hh24:mi') yfhrq, dd.yhdcs, dd.yhdjs, dd.yjrw_id, dd.ypygs, dd.ysdw, " +
            "dd.yxj, dd.zbdw, zldj.l_zldjname zldj, dd.zxbz, dd.sfxy, to_char(dd.yssj,'YYYY-MM-DD hh24:mi') yssj, pb.productname XH, " +
            "uom.uomname, fac.factoryname BM, yjrw.L_YJRWNAME rwh, yjrw.lpsl, yjrw.sflp, to_char(yjrw.jhjq,'YYYY-MM-DD hh24:mi') jhjq " +
            "from L_DD dd " +
            "left join L_HT ht on ht.l_htid = dd.l_htid " +
            "left join PRODUCT p on p.productid= dd.cpxhid " +
            "left join productbase pb on pb.productbaseid = p.productbaseid " +
            "left join UOM uom on uom.uomid = dd.Jldwid left " +
            "join FACTORY fac on fac.factoryid = dd.fzbmid " +
            "left join L_KH kh on kh.l_Khid = ht.l_khid " +
            "inner join L_YJRW yjrw on yjrw.L_YJRWID = dd.L_YJRWID " +
            "left join l_xspq xspq on xspq.l_xspqid = ht.l_xspqid " +
            "left join l_tgfs tgfs on tgfs.l_tgfsid = dd.tgfs " +
            "left join l_zldj zldj on zldj.l_zldjid = dd.zldj " +
            "where yjrw.l_yjrwid = #{yjrwId}")
    @ResultType(java.util.Map.class)
    List<Map<String,Object>> findSalesOrderByYjrwId(String yjrwId);

    @Select("SELECT IMG bytes FROM L_BH BH WHERE BH.L_BHNAME = #{bhName}")
    @ResultType(java.lang.Byte.class)
    FileDto findImgByName(String bhName);

    @Select("SELECT BH.L_BHID ID, BH.L_BHNAME,LD.L_DLLXNAME," +
            "BH.PFZR," +
            "BH.DYS," +
            "to_char(BH.RQ,'YYYY-MM-DD hh24:mi:ss') RQ," +
            "BH.HPFS," +
            "XH.DLXH," +
            "XH.DLMC," +
            "XH.DLID," +
            "XH.PRODUCTID AS PRODUCTNAME," +
            "XH.BB," +
            "XH.JBB," +
            "XH.SL," +
            "XH.SJS," +
            "XH.LPCC_X || 'X' || XH.LPCC_Y LPCC," +
            "XH.BZCC_X || 'X' || XH.BZCC_Y BZCC," +
            "XH.BZ," +
            "XH.SFSY," +
            "BH.YPCC," +
            "BH.DYCC_X || 'X' || BH.DYCC_Y DYCC," +
            "BH.HPJJ," +
            "BH.PH " +
            "FROM L_BH BH " +
            "LEFT JOIN L_XH XH ON XH.L_BHID = BH.L_BHID LEFT JOIN L_DLLX LD " +
            "ON LD.L_DLLXID = XH.L_DLLXID " +
            "WHERE 1 = 1 " +
            "And L_BHNAME =  #{bhName}" +
             "order by to_number(xh.dlxh)")
    @ResultType(java.util.Map.class)
    List<Map<String,Object>> findBhInfoByName(String bhName);
}
