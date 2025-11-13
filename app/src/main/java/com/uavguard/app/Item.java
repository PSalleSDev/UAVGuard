package com.uavguard.app;

public class Item {

    public String model;
    public String version;
    public boolean installed;

    public Item() {} // necessário para Gson

    public Item(String model, String version, boolean installed) {
        this.model = model;
        this.version = version;
        this.installed = installed;
    }
}
