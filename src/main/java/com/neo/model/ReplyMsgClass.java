package com.neo.model;
import java.lang.Thread;
import org.apache.http.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class ReplyMsgClass implements Runnable {
    private String url;
    private String body;

    public ReplyMsgClass(String url, String body) {
        this.url = url;
        this.body=body;
    }

    public void run(){
        try {
            Random r = new Random();
            int waitMillisSeconds = r.nextInt(10000);
            Thread.sleep(1000+waitMillisSeconds);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(this.url);
//                HttpPost post = new HttpPost("http://192.168.5.223:28080/vuln");
            post.setHeader("Content-Type", "application/json");
            StringEntity entity = new StringEntity(this.body, StandardCharsets.UTF_8);
            post.setEntity(entity);
            HttpResponse resp = httpClient.execute(post);
            HttpEntity httpEntity = resp.getEntity();
            System.out.println("Response:" + EntityUtils.toString(httpEntity, "UTF-8"));
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
