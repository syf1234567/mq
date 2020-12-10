package com.syf.mq.producer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitProducer {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "192.168.0.2";
    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");
        factory.setPassword("root123");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        String message = "hello";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        channel.close();


        /**
         * alternate exchange
         */
        Map<String, Object> arg = new HashMap<String, Object>();
        arg.put("alternate-exchange", "myAe");
        channel.exchangeDeclare("normalExchange", "direct", true, false, arg);
        channel.exchangeDeclare("myAe", "fanout", true, false, null);
        channel.queueDeclare("normalQueue", true, false, false, null);
        channel.queueBind("normalQueue", "normalExchange", "normalKey");
        channel.queueDeclare("unroutedQueue", true, false, false, null);
        channel.queueBind("unroutedQueue", "myAe", "");

        /**
         * 设置过期时间
         */
        arg.put("x-expires", 1800000);
        channel.queueDeclare("myqueue", false, false, false, arg);

        //添加死信队列        DLX
        channel.exchangeDeclare("dlx_exchange", "direct");
        arg.put("x-dead-letter-exchange", "dlx_exchange");
        channel.queueDeclare("myqueue", false, false, false, arg);
        arg.put("x-dead-letter-routing-key", "dlx-routing-key");

        /**
         * 设置TTl 和  DLX
         */
        channel.exchangeDeclare("exchange.dlx", "direct", true);
        channel.exchangeDeclare("exchange.normal", "fanout", true);
        arg.put("x-message-ttl", 10000);
        arg.put("x-dead-letter-exchange", "exchange.dlx");
        arg.put("x-dead-letter-routing-key", "routingkey");
        channel.queueDeclare("queue.narmal", true, false, false, arg);
        channel.queueBind("queue.normal", "exchange.normal", "");
        channel.queueDeclare("queue.dlx", true, false, false, null);
        channel.queueBind("queue.dlx", "exchange.dlx", "routingkey");
        channel.basicPublish("exchange.normal", "rk", MessageProperties.PERSISTENT_TEXT_PLAIN, "dlx".getBytes());

        /**
         * 设置优先级队列
         */
        arg.put("x-max-priority", 10);
        channel.queueDeclare("queue.priority", true, false, false, arg);

        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.priority(5);
        AMQP.BasicProperties properties = builder.build();
        channel.basicPublish("exchange_priority", "rk_priority", properties, "messages".getBytes());

        String callbackQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().replyTo(callbackQueueName).build();
        channel.basicPublish("", "rpc_queue", props, message.getBytes());

        /**
         * 事务机制   提供了三个方法    channel.txSelect、 channel.txCommit、  channel.txRollback
         */
        channel.txSelect();
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, "transaction messages".getBytes());
        channel.txCommit();

        /**
         * 事务回滚
         */
        try {
            channel.txSelect();
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            int result = 1 / 0;
            channel.txCommit();
        } catch (Exception e) {
            e.printStackTrace();
            channel.txRollback();
        }
        channel.txSelect();
        for (int i = 0; i < 3; i++) {
            try {
                channel.basicPublish("exchange", "routingKey", null, ("messages" + i).getBytes());
                channel.txCommit();
            } catch (IOException e) {
                e.printStackTrace();
                channel.txRollback();
            }
        }

        /**
         * publisher confirm 机制运作
         */
        try {
            channel.confirmSelect();
            channel.basicPublish("exchange", "routingKey", null, "publisher confirm test".getBytes());
            if (!channel.waitForConfirms()) {
                System.out.println("send message failed");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            channel.confirmSelect();
            int MsgCount = 0;
            while (true) {
                channel.basicPublish("exchange", "routingKey", null, "batch cofirm test".getBytes());
                if (++MsgCount >= 3) {
                    MsgCount = 0;
                    try {
                        if (channel.waitForConfirms()) {
                            continue;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //设置持久化   2代表持久化
    public static final AMQP.BasicProperties PERSISTENT_TEXT_PLAIN = new AMQP.BasicProperties("text/plain", null, null, 2, 0, null, null, null, null, null, null, null, null, null);


}
