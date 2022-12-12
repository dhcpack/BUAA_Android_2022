package com.example.hang.ports;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
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

    //判断是否联网
    public static boolean checkConnectNetwork(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getActiveNetworkInfo();
        return net != null && net.isConnected();
    }

    /*
     * GET
     * */
    public static Object httpGet(String url, ArrayList<String> params, boolean isArray) throws IOException {
        GetRunnable getRunnable = new GetRunnable(url, params, isArray);
        Thread thread = new Thread(getRunnable, "getThread");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isArray) {
            return getRunnable.jsonArray;
        } else {
            return getRunnable.jsonObject;
        }
    }

    static final class GetRunnable implements Runnable {
        private final OkHttpClient okHttpClient;
        private final String getUrl;
        private final boolean isArray;
        private JSONObject jsonObject;
        private JSONArray jsonArray;


        public GetRunnable(String url, ArrayList<String> params, boolean isArray) {
            okHttpClient = new OkHttpClient();
            StringBuilder getUrl = new StringBuilder(url);
            for (String s : params) {
                getUrl.append(s).append('/');
            }
            this.getUrl = getUrl.toString();
            System.out.println(this.getUrl);
            this.isArray = isArray;
        }

        @Override
        public void run() {
            //创造http请求
            Request request = new Request.Builder().get()
                    .url(getUrl)
                    .build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                System.out.println(response.message());
                if (isArray) {
                    this.jsonArray = new JSONArray(response.body().string());
                } else {
                    this.jsonObject = new JSONObject(response.body().string());
                }
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
            System.out.println(this.url);
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
    public static JSONObject httpPut(String url, HashMap<String, String> params) throws IOException {
        PutRunnable putRunnable = new PutRunnable(url, params);
        Thread thread = new Thread(putRunnable, "putThread");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return putRunnable.jsonObject;
    }

    static final class PutRunnable implements Runnable {
        private final OkHttpClient okHttpClient;
        private final String url;
        private final JSONObject jsonBody;
        private JSONObject jsonObject;

        public PutRunnable(String url, HashMap<String, String> params) {
            okHttpClient = new OkHttpClient();
            this.url = url;
            System.out.println(this.url);
            this.jsonBody = new JSONObject(params);
        }

        @Override
        public void run() {
            //创造http请求
            Request request = new Request.Builder()
                    .url(this.url)
                    .put(RequestBody.create(MediaType.parse("application/json"), jsonBody.toString()))
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
     * DELETE
     * */
    public static JSONObject httpDelete(String url, ArrayList<String> params) throws IOException {
        DeleteRunnable deleteRunnable = new DeleteRunnable(url, params);
        Thread thread = new Thread(deleteRunnable, "deleteThread");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return deleteRunnable.jsonObject;
    }

    static final class DeleteRunnable implements Runnable {
        private final OkHttpClient okHttpClient;
        private final String deleteUrl;
        private JSONObject jsonObject;


        public DeleteRunnable(String url, ArrayList<String> params) {
            okHttpClient = new OkHttpClient();
            StringBuilder deleteUrl = new StringBuilder(url);
            for (String s : params) {
                deleteUrl.append(s).append('/');
            }
            this.deleteUrl = deleteUrl.toString();
            System.out.println(this.deleteUrl);
        }

        @Override
        public void run() {
            //创造http请求
            Request request = new Request.Builder().delete()
                    .url(deleteUrl)
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
}