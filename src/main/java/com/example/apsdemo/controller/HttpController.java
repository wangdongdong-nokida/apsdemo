package com.example.apsdemo.controller;

import cn.hutool.json.JSONUtil;
import com.example.apsdemo.dao.businessObject.PickingOrder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(path = "/http")
public class HttpController {

    //    private static final String url = "http://localhost/CamstarPortal/startContainer.do";
//    private static final String url = "http://172.16.0.12/CamstarPortal/startContainer.do";
//    private static final String url = "http://10.14.100.21/CamstarPortal/startContainer.do";
    private static final String url = "http://10.14.100.61/CamstarPortal/startContainer.do";


    @PostMapping(path = "/test")
    public void test(HttpServletRequest request) {
        System.out.println(request);
    }

    public static void postHttp(Set<Long> ids, String type) {
        Map data = new HashMap();
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        List list = new LinkedList();
        for (Long id : ids) {
            Map idMap = new HashMap();
            idMap.put("id", id);
            idMap.put("GXType", type);
            idMap.put("workflow", type);
            list.add(idMap);
        }
        postThread(data, map, list);
    }

    private synchronized static void postThread(Map data, LinkedMultiValueMap<String, String> map, List list) {
        data.put("list", list);
        String listJson = JSONUtil.toJsonStr(data);
        RestTemplate client = new RestTemplate();
        map.add("data", listJson);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(client.postForEntity(HttpController.url, map, String.class).getBody());
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static void postPickingOrderHttp(Set<PickingOrder> ids, String type) {
        Map data = new HashMap();
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        List list = new LinkedList();
        for (PickingOrder id : ids) {
            Map idMap = new HashMap();
            idMap.put("id", id.getID());
            idMap.put("GXType", type);
            idMap.put("workflow", id.getWorkFlowName().getWorkFlowName());
            list.add(idMap);
        }
        postThread(data, map, list);
    }

}
