package com.syf.mq.utils;

import com.alibaba.fastjson.JSONArray;
import com.syf.mq.po.ClusterNode;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
import org.springframework.boot.json.JsonParser;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

public class HttpUtils {
    public static String httpGet(String url, String username, String password) throws IOException {
        HttpClient client = new HttpClient();
        client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        GetMethod getMethod = new GetMethod(url);
        int ret = client.executeMethod(getMethod);
        String data = getMethod.getResponseBodyAsString();
        System.out.println(data);
        return data;
    }

    public static List<ClusterNode> getClusterData(String ip, int port, String username, String password) {
        List<ClusterNode> list = new ArrayList<>();
        String url = "http://" + ip + ":" + port + "/api/nodes";
        System.out.println(url);
        try {
            String urlData = HttpUtils.httpGet(url, username, password);
            paseClusters(urlData, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(list);
        return list;
    }

    private static void parseClusters(String urlData, List<ClusterNode> list) {
        JsonParser parser = new JsonParse();
        JSONArray jsonArray = (JSONArray)parser.parse(urlData);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObjectTemp = jsonArray.get(i).getAsJsonObject();
            ClusterNode clusterNode = new ClusterNode();
            cluster.setDiskFree(jsonObjectTemp.get("disk_free").getAsLong());
            cluster.setDiskFreeLimit(jsonObjectTemp.get("disk_free_limit").getAsLong());

        }
    }
}
