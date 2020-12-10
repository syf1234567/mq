package com.syf.mq.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class RPCClient {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "192.168.0.2";
    private static final int PORT = 5672;
    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";
    private String replyQueueName;
   // private QueueingConsumer consumer;





    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");
        factory.setPassword("root123");
        com.rabbitmq.client.Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        String message = "hello";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        channel.close();

        replyQueueName = channel.queueDeclare().getQueue();
      //  consumer = new QueueingConsumer(channel);
      //  channel.basicConsume(replyQueueName,true,consumer);
    }

    public String call(String message)throws IOException, ShutdownSignalException, ConsumerCancelledException,InterruptedException{
        String response = null;
        String corrId = UUID.randomUUID().toString();
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().correlationId(corrId).replyTo(replyQueueName).build();
        channel.basicPublish("",requestQueueName,properties,message.getBytes());

        while (true){
     //       QueueingConsumer.Delivery delivery = consumer.nextDelivery();
    //        if(delivery.getProperties().getCorrelationId().equals(corrId)){
    //            response = new String(delivery.getBody());
                break;
            }
      //  }
        return response;
    }

    public void close() throws Exception {
        connection.close();
    }

    public static void main(String[] args) throws Exception{
        RPCClient fibRpc = new RPCClient();
        System.out.println(" [x] Requesting fib(30)");
        String response = fibRpc.call("30");
        System.out.println(" [.] Got '"+response+"'");
        fibRpc.close();
    }
}
