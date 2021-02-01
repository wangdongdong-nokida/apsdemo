package com.example.apsdemo.logicSchedule;

import com.example.apsdemo.dao.businessObject.EquipmentCalendar;
import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.utils.AspectLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.apsdemo.utils.Tools.dayFormat;

@Component
public class EquipmentCalendarBitSet {

    public static final Logger log = LoggerFactory.getLogger(AspectLogger.class);

    public final Map<String, BitSetWrapper> bitSetWrapperMap = new HashMap<>();

    public BitSetWrapper initialize(Calendar start, Calendar end, Equipment equipment) {
        BitSetWrapper wrapper = new BitSetWrapper(start, end, equipment);
        bitSetWrapperMap.put(equipment.getID(), wrapper);
        return wrapper;
    }

    public class BitSetWrapper {
        private BitSet bitSet;
        private Calendar start;
        private Calendar end;

        public BitSetWrapper(Calendar start, Calendar end, Equipment equipment) {
            initialize(start, end, equipment);
        }

        public Calendar getFromStart(int range) {

            Calendar indexStart = getCalendar(start.getTime());
            indexStart.add(Calendar.MINUTE, range);
            return indexStart;
        }

        private void initialize(Calendar start, Calendar end, Equipment equipment) {
            this.bitSet = getEquipmentsRule(start, end, equipment);
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            start.set(Calendar.MILLISECOND, 0);
            this.start = start;
            this.end = end;
        }

        public int getEndRange(int index, int range) {
            int original = index;
            int lastSet = bitSet.nextClearBit(index) - 1;
            boolean result = lastSet - index >= range;
            if (result) {
                return range;
            }
            while (true) {
                range = range + index - lastSet;
                index = bitSet.nextSetBit(lastSet);
                lastSet = bitSet.nextClearBit(index);
                if (lastSet - index >= range) {
                    return index + range - original;
                }
            }
        }

        public int getStartAvailable(Date date) {
            long dateTime = date.getTime();
            long startMills = start.getTimeInMillis();
            int minusRange = (int) ((dateTime - startMills) / (60 * 1000));
            return bitSet.nextSetBit(minusRange < 0 ? 0 : minusRange);
        }

        public BitSet getBitSet() {
            return bitSet;
        }

        public void setBitSet(BitSet bitSet) {
            this.bitSet = bitSet;
        }

        public Calendar getStart() {
            return start;
        }

        public void setStart(Calendar start) {
            this.start = start;
        }

        public Calendar getEnd() {
            return end;
        }

        public void setEnd(Calendar end) {
            this.end = end;
        }
    }

    /*
/根据时间间隔创建设备日历
 */
    public BitSet getEquipmentsRule(Calendar start, Calendar end, Equipment equipment) {

        Calendar originalStart = Calendar.getInstance();
        Calendar originalEnd = Calendar.getInstance();
        originalStart.setTime(start.getTime());
        originalEnd.setTime(end.getTime());


        Map<Integer, Set<EquipmentCalendar>> calendarMap = new HashMap<>();
        Set<EquipmentCalendar> calendars = equipment.getCalendars();
        for (EquipmentCalendar calendar : calendars) {
            switch (calendar.getRepeatType()) {
                case 0:
                    calendarMap.computeIfAbsent(0, key -> new HashSet<>()).add(calendar);
                    break;
                case 1:
                    calendarMap.computeIfAbsent(1, key -> new HashSet<>()).add(calendar);
                    break;
                case 2:
                    calendarMap.computeIfAbsent(2, key -> new HashSet<>()).add(calendar);
                    break;
                case 3:
                    calendarMap.computeIfAbsent(3, key -> new HashSet<>()).add(calendar);
                    break;
                case 4:
                    calendarMap.computeIfAbsent(4, key -> new HashSet<>()).add(calendar);
                    break;
                case 5:
                    calendarMap.computeIfAbsent(5, key -> new HashSet<>()).add(calendar);
                    break;
                case 6:
                    calendarMap.computeIfAbsent(6, key -> new HashSet<>()).add(calendar);
                    break;
                case 7:
                    calendarMap.computeIfAbsent(7, key -> new HashSet<>()).add(calendar);
                    break;
            }
        }
        BitSet bitSet = new BitSet();

        createCalendarRule(bitSet, 0, originalStart, originalEnd, calendarMap);
        return bitSet;
    }

