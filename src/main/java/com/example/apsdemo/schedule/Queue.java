package com.example.apsdemo.schedule;

import com.example.apsdemo.Base.DataBase;

public class Queue<T extends DataBase>{

    private Container first=null;
    private Container last=null;

    public void addFirst(T t){
        Container container=new Container(t);
        if(first==null){
            first=container;
            last=container;
        }else {
            container.setAfter(first);
        }
    }

    public void addLast(T t){
        Container container=new Container(t);
    }

    class Container{
        Container(T t) {
            this.t = t;
        }

        private T t;
        private Container before;
        private Container after;

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }

        public Container getBefore() {
            return before;
        }

        public void setBefore(Container before) {
            this.before = before;
        }

        public Container getAfter() {
            return after;
        }

        public void setAfter(Container after) {
            this.after = after;
        }
    }
}
