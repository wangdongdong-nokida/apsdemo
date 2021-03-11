/*
 * @author     ：root
 * @date       ：Created in 2021/3/2 17:48
 */
package com.example.apsdemo.listener;

import com.example.apsdemo.dao.businessObject.ScheduleTestItem;
import com.example.apsdemo.dao.camstarObject.SecondOrder;
import com.example.apsdemo.utils.AspectLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

public class ScheduleTestItemListener {
    private static final Logger log = LoggerFactory.getLogger(AspectLogger.class);

    @PrePersist
    public void PrePersist(ScheduleTestItem entity) {
        log.info("开始保存--" + entity.toString());
        setCreatedTeamNames(entity, false,true);
    }

    private void setCreatedTeamNames(ScheduleTestItem entity, boolean delete, boolean create) {
        SecondOrder secondOrder = entity.getSecondOrder();
        if (secondOrder != null) {
            Set<ScheduleTestItem> items = secondOrder.getScheduleTestItems();
            StringBuilder stringBuilder = new StringBuilder();

            if (create && entity.getTeamName() != null && stringBuilder.lastIndexOf(entity.getTeamName()) < 0) {
                stringBuilder.append(entity.getTeamName()).append(";");
            }
            for (ScheduleTestItem item : items) {
                if (delete && item.getID() == entity.getID()) {
                    continue;
                }
                if (item.getTeamName() != null && stringBuilder.lastIndexOf(item.getTeamName()) < 0) {
                    stringBuilder.append(item.getTeamName()).append(";");
                }
            }
            secondOrder.setCreatedTeamName(stringBuilder.toString());
        }
    }

    @PreUpdate
    public void PreUpdate(ScheduleTestItem entity) {
//        setCreatedTeamNames(entity, false,false);
        log.info("开始更新--" + entity.toString());
    }

    @PreRemove
    public void PreRemove(ScheduleTestItem entity) {
        setCreatedTeamNames(entity, true,false);
        log.info("开始删除--" + entity.toString());
    }

    @PostPersist
    public void PostPersist(Object entity) {
        log.info("结束保存--" + entity.toString());
    }

    @PostUpdate
    public void PostUpdate(Object entity) {
        log.info("结束更新--" + entity.toString());
    }

    @PostRemove
    public void PostRemove(Object entity) {
        log.info("结束删除--" + entity.toString());
    }

}
