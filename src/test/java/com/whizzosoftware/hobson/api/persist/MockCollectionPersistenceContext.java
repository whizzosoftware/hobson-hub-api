package com.whizzosoftware.hobson.api.persist;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MockCollectionPersistenceContext implements CollectionPersistenceContext {
    private Map<String,Map<String,Object>> maps = new HashMap<>();

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
