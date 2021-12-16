package com.zl.javabingfabianchengshizhan.threadSafe;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Allen.zhang
 * @title: DelegatingVehicleTracker
 * @projectName zl
 * @description: TODO
 * @date 2021/11/71:07
 */
public class DelegatingVehicleTracker {

    private final ConcurrentHashMap<String, Point> locations;

    private final Map<String, Point> unmodifiableMap;


    public DelegatingVehicleTracker(Map<String, Point> points) {
        this.locations = new ConcurrentHashMap<>(points);
        this.unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    public Map<String, Point> getLocations() {
        return unmodifiableMap;
    }

    public Point getLocation(String id){
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (locations.replace(id, new Point(x, y)) == null) {
            throw new IllegalArgumentException("invalid vehicle name: " + id);
        }
    }

    public static void main(String[] args) {
        Map<String, Point> a = new ConcurrentHashMap<>();
        a.put("aa", new Point(1,2));
        a.put("cc", new Point(5,6));
        Point bb = a.replace("aa", new Point(3,4));
        System.out.println("bb:"+bb);
        System.out.println("a:"+a);

    }
}