    /*
    /根据开始结束时间获取日历刻度
     */
    public void createCalendarRule(BitSet bitSet, int index, Calendar start, Calendar end, Map<Integer, Set<EquipmentCalendar>> classifiedCalendar) {

        Set<EquipmentCalendar> repeatCalendars = classifiedCalendar.get(start.get(Calendar.DAY_OF_WEEK));
        Set<EquipmentCalendar> normalCalendarsAll = classifiedCalendar.get(0);
        Set<EquipmentCalendar> normalCalendars = new HashSet<>();
        String standardDay = getDayString(start);
        if (normalCalendarsAll != null) {
            for (EquipmentCalendar calendar : normalCalendarsAll) {
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.setTime(calendar.getStartTime());
                String day = getDayString(calendarDate);
                if (standardDay.equals(day)) {
                    normalCalendars.add(calendar);
                }
            }
        }
        ruleCalendars(bitSet, index, repeatCalendars, normalCalendars);


        start.add(Calendar.DATE, 1);
        index++;
        if (start.before(end)) {
            createCalendarRule(bitSet, index, start, end, classifiedCalendar);
        }
    }

    public void ruleCalendars(BitSet bitSet, int index, Set<EquipmentCalendar> calendars, Set<EquipmentCalendar> normalCalendars) {
        Set<EquipmentCalendar> blackCalendars = new HashSet<>();
        if (calendars != null) {
            for (EquipmentCalendar equipmentCalendar : calendars) {
                if (!equipmentCalendar.isBlackName()) {
                    fillCalendarRule(bitSet, index, equipmentCalendar);
                } else {
                    blackCalendars.add(equipmentCalendar);
                }
            }
        }

        if (normalCalendars != null) {
            for (EquipmentCalendar equipmentCalendar : normalCalendars) {
                if (!equipmentCalendar.isBlackName()) {
                    fillCalendarRule(bitSet, index, equipmentCalendar);
                } else {
                    blackCalendars.add(equipmentCalendar);
                }
            }
        }

        for (EquipmentCalendar equipmentCalendar : blackCalendars) {
            fillCalendarRule(bitSet, index, equipmentCalendar);
        }
    }

    private void fillCalendarRule(BitSet bitSet, int index, EquipmentCalendar equipmentCalendar) {
        Calendar startTime = getCalendar(equipmentCalendar.getStartTime());
        Calendar endTime = getCalendar(equipmentCalendar.getEndTime());

        int position = index * 1440;
        int startMinute = position + (startTime.get(Calendar.HOUR_OF_DAY) * 60) + startTime.get(Calendar.MINUTE);
        int endMinute = position + (endTime.get(Calendar.HOUR_OF_DAY) * 60) + endTime.get(Calendar.MINUTE);
        if (equipmentCalendar.getRepeatType() == 0) {
            startMinute = position + (startTime.get(Calendar.HOUR_OF_DAY) * 60) + startTime.get(Calendar.MINUTE);
            endMinute = (int) ((endTime.getTime().getTime()-startTime.getTime().getTime())/(1000*60)+startMinute);
        }
        if (startMinute > endMinute) {
            log.info("开始时间大于结束时间");
            log.info("ID:"+equipmentCalendar.getID());
            return;
        }
        bitSet.set(startMinute, endMinute, !equipmentCalendar.isBlackName());
    }

    private Calendar getCalendar(Date time) {
        Calendar calendarOut = Calendar.getInstance();
        calendarOut.setTime(time);
        return calendarOut;
    }

    private String getDayString(Calendar today) {
        return dayFormat.format(today.getTime());
    }

}
