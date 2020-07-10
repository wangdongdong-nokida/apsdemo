package com.example.apsdemo.schedule;

import com.example.apsdemo.dao.businessData.ScheduleTaskLineData;
import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.logicSchedule.EquipmentCalendarBitSet;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ScheduleTaskLine extends ScheduleTaskLineData {

    @JsonIgnore
    @OneToOne(targetEntity = Equipment.class,fetch = FetchType.LAZY)
    private Equipment equipment;

    @JsonIgnore
    @OneToMany(targetEntity = ScheduleTask.class,mappedBy = "scheduleTaskLine",fetch = FetchType.EAGER)
    private Set<ScheduleTask> tasks=new HashSet<>();

    public Set<ScheduleTask> getTasks() {
        return tasks;
    }

    public void setTasks(Set<ScheduleTask> tasks) {
        this.tasks = tasks;
    }

    @JsonIgnore
    @OneToOne(targetEntity = ScheduleTask.class,fetch = FetchType.LAZY)
    private ScheduleTask first;
    @JsonIgnore
    @OneToOne(targetEntity = ScheduleTask.class,fetch = FetchType.LAZY)
    private ScheduleTask last;

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public ScheduleTask getFirst() {
        return first;
    }

    public void setFirst(ScheduleTask first) {
        this.first = first;
    }

    public ScheduleTask getLast() {
        return last;
    }

    public void setLast(ScheduleTask last) {
        this.last = last;
    }

    //设置队列的开始时间
    public void updateScheduleDate(EquipmentCalendarBitSet.BitSetWrapper wrapper) {
        ScheduleTask task=getFirst();
        task.setStartDate(this.getStandardTime() == null ? Calendar.getInstance().getTime() : this.getStandardTime());
        int i=1;
        while (task!=null){
            task.calcDate(wrapper);
            task.setIndexOrder(i);
            task=task.getSon();
            i++;
        }
    }

    public void addFirst(ScheduleTask t) {
        if (getFirst() == null) {
             setLast(t);
        } else {
            ScheduleTask.setFatherSonRelation(t,getFirst());
        }
        setFirst(t);
    }

    public void addLast(ScheduleTask t) {
        if (getLast() == null) {
            setFirst(t);
        } else {
            ScheduleTask.setFatherSonRelation(getLast(), t);
        }
        setLast(t);
    }

//    public void addTo(ScheduleTask lineStart, ScheduleTask lineEnd, ScheduleTask to, boolean before) throws Exception {
//
//        if (lineStart == null || lineEnd == null || to == null) {
//            throw new Exception("未选择调整的任务或放置的位置！");
//        }
//        ScheduleTask fromFather = lineStart.getFather();
//        ScheduleTask fromSon = lineStart.getSon();
//
//        ScheduleTask toFather = to.getFather();
//        ScheduleTask toSon = to.getSon();
//        if (fromFather != null && fromSon != null) {
//            ScheduleTask.setFatherSonRelation(fromFather, fromSon);
//        }
//        if (before) {
//            ScheduleTask.setFatherSonRelation(toFather, lineStart);
//            ScheduleTask.setFatherSonRelation(lineEnd, to);
//        } else {
//            ScheduleTask.setFatherSonRelation(to, lineStart);
//            ScheduleTask.setFatherSonRelation(lineEnd, toSon);
//        }
//        first.scheduleDate();
//    }


//    private void updateScheduleMap(ScheduleTask task) {
//        if (task == null) {
//            return;
//        }
//        if (task.getSon() != null) {
//            ScheduleTask inside = task;
//            while (inside != null) {
//                scheduleMap.put(inside.getID(), inside);
//                inside = inside.getSon();
//            }
//        } else {
//            last = task;
//        }
//
//        if (task.getFather() != null) {
//            ScheduleTask inside = task.getFather();
//            while (inside != null) {
//                scheduleMap.put(inside.getID(), inside);
//                inside = inside.getFather();
//            }
//        } else {
//            first = task;
//        }
//    }

}
