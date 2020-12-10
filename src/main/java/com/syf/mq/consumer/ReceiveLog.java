package com.syf.mq.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLog {

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
        try {
            Channel channelDebug = connection.createChannel();
            Channel channeInfo = connection.createChannel();
            Channel channeWarn = connection.createChannel();
            Channel channelError = connection.createChannel();
            channelDebug.basicConsume("queueu.debug", false, "DEBUG", new ConsumerThread(channelDebug));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ConsumerThread extends DefaultConsumer {

        public ConsumerThread(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            String log = new String(body);
            System.out.println("=" + consumerTag + " REPORT====\n" + log);
            getChannel().basicAck(envelope.getDeliveryTag(), false);
        }
    }

}
