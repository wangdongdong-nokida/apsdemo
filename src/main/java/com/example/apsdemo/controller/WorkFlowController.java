package com.example.apsdemo.controller;

import com.example.apsdemo.dao.camstarObject.WorkStep;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.WorkFLowService;
import com.example.apsdemo.service.WorkStepService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/workFlow")
public class WorkFlowController {
    @Autowired
    WorkFLowService workFLowService;

    @Autowired
    WorkStepService workStepService;

    @RequestMapping(path = "/getWorkFlow")
    public Result getWorkFlow(){
       return Tools.getResult(new HashMap<>(),workFLowService);
    }

    @RequestMapping(path = "/getWorkStep")
    public Result getWorkStep(@RequestBody Map params){
        if(params.get("params")==null||((Map)params.get("params")).get("workFlow-ID")==null){
            return new Result();
        }
        return Tools.getResult(params,workStepService);
    }

}
