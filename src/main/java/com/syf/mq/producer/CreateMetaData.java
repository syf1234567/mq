package com.syf.mq.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.syf.mq.po.Binding;
import com.syf.mq.po.Exchange;
import com.syf.mq.po.Queue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static java.net.URLEncoder.encode;

public class CreateMetaData {

    private static final String ip = "192.168.0.2";
    private static final String username = "root";
    private static final String password = "root123";
    private static List<Queue> queueList = new ArrayList<Queue>();
    private static List<Exchange> exchanges = new ArrayList<>();
    private static Exchange exchange = new Exchange();

    private static Binding binding = new Binding();

    private static final List<String> nodeList = new ArrayList<String>() {{
        add("root@node1");
        add("rabbit@node2");
        add("rabbit@node3");
    }};

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            Collections.shuffle(nodeList);
            System.out.println(nodeList.get(0));

        }
    }

    //创建队列
    private static Boolean createQueues() {
        try {
            for (int i = 0; i < queueList.size(); i++) {
                Queue queue = queueList.get(i);
                String url = String.format("http://%s:15672/api/queues/%s/%s", ip, encode(queue.getVohost(), "UTF-8"), encode(queue.getName(), "UTF-8"));
                Map<String, Object> map = new HashMap<>();
                map.put("auto_delete", queue.getAuto_delete());
                map.put("durable", queue.getDurable());
                map.put("arguments", queue.getArguments());
                Collections.shuffle(nodeList);
                map.put("node", nodeList.get(0));
                //        String data = new Gson().toJson(map);

                System.out.println(url);
                //        System.out.println(data);
                //       httpPut(url, data, username, password);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //创建交换器
    private static Boolean createExchanges() {
        //String url = String.format("http://%s:15672/api/exchanges/%s/%s", ip, encode(exchange.getVhost(), "UTF-8"), encode(exchange.getName(), "UTF-8"));
        return false;
    }

    //创建绑定关系
    private static Boolean createBindings() {
        String url = null;
        if (binding.getDestination_type().equals("queue")) {
            //   url = String.format("http://%s:15672/api/bindings/%s/e/%s/q/%s", ip, encode(binding.getVhost(), "UTF-8"), encode(binding.getSource(), "UTF-8"));
        } else {
            //    url = String.format("http://%s:15672/api/bindings/%s/e/%s/e/%s", ip, encode(binding.getVhost(), "UTF-8"), encode(binding.getSource(), "UTF-8"), encode(binding.getDestination(), "UTF-8"));
        }
        return false;
    }

    /* public static int httpPut(String url, String data, Strign username, String password) throws IOException {
         HttpClient client = new HttpClient();
         client.getState().setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(username,password));
         PutMethod putMethod = new PutMethod(url);
         putMethod.setRequestHeader("Content-Type","application/json;charset=UTF-8");
         putMethod.setRequestEntity(new StringRequestEntity(data,"application/json","UTF-8"));
         int statusCode = client.executeMethod(putMethod);
         return statusCode;
     }*/
    public static int httpPost(String url, String data, String username, String password) throws IOException {
        return 1;
    }

    private static void createQueuesNew() {
        List<Channel> channelList = new ArrayList<>();
        List<Connection> connectionList = new ArrayList<>();
        try {
            for (int i = 0; i < nodeList.size(); i++) {
                String ip = nodeList.get(i);
                ConnectionFactory connectionFactory = new ConnectionFactory();
                connectionFactory.setUsername(username);
                connectionFactory.setPassword(password);
                connectionFactory.setHost(ip);
                connectionFactory.setPort(5672);
                Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();
                channelList.add(channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            for (Connection connection : connectionList) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void createQueueByChannel(List<Channel> channelList) {
        for (int i = 0; i < queueList.size(); i++) {
            Queue queue = queueList.get(i);
            Collections.shuffle(channelList);
            Channel channel = channelList.get(0);
            try {
                Map<String, Object> mapArgs = queue.getArguments();
                channel.queueDeclare(queue.getName(), queue.getDurable(), false, queue.getAuto_delete(), mapArgs);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
