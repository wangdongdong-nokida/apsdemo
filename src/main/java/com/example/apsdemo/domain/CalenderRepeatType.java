package com.example.apsdemo.domain;

public enum CalenderRepeatType {
    monday("monday",1),
    tuesday("tuesday",2),
    wednesday("wednesday",3),
    thursday("thursday",4),
    friday("friday",5),
    saturday("saturday",6),
    sunday("sunday",7),
    normal("normal",8);

    public String name;
    public int index;
    CalenderRepeatType(String name,int index) {
        this.name=name;
        this.index=index;
    }

}
