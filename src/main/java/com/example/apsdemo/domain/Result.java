package com.example.apsdemo.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Result {
    private List data=new LinkedList();
    private long total=0;
    private boolean success=true;
    private int pageSize=0;
    private int current=0;

    public Result() {
    }

    public Result(List data, long total, boolean success, int pageSize, int current) {
        this.data = data;
        this.total = total;
        this.success = success;
        this.pageSize = pageSize;
        this.current = current;
    }

    public Result(int pageSize, int current) {
        this.pageSize = pageSize;
        this.current = current;
    }
}
