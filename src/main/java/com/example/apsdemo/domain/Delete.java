package com.example.apsdemo.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Delete {
   private List<Long> ids;
   private String method;
}
