package com.syf.mq.master;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerShutdownSignalCallback;

import java.util.HashMap;
import java.util.Map;

public class Consumer {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "192.168.0.2";
    private static final int PORT = 5672;

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");
        factory.setPassword("root123");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
0        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        Map<String, Object> arg = new HashMap<String, Object>();
        arg.put("x-cancel-on-ha-failover", true);

        ConsumerShutdownSignalCallback consumer = new ConsumerShutdownSignalCallback();

        channel.basicConsume("my-queue", false, args, consumer);
    }
}
