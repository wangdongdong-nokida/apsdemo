package com.example.apsdemo.domain;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class CreateOperationParams {
    List<Long> pickingOrder = new LinkedList<>();
    String workFlow;
}
