package com.syf.mq.po;

import java.util.Properties;

public class AMQPPing {
    private static String host = "localhost";
    private static int port = 5672;
    private static String vhost = "/";
    private static String username = "guest";
    private static String password = "guest";

    static {
        Properties properties = new Properties();
        try{
            properties.load(AMQPPing.class.getClassLoader().getResourceAsStream("rmq_cfg.properties"));
            host = properties.getProperty("host");
            port = Integer.valueOf(properties.getProperty("port"));
            vhost = properties.getProperty("vhost");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
