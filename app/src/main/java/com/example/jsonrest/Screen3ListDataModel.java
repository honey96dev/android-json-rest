package com.example.jsonrest;

public class Screen3ListDataModel {
    boolean checked;
    String name;
    String ip;
    String port;

    public Screen3ListDataModel(String name, String ip, String port) {
        this.name = name;
        this.ip = ip;
        this.port = port;

    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

}
