package com.example.apsdemo.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class PickingItemParams {
    private List<String> modelIds = new LinkedList<>();
    private Map modelNrs = new HashMap();
}
