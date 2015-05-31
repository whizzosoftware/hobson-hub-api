package com.whizzosoftware.hobson.api.persist;

import java.util.HashMap;
import java.util.Map;

public class MockCollectionPersistenceContext implements CollectionPersistenceContext {
    private Map<String,Map<String,String>> maps = new HashMap<>();

    @Override
    public Map<String, String> getMap(String key) {
        Map<String,String> map = maps.get(key);
        if (map == null) {
            map = new HashMap<>();
            maps.put(key, map);
        }
        return map;
    }

    @Override
    public void commit() {

    }

    public boolean hasMap(String key) {
        return maps.containsKey(key);
    }
}
