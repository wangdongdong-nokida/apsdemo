package com.example.apsdemo.logicSchedule;

import com.example.apsdemo.dao.businessObject.EquipmentCalendar;
import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.service.EquipmentService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

import static com.example.apsdemo.utils.Tools.dayFormat;

@Component
public class EquipmentCalendarSchedule {

    @Autowired
    public EquipmentService equipmentService;

    private Map<String, RuleContainer<String, int[]>> equipmentsRule;

    public EquipmentCalendarSchedule(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
        this.updateEquipmentCalendarsMap();
    }

    public void getEndTime(Calendar start, int duration) {

        RuleContainer<String, int[]> rule = new RuleContainer<>();

        int index = duration;
        int disabledMinus;
        int end = 0;
        RuleContainer<String, int[]>.Item item = rule.getItemMap().get("xxx");
        int startMinusInDay = (start.get(Calendar.HOUR_OF_DAY) * 24 + start.get(Calendar.MINUTE));


        while (index > 0) {
            int[] day = item.getValue();
            for (int i = startMinusInDay; i < 1440; i++) {
                if (day[i] == 1) {
                    index--;
                }
                if (index <= 0 || i == 1439) {
                    end += i;
                }
            }
            if (index > 0) {
                item = item.getAfter();
                if (item == null) {

                }
            }
        }
    }

    private Map<String, Map<Integer, Set<EquipmentCalendar>>> equipmentCalendarsMap = new HashMap<>();


    public Map<String, Map<Integer, Set<EquipmentCalendar>>> getEquipmentCalendarsMap() {
        return equipmentCalendarsMap;
    }

    private void setEquipmentCalendarsMap(Map<String, Map<Integer, Set<EquipmentCalendar>>> equipmentCalendarsMap) {
        this.equipmentCalendarsMap = equipmentCalendarsMap;
    }

    public void updateEquipmentCalendarsMap() {
//        this.setEquipmentCalendarsMap(equipmentService.getClassifiedCalendar());
    }


    class RuleContainer<N, V> {
        @Getter
        @Setter
        private Item first = null;
        @Getter
        @Setter
        private Item last = null;
        @Getter
        private Map<N, Item> itemMap = new HashMap<>();

        public void addLast(N name, V value) {
            if (value != null) {
                Item item = new Item(last, null, name, value);
                if (first == null) {
                    first = item;
                }
                last = item;
                itemMap.put(name, item);
            }
        }

        @Getter
        @Setter
        class Item {
            private Item before;
            private Item after;
            private N name;
            private V value;

            Item(Item before, Item after, N name, V value) {
                this.before = before;
                this.after = after;
                this.name = name;
                this.value = value;
                if (before != null) {
                    before.setAfter(this);
                }
                if (after != null) {
                    after.setBefore(this);
                }
            }
        }
    }
}
