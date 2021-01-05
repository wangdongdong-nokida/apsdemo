package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.dao.businessData.ScheduleTaskLineData;
import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.logicSchedule.EquipmentCalendarBitSet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "A_ScheduleTaskLine")
public class ScheduleTaskLine extends ScheduleTaskLineData {

    @JsonIgnore
    @OneToOne(targetEntity = Equipment.class, fetch = FetchType.EAGER)
    private Equipment equipment;

    @JsonIgnore
    @OneToMany(targetEntity = ScheduleTask.class, mappedBy = "scheduleTaskLine", fetch = FetchType.EAGER)
    private Set<ScheduleTask> tasks = new HashSet<>();

    public Set<ScheduleTask> getTasks() {
        return tasks;
    }

    public void setTasks(Set<ScheduleTask> tasks) {
        this.tasks = tasks;
    }

    @JsonIgnore
    @JoinColumn(name = "first_id")
    @OneToOne(targetEntity = ScheduleTask.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private ScheduleTask first;

    @JsonIgnore
    @JoinColumn(name = "last_id")
    @OneToOne(targetEntity = ScheduleTask.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private ScheduleTask last;


    public ScheduleLine getScheduleLine() {
        return new ScheduleLine(this);
    }


    @Data
    public class ScheduleLine {
        private ScheduleTaskLine line;
        private Container containerFirst;
        private Container containerLast;
        private Map<Long, Container> scheduleTaskMap = new HashMap<>();

        public ScheduleTask deleteFromLine(long id) {
            Container container = removeFromLine(id);
            if (container != null) {
                return container.getSelf();
            }
            return null;
        }

        private ScheduleTaskLine.ScheduleLine.Container removeFromLine(long id) {
            Container container = scheduleTaskMap.get(id);
            if (container == null) {
                return null;
            }
            if (containerFirst == container) {
                containerFirst = container.getSon();
            }
            return container.removeFromLine();
        }

        public synchronized void removeTo(List<Long> ids, Long to, boolean after) {
            Container nextPosition = scheduleTaskMap.get(to);
            for (Long id : ids) {
                Container container = scheduleTaskMap.get(id);
                if (container != null && nextPosition != null) {
                    nextPosition.linkTo(container, after);
                    nextPosition = container;
                }
            }
        }

        public ScheduleLine(ScheduleTaskLine line) {
            this.setLine(line);
            ScheduleTask task = getFirst();
            this.setContainerFirst(null);
            this.setContainerLast(null);
            while (task != null) {
                addLast(task);
                task = task.getSon();
            }
        }

        //        从数据库中获取的排产队列，转换为内存模型
        public void addLast(ScheduleTask task) {
            task.setScheduleTaskLine(this.getLine());
            Container last = new Container(getContainerLast(), null, task);
            if (getContainerLast() == null) {
                setContainerFirst(last);
                this.getLine().setFirst(task);
            }
            setContainerLast(last);
            this.getLine().setLast(task);
            scheduleTaskMap.put(task.getID(), last);
        }


        public void addLastAndQueen(ScheduleTask task) {

            ScheduleTaskLine line = this.getLine();
            task.setScheduleTaskLine(line);
            Container last = new Container(getContainerLast(), null, task);
            if (getContainerLast() == null) {
                setContainerFirst(last);
                this.getLine().setFirst(task);
            }
            setContainerLast(last);
            line.addLast(task);
            scheduleTaskMap.put(task.getID(), last);
        }


        //        todo:逻辑需要补充
        public void addFirst(ScheduleTask task) {
            Container last = new Container(null, getContainerFirst(), task);
            if (getContainerFirst() == null) {
                setContainerLast(last);
            }
            setContainerFirst(last);
            scheduleTaskMap.put(task.getID(), last);
        }

        public void calcScheduleLineDate(EquipmentCalendarBitSet.BitSetWrapper wrapper) {
            if (getContainerFirst() != null) {
                int i = 0;
                Container container = getContainerFirst();
                if (container != null) {
                    ScheduleTask task = container.getSelf();
                    ScheduleTask next = task;

                    while (next.isFinished()) {
                        next = task.getSon();
                        deleteFromLine(task.getID());
                        task = next;
                    }
                    container = getContainerFirst();
                    while (container != null) {
                        container = getContainerFirst().calcDate(wrapper, i);
                    }
                }
            }
        }

        @Data
        class Container {
            Container father;
            Container son;
            ScheduleTask self;


            private void setSonRelation(Container son) {
                if (this == son) {
                    return;
                }
                if (getContainerFirst() == son && son.getSon() != null) {
                    setContainerFirst(son.getSon());
                }
                son.removeFromLine();
                Container containerSon = this.getSon();
                this.setSon(son);
                son.setFather(this);
                this.getSelf().setSon(son.getSelf());
                if (containerSon != null) {
                    son.setSon(containerSon);
                    containerSon.setFather(son);
                    son.getSelf().setSon(containerSon.getSelf());
                } else {
                    setLast(son.getSelf());
                    getLine().setLast(son.getSelf());
                }
            }

            private void setFatherRelation(Container father) {
                father.removeFromLine();
                Container containerFather = this.getFather();
                this.setFather(father);
                father.setSon(this);
                father.getSelf().setSon(this.getSelf());
                if (containerFather != null) {
                    containerFather.setSon(father);
                    father.setFather(containerFather);
                    containerFather.getSelf().setSon(father.getSelf());
                } else {
                    setFirst(father.getSelf());
                    getLine().setFirst(father.getSelf());
                }
            }

            void linkTo(Container container, boolean after) {
                if (container != null) {
                    if (after) {

//                        if(getFirst()==container.getSelf()&&container.getSon()!=null){
//                            setFirst(container.getSon().getSelf());
//                        }
                        setSonRelation(container);
                    } else {
                        setFatherRelation(container);
                    }
                }
            }

            private Container removeFromLine() {
                Container father = this.getFather();
                Container son = this.getSon();
                if (father != null && son != null) {
                    father.setSon(son);
                    son.setFather(father);
                    father.getSelf().setSon(son.getSelf());
                } else if (father != null) {
                    father.getSelf().setSon(null);
                    father.setSon(null);
                    setLast(father.getSelf());
                } else if (son != null) {
                    son.setFather(null);
                    setFirst(son.getSelf());
                } else {
                    setFirst(null);
                    setLast(null);
                }
                this.getSelf().setScheduleTaskLine(null);
                this.getSelf().setSon(null);
                this.setSon(null);
                this.setFather(null);
                return this;
            }

            Container calcDate(EquipmentCalendarBitSet.BitSetWrapper wrapper, int i) {
                i++;
                if (i % 500 == 0) {
                    return this;
                }
                Calendar calendarOut = Calendar.getInstance();

                if (this.getFather() == null) {
                    this.getSelf().setIndexOrder(1);
                } else {
                    this.getSelf().setIndexOrder(getFather().getSelf().getIndexOrder() + 1);
                }
                if (wrapper.getBitSet().isEmpty()) {
                    if (this.getSelf().getStartDate() == null) {
                        calendarOut.set(Calendar.SECOND, 0);
                        calendarOut.set(Calendar.MILLISECOND, 0);
                        this.getSelf().setStartDate(calendarOut.getTime());
                    }
                    calendarOut.setTime(this.getSelf().getStartDate());
                    calendarOut.add(Calendar.MINUTE, this.getSelf().getDurationTime() + this.getSelf().getDurationDelayTime());
                    this.getSelf().setEndDate(calendarOut);
                    Container next = getSon();
                    if (next != null) {
                        next.getSelf().setStartDate(self.getEndDate());
                        return next.calcDate(wrapper, i);
                    }
                } else {
                    if (self.getStartDate() == null) {
                        Calendar calendar = wrapper.getFromStart(wrapper.getBitSet().nextSetBit(0));
                        this.getSelf().setStartDate(getStandardTime() == null ? calendar.getTime() : getStandardTime());
                    }
                    int startAvailable = wrapper.getStartAvailable(self.getStartDate());
                    int endRange = wrapper.getEndRange(startAvailable, self.getDurationTime() + self.getDurationDelayTime());
                    Calendar calendar = wrapper.getFromStart(startAvailable + endRange);
                    self.setEndDate(calendar);
                    Container next = getSon();
                    if (next != null) {
                        next.getSelf().setStartDate(self.getEndDate());
                        return next.calcDate(wrapper, i);
                    }
                }
                return null;
            }

            private Container(Container father, Container son, ScheduleTask self) {
                this.setFather(father);
                this.setSon(son);
                if (father != null) {
                    father.setSon(this);
                }
                if (son != null) {
                    son.setFather(this);
                }
                this.self = self;
            }
        }
    }


    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public ScheduleTask getFirst() {
        return first;
    }

    private void setFirst(ScheduleTask first) {
        this.first = first;
    }

    private ScheduleTask getLast() {
        return last;
    }

    private void setLast(ScheduleTask last) {
        this.last = last;
        if (last != null) {
            this.setLastTime(last.getEndDate());
        }
    }

    //设置队列的开始时间
//    public void updateScheduleDate(EquipmentCalendarBitSet.BitSetWrapper wrapper) {
//        ScheduleTask task = getFirst();
//        task.setStartDate(this.getStandardTime() == null ? Calendar.getInstance().getTime() : this.getStandardTime());
//        int i = 0;
//        while (task != null) {
//            task.calcDate(wrapper);
//            task.setIndexOrder(++i);
//            task = task.getSon();
//        }
//    }

    public void addFirst(ScheduleTask t) {
        if (getFirst() == null) {
            setLast(t);
        } else {
            ScheduleTask.setFatherSonRelation(t, getFirst());
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
//        containerFirst.scheduleDate();
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
//            containerFirst = task;
//        }
//    }


    @Override
    public String toString() {
        return super.toString();
    }
}
