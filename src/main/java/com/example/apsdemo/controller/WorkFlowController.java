package com.example.apsdemo.controller;

import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.dao.camstarObject.OperationEquipment;
import com.example.apsdemo.dao.camstarObject.WorkStepName;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.*;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(path = "/workFlow")
public class WorkFlowController {
    @Autowired
    WorkFLowService workFLowService;

    @Autowired
    WorkStepService workStepService;

    @Autowired
    WorkStepNameService workStepNameService;

    @Autowired
    OperationEquipmentService operationEquipmentService;

    @Autowired
    EquipmentService equipmentService;

    @RequestMapping(path = "/getWorkFlow")
    public Result getWorkFlow() {
        return Tools.getResult(new HashMap<>(), workFLowService);
    }

    @RequestMapping(path = "/getWorkStep")
    public Result getWorkStep(@RequestBody Map params) {
        if (params.get("params") == null || (((Map) params.get("params")).get("workFlow-ID") == null)) {
            return new Result();
        }
        return Tools.getResult(params, workStepService);
    }

    @RequestMapping(path = "/getWorkStepName")
    public Result getWorkStepName(@RequestBody Map params) {
        if (params.get("params") == null || (((Map) params.get("params")).get("showAll") == null)) {
            return new Result();
        }
        return Tools.getResult(params, workStepNameService);
    }

    @RequestMapping(path = "/getWorkStepEquipment")
    public Result getWorkStepEquipment(@RequestBody Map params) {
        if (params.get("params") == null || (((Map) params.get("params")).get("workStepName-ID") == null)) {
            return new Result();
        }
        return Tools.getResult(params, operationEquipmentService);
    }

    @RequestMapping(path = "/getEquipmentByOperation")
    public Result getEquipmentByOperation(@RequestBody Map params) {
        return Tools.getResult(new HashMap<>(), equipmentService);

    }

    @RequestMapping(path = "/addOperationEquipmentRelation")
    @Transactional
    public void addOperationEquipmentRelation(@RequestBody Map<String, List<String>> params) {
        if ((params.get("workStepName") == null) || (params.get("equipment") == null)) {
            return;
        }

        List<String> workStepNames = params.get("workStepName");
        List<String> equipment = params.get("equipment");

        if (workStepNames.size() > 0 || equipment.size() > 0) {
            Optional<WorkStepName> workStepNameOptional = workStepNameService.findById(workStepNames.get(0));
            List<Equipment> equipmentsFind = equipmentService.findAll(equipment);
            if (workStepNameOptional.isPresent()) {
               Set<OperationEquipment> operationEquipments=workStepNameOptional.get().getOperationEquipments();
               Set<Equipment> equipmentsBind=new HashSet<>();
               for(OperationEquipment operationEquipment:operationEquipments){
                   equipmentsBind.add(operationEquipment.getEquipment());
               }
                for (Equipment index : equipmentsFind) {
                    if(equipmentsBind.contains(index)){
                        continue;
                    }
                    OperationEquipment operationEquipment = new OperationEquipment();
                    operationEquipment.setEquipment(index);
                    operationEquipment.setWorkStepName(workStepNameOptional.get());
                    operationEquipmentService.save(operationEquipment);
                }
            }
        }
    }

    @RequestMapping(path = "/deleteOperationEquipment")
    @Transactional
    public void deleteOperationEquipment(@RequestBody Map<String, List<Long>> params) {
        if ((params.get("ids") == null)) {
            return;
        }
        List<Long> ids = params.get("ids");
        if (ids.size() > 0) {
            List operationEquipmentAll=operationEquipmentService.findAll(ids);
            operationEquipmentService.deleteAll(operationEquipmentAll);
        }
    }

}
