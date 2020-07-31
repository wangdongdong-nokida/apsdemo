package com.example.apsdemo.domain;

import lombok.Data;

import java.util.List;

@Data
public class EditBrief {
    private String brief;
    private List<Long> ids;
}
