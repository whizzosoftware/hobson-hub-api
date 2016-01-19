package com.whizzosoftware.hobson.api.persist;

import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;

import java.util.*;

public class MockCollectionPersistenceContext implements CollectionPersistenceContext {
    private Map<String,Object> maps = new HashMap<>();
    private Map<PropertyContainerClassContext,PropertyContainerClass> pccMap = new HashMap<>();

    @Override
    public void addSetValue(String key, Object value) {
        Set<Object> s = (Set<Object>)maps.get(key);
        if (s == null) {
            s = new TreeSet<>();
            maps.put(key, s);
        }
        s.add(value);
    }

    @Override
    public Map<String, Object> getMap(String key) {
        Map<String,Object> map = (Map<String,Object>)maps.get(key);
        if (map == null) {
            map = new HashMap<>();
            maps.put(key, map);
        }
        return map;
    }

    @Override
    public Object getMapValue(String key, String name) {
        return getMap(key).get(name);
    }

    @Override
    public Set<Object> getSet(String key) {
        return (Set<Object>)maps.get(key);
    }

    @Override
    public void setMap(String key, Map<String,Object> map) {
        maps.put(key, map);
    }

    @Override
    public void setMapValue(String key, String name, Object value) {
        Map<String,Object> map = getMap(key);
        map.put(name, value);
    }

    @Override
    public void setSet(String key, Set<Object> set) {
        maps.put(key, set);
    }

    @Override
    public void remove(String key) {
        maps.remove(key);
    }

    @Override
    public void removeFromSet(String key, Object value) {
        getSet(key).remove(value);
    }

//    @Override
//    public Set<String> getKeySet() {
//        return maps.keySet();
//    }

    @Override
    public void commit() {

    }

    public boolean hasMap(String key) {
        return maps.containsKey(key) && maps.get(key) instanceof Map;
    }

    @Override
    public boolean hasSet(String key) {
        return maps.containsKey(key) && maps.get(key) instanceof Set;
    }
}
