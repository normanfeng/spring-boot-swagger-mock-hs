package com.neo.controller;


import com.neo.model.ReplyMsgClass;
import io.swagger.annotations.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.io.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "恒生mock server接口", description = "恒生mock server API", position = 100, protocols = "http")
@RestController
@RequestMapping(value = "/")
public class HSController {
    Logger logger = LoggerFactory.getLogger(getClass());

    private void writeFile(String filename, String msg) {
//        System.out.println(msg);
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
            out.write(msg);
            out.write("\n");
            out.flush();
            out.close();
        }
        catch (IOException e) {
            System.out.println("Exception while writing file:"+ e);
        }
    }

    void parseBodyJsonString(String requestBody)
    {
        String dataStr="";
        Iterator<String> keys;
        String filename = "./data.txt";
        try {


            Object typeObject = new JSONTokener(requestBody).nextValue();
            if (typeObject instanceof org.json.JSONArray) {
                JSONArray jsonArray = new JSONArray(requestBody);
                for(int i=0; i < jsonArray.length(); i++)
                {
//                writeFile(filename, "------------------------------------------------------------------------------");
                    System.out.println("------------------------------------------------------------------------------");
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if( jsonObject.has("body") ) {
                        jsonObject = jsonObject.getJSONObject("body");
//                logger.info( "\nbody:" + jsonObject.getString("body"));
//                logger.info( "\nbody:" + jsonBody.toString());
                    }
                    keys = jsonObject.keys();


                    while (keys.hasNext())
                    {
                        String key = keys.next().toString();
                        String value = jsonObject.optString( key);
//                    writeFile(filename, key + ":" + value);
                        System.out.println(key + ":" + value);
                        dataStr = dataStr + key + ":" + value + ", ";
                    }

                }
            } else if (typeObject instanceof org.json.JSONObject) {
                JSONObject object = new JSONObject(requestBody);
                keys = object.keys();
                while (keys.hasNext())
                {
                    String key = keys.next().toString();
                    String value = object.optString( key);
//                    writeFile(filename, key + ":" + value);
                    System.out.println(key + ":" + value);
                    dataStr = dataStr + key + ":" + value + ", ";
                }
            }

            writeFile(filename, dataStr);

        }
        catch(JSONException ex)
        {
            System.out.println("read json string error!");
            ex.printStackTrace();



        }

    }


    @PostMapping("/urlencoded")
//    public String signout(@RequestBody String requestBody, HttpServletRequest httpServletRequest) {
//        System.out.println("查询字符串参数: " + httpServletRequest.getQueryString());
//        System.out.println("requestBody: " + requestBody);
//        return "success";
//    }
    public ResponseEntity<?> post(HttpServletRequest servletRequest) throws Exception {
        String url = servletRequest.getRequestURI().replaceFirst("/", "");

        HttpHeaders httpHeaders = (Collections
                .list(servletRequest.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(servletRequest.getHeaders(h)),
                        (oldValue, newValue) -> newValue,
                        HttpHeaders::new
                ))
        );

        HttpEntity<?> request;
        String filename = "./data.txt";

        System.out.println("=================================================================================================");
        writeFile(filename, "=================================================================================================");

        String body = servletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        if (!body.isEmpty()) {
            request = new HttpEntity<>(body, httpHeaders);
        } else {
            Map<String, String> payload = new HashMap<>();
//            Stream.of(servletRequest.getParameterMap()).forEach(stringMap -> stringMap.forEach((k, v) ->
//                    Stream.of(v).forEach(value -> payload.put(k, value))
//            ));
//            request = new HttpEntity<>(payload, httpHeaders);
            Stream.of(servletRequest.getParameterMap()).forEach(stringMap -> stringMap.forEach((k, v) ->
                    Stream.of(v).forEach(value -> System.out.println(k + ":" + value))
            ));
            Stream.of(servletRequest.getParameterMap()).forEach(stringMap -> stringMap.forEach((k, v) ->
                    Stream.of(v).forEach(value -> writeFile(filename, k + ":" + value))
            ));


            ;
            request = new HttpEntity<>(payload, httpHeaders);
        }
//        System.out.println("request:" +request.getBody().toString());

        return ResponseEntity.ok("Success");
    }

    @RequestMapping(value = {"/combined", "/combine", "/vuln", "/vuln1", "/vuln2", "/sync", "/async"}, method = RequestMethod.POST)
    public String combined(@RequestBody String requestBody) {
        System.out.println("进入 combined接口");
//        logger.info("Info:\t" + "进入combined接口");


        String filename = "./data.txt";
        String resultStr = new String( "[\n" +
                "    {\n" +
                "        \"status\": \"200\",\n" +
                "        \"body\": {}\n" +
                "    },\n" +
                "    {\n" +
                "        \"status\": \"400\",\n" +
                "        \"body\": {}\n" +
                "    }\n" +
                "]");
        String dataStr="";
        Iterator<String> keys;

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String logStr = "";
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        response.setStatus(200);

        Enumeration<?> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            System.out.println("-\t Params: " + paramName + "=" + paramValue);
            dataStr = dataStr + paramName + ":" + paramValue + ", ";
        }



        System.out.println("-\t Reqest Body:" + requestBody);

//        response.addHeader("X-Request-Id", request.getHeader("X-Request-Id"));

//        writeFile(filename, "=================================================================================================");
        System.out.println("=================================================================================================");
        if(request.getHeader("X-Async-Api-Callback-Url") != null)
        {
//            writeFile(filename, "X-Async-Api-Callback-Url:" + request.getHeader("X-Async-Api-Callback-Url"));
            System.out.println("X-Async-Api-Callback-Url:" + request.getHeader("X-Async-Api-Callback-Url"));
            ReplyMsgClass reply = new ReplyMsgClass(request.getHeader("X-Async-Api-Callback-Url"), resultStr );
            reply.run();
        }


        try {


            Object typeObject = new JSONTokener(requestBody).nextValue();
            if (typeObject instanceof org.json.JSONArray) {
                JSONArray jsonArray = new JSONArray(requestBody);
                for(int i=0; i < jsonArray.length(); i++)
                {
//                writeFile(filename, "------------------------------------------------------------------------------");
                    System.out.println("------------------------------------------------------------------------------");
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if( jsonObject.has("body") ) {
                        jsonObject = jsonObject.getJSONObject("body");
//                logger.info( "\nbody:" + jsonObject.getString("body"));
//                logger.info( "\nbody:" + jsonBody.toString());
                    }
                    keys = jsonObject.keys();


                    while (keys.hasNext())
                    {
                        String key = keys.next().toString();
                        String value = jsonObject.optString( key);
//                    writeFile(filename, key + ":" + value);
                        System.out.println(key + ":" + value);
                        dataStr = dataStr + key + ":" + value + ", ";
                    }

                }
            } else if (typeObject instanceof org.json.JSONObject) {
                JSONObject object = new JSONObject(requestBody);
                keys = object.keys();
                while (keys.hasNext())
                {
                    String key = keys.next().toString();
                    String value = object.optString( key);
//                    writeFile(filename, key + ":" + value);
                    System.out.println(key + ":" + value);
                    dataStr = dataStr + key + ":" + value + ", ";
                }
            }

            writeFile(filename, dataStr);

        }
        catch(JSONException ex)
        {
            System.out.println("read json string error!");
            ex.printStackTrace();



        }


        return resultStr;
    }



}