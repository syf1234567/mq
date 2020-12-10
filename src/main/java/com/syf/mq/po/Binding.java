package com.syf.mq.po;

import java.util.Map;

public class Binding {
    private String source;
    private String vhost;
    private String destination;
    private String destination_type;
    private String routing_key;
    private Map<String,Object> arguments;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination_type() {
        return destination_type;
    }

    public void setDestination_type(String destination_type) {
        this.destination_type = destination_type;
    }

    public String getRouting_key() {
        return routing_key;
    }

    public void setRouting_key(String routing_key) {
        this.routing_key = routing_key;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }
}
