package com.syf.mq.po;

import java.util.Map;


public class Queue {
    private String name;
    private String vohost;
    private Boolean durable;
    private Boolean auto_delete;
    private Map<String, Object> arguments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVohost() {
        return vohost;
    }

    public void setVohost(String vohost) {
        this.vohost = vohost;
    }

    public Boolean getDurable() {
        return durable;
    }

    public void setDurable(Boolean durable) {
        this.durable = durable;
    }

    public Boolean getAuto_delete() {
        return auto_delete;
    }

    public void setAuto_delete(Boolean auto_delete) {
        this.auto_delete = auto_delete;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }
}
