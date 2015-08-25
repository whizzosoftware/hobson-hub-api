package com.whizzosoftware.hobson.api.persist;

import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;

import java.util.*;

public class MockCollectionPersistenceContext implements CollectionPersistenceContext {
    private Map<String,Map<String,Object>> maps = new HashMap<>();
    private Map<PropertyContainerClassContext,PropertyContainerClass> pccMap = new HashMap<>();

    @Override
    public Map<String, Object> getMap(String key) {
        Map<String,Object> map = maps.get(key);
        if (map == null) {
            map = new HashMap<>();
            maps.put(key, map);
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> getMapsWithPrefix(String keyPrefix) {
        List<Map<String,Object>> results = new ArrayList<>();
        for (String key : maps.keySet()) {
            if (key.startsWith(keyPrefix)) {
                results.add(maps.get(key));
            }
        }
        return results;
    }

    @Override
    public void setMap(String key, Map<String,Object> map) {
        maps.put(key, map);
    }

    @Override
    public Set<String> getKeySet() {
        return maps.keySet();
    }

    @Override
    public void commit() {

    }

    public boolean hasMap(String key) {
        return maps.containsKey(key);
    }
}
