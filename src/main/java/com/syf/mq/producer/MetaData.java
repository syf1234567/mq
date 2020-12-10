package com.syf.mq.producer;

import com.alibaba.fastjson.JSONArray;
import com.syf.mq.po.Binding;
import com.syf.mq.po.Exchange;
import com.syf.mq.po.Queue;
import net.sf.json.JSONObject;
import org.springframework.boot.json.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaData {
    private static List<Queue> queueList = new ArrayList<Queue>();
    private static List<Exchange> exchanges = new ArrayList<>();
    private static List<Binding> bingingList = new ArrayList<Binding>();

    /*private static void parseJson(String filename) {
        JsonParser parser = new JsonParser();
        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader(filename));
            JSONArray jsonQueueArray = json.get("queues").getAsJsonArray();
            for (int i = 0; i < jsonQueueArray.size(); i++) {
                JSONObject subObject = jsonQueueArray.get(i).getAsJsonObject();
                Queue queue = parseQueue(subObject);
                queueList.add(queue);
            }
            JSONArray jsonExchangeArray = json.get("exchanges").getAsJsonArray();
            for (int i = 0; i < jsonExchangeArray.size; i++) {
                JSONObject subObject = jsonExchangeArray.get(i).getAsJsonObject();
                Binding binding = parseBinding(subObject);
                bingingList.add(binding);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/

   /* //解析队列信息
    private static Queue parseQueue(JSONObject subObject) {
        Queue queue = new Queue();
        queue.setName(subObject.get("name").getAsString());
        queue.setVohost(subObject.get("vhost").getAsString());
        queue.setDurable(subObject.get("durable").getAsBoolean());
        queue.setAuto_delete(subObject.get("auto_delete").getAsBoolean());
        JsonObject argsObject = subObject.get("arguments").getAsJsonObject();
        Map<String, Object> map = parseArguments(argsObject);
        queue.setArguments(map);
        return queue;
    }

    //解析绑定信息
    private static Binding parseBinding(JsonObject subObject) {

    }

    //解析参数arguments这一项内容
    private static Map<String, Object> parseArguemnts(JsonObject argsObject) {
        Map<String, Object> map = new HashMap<String, Object>();
        Set<Map.Entry<String, JsonElement>> entrySet = argsObject.entrySet();
        for (Map.Entry<String, JsonElement> mapEntry : entrySet) {
            map.put(mapEntry.getKey(),mapEntry.getValue());
        }
        return map;
    }*/
}
