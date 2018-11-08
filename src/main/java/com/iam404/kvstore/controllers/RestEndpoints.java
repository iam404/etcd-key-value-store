package com.iam404.restkeyvaluevault.controllers;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedHashTreeMap;
import com.iam404.restkeyvaluevault.Entity.Config;
import com.iam404.restkeyvaluevault.responses.HttpResponse;
import com.iam404.restkeyvaluevault.services.EtcdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zalando.boot.etcd.EtcdException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
To Do: Open Endpoints with following Methods
Name	    Method	    URL
List	    GET	        /configs
Create	    POST	    /configs
Get	        GET	        /configs/{name}
Update	    PUT/PATCH	/configs/{name}
Delete	    DELETE	    /configs/{name}
 */

@RestController
@Validated
public class RestEndpoints {

    @Autowired
    EtcdService etcdService;

    @Autowired
    Gson gson;


    @RequestMapping(value="/", method= RequestMethod.GET)
    @ResponseBody
    public String index() {
        return "This is a demo rest based configuration management application.";
    }

    @RequestMapping(value="/configs", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Object> listConfigMaping()
            throws EtcdException {

        try {
            List<Config> myList = etcdService.getDirList();
            List l = new ArrayList();
            for(int i = 0; i < myList.size(); i++) {
                l.add(myList.get(i).getKey().split("/")[1]);
            }
            String res = gson.toJson(l);
            return new ResponseEntity<Object>(res,HttpStatus.OK);
        } catch (EtcdException e) {

            System.out.println(e);
            return new ResponseEntity<Object>("FAILED",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/configs/{dir}", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Object> listDirMaping(@PathVariable("dir") String dir)
            throws EtcdException, IllegalAccessException {

        if (!etcdService.isDirExist(dir)){
            return new ResponseEntity<Object>("FAILED CONFIG DOES NOT EXIST",HttpStatus.NOT_FOUND);
        }

        try {
            // String res = etcdService.getKeyValueMap(dir);
            LinkedHashTreeMap<String, String> myMap = etcdService.getKeyValueMap(dir);
            String res = gson.toJson(myMap);
            return new ResponseEntity<Object>(res,HttpStatus.OK);

        } catch (EtcdException e) {
            System.out.println(e);
            return new ResponseEntity<Object>("FAILED",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/configs", method = RequestMethod.POST, consumes="application/json", produces="application/json")
    public HttpResponse createConfigMaping(@RequestBody Map<String, Map<String, String>> request)
            throws EtcdException {

        try {
            for (Map.Entry<String, Map<String, String>> entry : request.entrySet()) {
                Map<String, String> childMap = entry.getValue();
                String dir = entry.getKey();
                etcdService.createDir(dir);
                System.out.println("creating directory: " + entry.getKey());
                for (Map.Entry<String, String> entry2 : childMap.entrySet()) {
                    String key = entry2.getKey();
                    String value = entry2.getValue();
                    if (key != null) {
                        etcdService.updateKey(dir, key, value);
                    }
                    System.out.println("Added KEY :" + key + " VAL : " + value + " in DIR : " + dir);
                }
            }
            return new HttpResponse("SUCCESS","CONFIG CREATED",HttpStatus.CREATED);
        } catch (EtcdException e) {
            System.out.println(e);
            return new HttpResponse("FAILED","COULD NOT CREATE CONFIG",HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value="/configs/{dir}", method = RequestMethod.PUT,consumes="application/json", produces="application/json")
    public HttpResponse updateKeyMaping(@PathVariable("dir") String dir, @RequestBody Map<String, String> requests)
            throws EtcdException {

        if (!etcdService.isDirExist(dir)){
            return new HttpResponse("FAILED","CONFIG DOES NOT EXIST",HttpStatus.NOT_FOUND);
        }
        try{
            for (Map.Entry<String, String> entry : requests.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key != null) {
                    etcdService.updateKey(dir, key, value);
                }
                System.out.println("Updated key :" + key + " value: " + value + " in dir: " + dir);
            }
            return new HttpResponse("SUCCESS","KEYS UPDATED",HttpStatus.OK);
        } catch (EtcdException e) {
            System.out.println(e);
            return new HttpResponse("FAILED","COULD NOT UPDATE",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value="/configs/{dir}", method = RequestMethod.PATCH,consumes="application/json", produces="application/json")
    public HttpResponse patchKeyMaping(@PathVariable("dir") String dir, @RequestBody Map<String, String> request)
            throws EtcdException {

       try{
           HttpResponse res = updateKeyMaping(dir,request);
           return res;
       } catch (EtcdException e){
           return new HttpResponse("FAILED","COULD NOT PATCH",HttpStatus.INTERNAL_SERVER_ERROR);
       }

    }

    @RequestMapping(value="/configs/{dir}", method = RequestMethod.DELETE)
    public HttpResponse deleteDirMaping(@PathVariable("dir") String dir)
            throws EtcdException {

        if (!etcdService.isDirExist(dir)){
            return new HttpResponse("FAILED","CONFIG DOES NOT EXIST",HttpStatus.NOT_FOUND);
        }
        try {
            etcdService.deleteDir(dir);
            return new HttpResponse("SUCCESS","CONFIG DELETED",HttpStatus.OK);
        } catch (EtcdException e) {
            System.out.println(e);
            return new HttpResponse("FAILED","",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     @RequestMapping(value="/configs/{dir}/{key}", method = RequestMethod.DELETE)
      public HttpResponse deleteKeyMaping(@PathVariable("dir") String dir, @PathVariable("key") String key)
            throws EtcdException {

         if (!etcdService.isDirExist(dir)){
             return new HttpResponse("FAILED","CONFIG DOES NOT EXIST",HttpStatus.NOT_FOUND);
         }

         if (!etcdService.isKeyExist(dir,key)){
             return new HttpResponse("FAILED","KEY DOES NOT EXIST",HttpStatus.NOT_FOUND);
         }

        try {
            etcdService.deleteKey(dir+"/"+key);
            return new HttpResponse("SUCCESS","KEY DELETED",HttpStatus.OK);
        } catch (EtcdException e) {
            System.out.println(e);
            return new HttpResponse("FAILED","",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
