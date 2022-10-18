package com.example.hang.ports;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpUtil {

    /*
     * GET
     * */
    public static JSONObject httpGet(String url, ArrayList<String> params) throws IOException {
        GetRunnable getRunnable = new GetRunnable(url, params);
        Thread thread = new Thread(getRunnable, "getThread");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getRunnable.jsonObject;
    }

    static final class GetRunnable implements Runnable {
        private final OkHttpClient okHttpClient;
        private final String getUrl;
        private JSONObject jsonObject;


        public GetRunnable(String url, ArrayList<String> params) {
            okHttpClient = new OkHttpClient();
            StringBuilder getUrl = new StringBuilder(url);
            for (String s : params) {
                getUrl.append(s).append('/');
            }
            this.getUrl = getUrl.toString();
        }

        @Override
        public void run() {
            //创造http请求
            Request request = new Request.Builder().get()
                    .url(getUrl.toString())
                    .build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                System.out.println(response.message());
                this.jsonObject = new JSONObject(response.body().string());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * POST
     * */
    public static JSONObject httpPost(String url, HashMap<String, String> params) throws IOException {
        PostRunnable postRunnable = new PostRunnable(url, params);
        Thread thread = new Thread(postRunnable, "postThread");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return postRunnable.jsonObject;
    }

    static final class PostRunnable implements Runnable {
        private final OkHttpClient okHttpClient;
        private final String url;
        private final JSONObject jsonBody;
        private JSONObject jsonObject;

        public PostRunnable(String url, HashMap<String, String> params) {
            okHttpClient = new OkHttpClient();
            this.url = url;
            this.jsonBody = new JSONObject(params);
        }

        @Override
        public void run() {
            //创造http请求
            Request request = new Request.Builder()
                    .url(this.url)
                    .post(RequestBody.create(MediaType.parse("application/json"), jsonBody.toString()))
                    .build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                System.out.println(response.message());
                this.jsonObject = new JSONObject(response.body().string());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * PUT
    * */


    /*
    * DELETE
    * */
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//    public class Test {
//        public static void main(String[] args) throws Exception {
//            String url = "http://****";
//            String json = "{\"param\":\"value2\",\"key\":\"value\"}";
//            OkHttpClient client = new OkHttpClient().newBuilder() //
//                    .readTimeout(60, TimeUnit.SECONDS) // 设置读取超时时间
//                    .writeTimeout(60, TimeUnit.SECONDS) // 设置写的超时时间
//                    .connectTimeout(60, TimeUnit.SECONDS) // 设置连接超时时间
//                    .build();
//
//            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//            RequestBody body = RequestBody.create(json, JSON);
//            Request request = new Request.Builder().url(url).put(body).build();
//            try (Response response = client.newCall(request).execute()) {
//                System.out.println(response.body().string());
//            }
//        }
//    }


}