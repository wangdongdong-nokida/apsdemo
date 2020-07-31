package com.example.apsdemo;

import com.example.apsdemo.annotation.ListenerTo;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TestController {

    private String  name;
    private String reflectName;

    @ListenerTo(type = "com.example.apsdemo.TestController",path = "calcReflectName")
    public void calcChangeName(String name){
        this.setName(name);
    }

    public void calcReflectName(){
        this.setReflectName(this.getName());
    }

}
