package com.jed.game.structure;

public class Setting {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Setting(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
