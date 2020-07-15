package com.example.apsdemo.domain;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class TestMoveTaskParams {
    List<Long> moveKeys=new LinkedList<>();
    List<Long> toPlace=new LinkedList<>();
    String equipmentId;
}
