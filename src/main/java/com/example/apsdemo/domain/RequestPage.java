package com.example.apsdemo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPage<T> {
    protected int pageSize;
    protected int current;
    protected  T params;
}
