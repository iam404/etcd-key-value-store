package com.iam404.restkeyvaluevault.controllers;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedHashTreeMap;
import com.iam404.restkeyvaluevault.Entity.Config;
import com.iam404.restkeyvaluevault.Entity.Data;
import com.iam404.restkeyvaluevault.services.EtcdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zalando.boot.etcd.EtcdException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
To Do: Open Endpoints with following Methods
Name	    Method	    URL
Query	    GET	        /search?name={config_name}&data.key={key_to_search}
Query	    GET	        /search?data.key={key_to_search}
 */

@RestController
@Validated
public class SearchController {

    private String config;
    private String key;
    private String val;

    @Autowired
    EtcdService etcdService;

    @Autowired
    Gson gson;

    @RequestMapping(value="/search", method= RequestMethod.GET, produces="application/json")
    @ResponseBody
    public ResponseEntity<Object> searchMethods(@RequestParam Map<String, String> queryParam) throws EtcdException {

        /*
        Iterator<Map.Entry<String, String>> itr = queryParam.entrySet().iterator();

        while(itr.hasNext())
        {
            Map.Entry<String, String> entry = itr.next();

            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());

            if (entry.getKey().contains("name")){
                this.config = entry.getValue();

            }

            if (entry.getKey().startsWith("data.")){
                this.key = entry.getKey().split("\\.")[1];
                this.val = entry.getValue();

            }

        }
        System.out.printf("%s %s %s", this.config,this.key,this.val );
        */

        final String config = queryParam.get("name");
        final String key = queryParam.get("data.key");

        if (config != null && key !=null){
            LinkedHashTreeMap<String, String> myMap = etcdService.getKeyValueMap(config);
            if (myMap.containsKey(key)){
                Map<String, Data> m = new HashMap<>();
                Data d = new Data();
                d.setKey(key);
                d.setValue(myMap.get(key));
                m.put(config,d);
                String res = gson.toJson(m);
                ResponseEntity<Object> response = new ResponseEntity<Object>(res,HttpStatus.OK);
                return response;
            } else {
                ResponseEntity<Object> response = new ResponseEntity<Object>("FAILED COULD NOT MATCH ANY ITEM",HttpStatus.NOT_FOUND);
                return response;
            }
        } else if (config == null && key !=null){
            List<Config> myList = etcdService.getDirList();
            Map<String, Data> m = new HashMap<>();
            for (Config var : myList)
            {
                LinkedHashTreeMap<String, String> myMap = etcdService.getKeyValueMap(var.getKey().split("/")[1]);
                if (myMap.containsKey(key)){
                    Data d = new Data();
                    d.setKey(key);
                    d.setValue(myMap.get(key));
                    m.put(var.getKey().split("/")[1],d);
                }
            }

            if(m.size() > 0){
                String res = gson.toJson(m);
                ResponseEntity<Object> response = new ResponseEntity<Object>(res,HttpStatus.OK);
                return response;
            } else {
                ResponseEntity<Object> response = new ResponseEntity<Object>("FAILED KEY NOT FOUND",HttpStatus.NOT_FOUND);
                return response;
            }
        } else{
            ResponseEntity<Object> response = new ResponseEntity<Object>("FAILED INVALID QUERY PARAMS PROVIDED",HttpStatus.BAD_REQUEST);
            return response;
        }

    }
}
