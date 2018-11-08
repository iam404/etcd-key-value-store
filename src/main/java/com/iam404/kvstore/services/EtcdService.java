package com.iam404.restkeyvaluevault.services;

import com.google.gson.internal.LinkedHashTreeMap;
import com.iam404.restkeyvaluevault.Entity.Config;
import org.springframework.stereotype.Service;
import org.zalando.boot.etcd.EtcdException;

import java.util.List;

@Service
public interface EtcdService {

   List<Config> getDirList()
       throws EtcdException;

   LinkedHashTreeMap<String, String> getKeyValueMap(final String dir)
       throws EtcdException;

   void createDir(final String key)
            throws EtcdException;

   void createKey(final String dir, String key, final String val )
       throws EtcdException;

   void updateKey(final String dir, String key, final String val )
           throws EtcdException;

   void deleteDir(final String key)
       throws EtcdException;

   void deleteKey(final String key)
       throws EtcdException;

   boolean isKeyExist(final String dir, String key)
           throws EtcdException;

   boolean isDirExist(final String d)
            throws EtcdException;

}
