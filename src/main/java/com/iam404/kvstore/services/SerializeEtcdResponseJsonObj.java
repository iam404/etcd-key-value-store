package com.iam404.restkeyvaluevault.services;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;
import org.zalando.boot.etcd.EtcdException;
import org.zalando.boot.etcd.EtcdNode;
import org.zalando.boot.etcd.EtcdResponse;

import java.util.List;

@Service
public class SerializeEtcdResponseJsonObj {

    public String getJsonObject(EtcdResponse obj) throws EtcdException {

        EtcdNode node = obj.getNode();
        List<EtcdNode> nodes = node.getNodes();
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .setExclusionStrategies(new MyCustomExclusionStrategy()).create();
        String element = gson.toJson(nodes);

        return element;
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
