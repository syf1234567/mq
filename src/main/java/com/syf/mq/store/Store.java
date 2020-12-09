package com.syf.mq.store;

import java.util.HashMap;
import java.util.Map;

public class Store {
    public static void main(String[] args) {
        Map<String,Object> arg = new HashMap<>();
        arg.put("x-queue-mode","lazy");
        channel.queueDeclare("myqueue",false,false,false,false,arg);
    }
}
