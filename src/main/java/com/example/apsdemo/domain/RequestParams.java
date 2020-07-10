package com.example.apsdemo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestParams extends RequestPage {
    protected String name;
    protected String type;
}
