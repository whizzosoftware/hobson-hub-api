package com.whizzosoftware.hobson.api.persist;

import java.util.*;

public class MockCollectionPersistenceContext implements CollectionPersistenceContext {
    private Map<String,Object> maps = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void addSetValue(String key, Object value) {
        Set<Object> s = (Set<Object>)maps.get(key);
        if (s == null) {
            s = new TreeSet<>();
            maps.put(key, s);
        }
        s.add(value);
    }

    @Override
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
    public Set<Object> getSet(String key) {
        Set<Object> set = (Set<Object>)maps.get(key);
        if (set == null) {
            set = new HashSet<>();
            maps.put(key, set);
        }
        return set;
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

    @Override
    public void commit() {

    }

    @Override
    public void close() {

    }

    public boolean hasMap(String key) {
        return maps.containsKey(key) && maps.get(key) instanceof Map;
    }

    @Override
    public boolean hasSet(String key) {
        return maps.containsKey(key) && maps.get(key) instanceof Set;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasSetValue(String key, Object value) {
        return (maps.containsKey(key) && ((Set<Object>)maps.get(key)).contains(value));
    }
}
