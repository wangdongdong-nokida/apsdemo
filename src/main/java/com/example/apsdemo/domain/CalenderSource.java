package com.example.apsdemo.domain;

import java.text.ParseException;
import java.util.Date;

public class CalenderSource {
    private int day=24*60;
    private Date standard;
    private Range first;
    private Range last;
    private boolean repeat;


    public CalenderSource(boolean repeat) throws ParseException {
        Range range=new Range(true,0,1440);
        this.setFirst(range);
        this.setLast(range);
    }

    public Range getFirst() {
        return first;
    }

    public void setFirst(Range first) {
        this.first = first;
    }

    public Range getLast() {
        return last;
    }

    public void setLast(Range last) {
        this.last = last;
    }


    class Range {

        private Range before;
        private Range after;

        private boolean disable;
        private int start;
        private int end;
        private long range;

        Range(boolean disable, int start, int end) {
            this.disable = disable;
            this.start = start;
            this.end = end;
        }


        public Range getBefore() {
            return before;
        }

        public void setBefore(Range before) {
            this.before = before;
        }

        public Range getAfter() {
            return after;
        }

        public void setAfter(Range after) {
            this.after = after;
        }

        public boolean isDisable() {
            return disable;
        }

        public void setDisable(boolean disable) {
            this.disable = disable;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public long getRange() {
            return range;
        }

        public void setRange(long range) {
            this.range = range;
        }
    }
}
