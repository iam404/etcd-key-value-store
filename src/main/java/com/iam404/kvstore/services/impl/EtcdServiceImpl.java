package com.iam404.restkeyvaluevault.services.impl;

import com.google.gson.*;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.reflect.TypeToken;
import com.iam404.restkeyvaluevault.Entity.Config;
import com.iam404.restkeyvaluevault.Entity.Data;
import com.iam404.restkeyvaluevault.services.EtcdService;
import com.iam404.restkeyvaluevault.services.SerializeEtcdResponseJsonObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.zalando.boot.etcd.EtcdClient;
import org.zalando.boot.etcd.EtcdException;
import org.zalando.boot.etcd.EtcdNode;
import org.zalando.boot.etcd.EtcdResponse;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



@Service
public class EtcdServiceImpl implements EtcdService {

    @Autowired
    private EtcdClient etcdClients;

    @Autowired
    private SerializeEtcdResponseJsonObj serializeEtcdResponseJsonObj;



    @Override
    public List<Config> getDirList() throws EtcdException,NullPointerException {

        List<String> myMap = new ArrayList<>();


        EtcdResponse obj = etcdClients.get("");

        String element = serializeEtcdResponseJsonObj.getJsonObject(obj);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Type list = new TypeToken<List<Config>>(){}.getType();
        List<Config> enums = gson.fromJson(element, list);
        try {
            for(int i = 0; i < enums.size(); i++) {
                myMap.add(enums.get(i).getKey().split("/")[1]);
            }

        }catch (NullPointerException e){
            System.out.println(e);
        }

        return enums;

    }


    @Override
    public LinkedHashTreeMap<String, String> getKeyValueMap(String dir) throws EtcdException, NullPointerException, HttpClientErrorException {
        LinkedHashTreeMap<String, String> myMap= new LinkedHashTreeMap<>();

        EtcdResponse res = etcdClients.get(dir);
        EtcdNode node = res.getNode();
        List<EtcdNode> nodes = node.getNodes();

        Gson gson = new GsonBuilder().setPrettyPrinting()
                .setExclusionStrategies(new MyCustomExclusionStrategy()).create();
        String element = gson.toJson(nodes);

        Type list = new TypeToken<List<Data>>(){}.getType();
        List<Data> enums = gson.fromJson(element, list);
        try {
            for(int i = 0; i < enums.size(); i++) {
                myMap.put(enums.get(i).getKey().substring(1).split("/")[1] ,enums.get(i).getValue());
            }

        }catch (NullPointerException e){
           System.out.println(e);
        }

        String ret = gson.toJson(myMap);


        return myMap;

    }

    @Override
    public void createDir(String dir) throws EtcdException {
         etcdClients.putDir(dir);
    }


    @Override
    public void updateKey(String dir, String key, String val) throws EtcdException {
        etcdClients.put(dir + "/" + key, val);
    }

    @Override
    public void deleteDir(String key) throws EtcdException {
        etcdClients.deleteDir(key,true);
    }

    @Override
    public void deleteKey(String key) throws EtcdException {
        etcdClients.delete(key);

    }

    @Override
    public boolean isKeyExist(final String dir, String key)
            throws EtcdException, HttpClientErrorException  {
        LinkedHashTreeMap<String, String> hashTree;
        try {
            hashTree = this.getKeyValueMap(dir);

            if (hashTree.containsKey(key)){
                return true;
            } else {
                System.out.println("KEY Not Found");
                return false;
            }
        } catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean isDirExist(final String dir)
            throws EtcdException  {

        List<Config> l;

        try {
            l = this.getDirList();
            for(int i = 0; i < l.size(); i++) {
                if (l.get(i).getKey().split("/")[1].equals(dir))
                {
                    return true;
                }
            }
            return false;
        } catch (Exception e){
            return false;
        }

    }


    @Override
    public void createKey(String dir, String key, String val) throws EtcdException {
        etcdClients.create(dir + "/" + key,val);
    }


    private static class MyCustomExclusionStrategy implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {
            return (f.getDeclaringClass() == EtcdNode.class && f.getName().equals("createdIndex"))||
                    (f.getDeclaringClass() == EtcdNode.class && f.getName().equals("modifiedIndex"))||
                    (f.getDeclaringClass() == EtcdNode.class && f.getName().equals("dir"));
        }

    }

}
