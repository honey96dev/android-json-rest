package com.example.jsonrest;

public class ServerDataModel {
    boolean checked;
    String id;
    String name;
    String ip;
    String port;

    public ServerDataModel(String name, String ip, String port, String id) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.id = id;
    }

    public ServerDataModel(String name, String ip, String port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.id = "-1";
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

    public String getId() {
        return id;
    }

}
