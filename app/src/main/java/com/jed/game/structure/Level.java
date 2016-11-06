package com.jed.game.structure;

public class Level {
    private String name;
    private String  next;
    private Map    map;

    public String getName() {
        return name;
    }

    public String getNext() {
        return next;
    }

    public Map getMap() {
        return map;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Level(String name, String next, Map map) {
        this.name = name;
        this.next = next;
        this.map  = map;
    }
}
