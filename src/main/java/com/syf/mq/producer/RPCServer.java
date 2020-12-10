package com.syf.mq.producer;

import com.rabbitmq.client.*;

import java.io.IOException;

public class RPCServer {
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
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        String message = "hello";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        channel.close();

        //RPC过程
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicQos(1);
        System.out.println("[x] Awaiting RPC requests");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId()).build();
                String response = "";
                try {
                    String message = new String(body, "UTF-8");
                    int n = Integer.parseInt(message);
                    System.out.println("[.]fib(" + message + ")");
                    response += fib(n);
                } catch (RuntimeException e) {
                    System.out.println("[.]" + e.toString());
                } finally {
                    channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }

    private static int fib(int n) {
        if (n == 0) {
            return 0;
        }

        if (n == 1) {
            return 1;
        }

        return fib(n - 1) + fib(n - 2);
    }
}
