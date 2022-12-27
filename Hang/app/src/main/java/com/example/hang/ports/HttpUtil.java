package com.example.hang.ports;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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

    /*
     * 上传文件
     * */
    /*
     * POST
     * */
    public static JSONObject postFile(File file, String fileType) throws IOException {
        String url = Ports.postFileUrl + fileType + "/";
        PostFileRunnable postFileRunnable = new PostFileRunnable(file, url);
        Thread thread = new Thread(postFileRunnable, "postThread");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return postFileRunnable.jsonObject;
    }

    static final class PostFileRunnable implements Runnable {
        private final File file;
        private final String url;
        private final OkHttpClient okHttpClient;
        private JSONObject jsonObject;

        public PostFileRunnable(File file, String url) {
            okHttpClient = new OkHttpClient();
            this.file = file;
            this.url = url;
        }

        @Override
        public void run() {
            System.out.println(this.url);
            // 构建请求体
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", "file", RequestBody.create(MediaType.parse("multipart/form-data"), file)).build();

            //创造http请求
            Request request = new Request.Builder()
                    .url(this.url)
                    .post(requestBody)
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
     * 得到文件
     * */
    /*
     * GET
     * */
    public static void getFile(String fileName, String fileType, Context context) throws IOException {
        String url = Ports.getFileUrl + fileName + "/" + fileType + "/";
        fileName += ".";
        fileName += fileType;
        GetFileRunnable getFileRunnable = new GetFileRunnable(url, fileName, context);
        Thread thread = new Thread(getFileRunnable, "postThread");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static final class GetFileRunnable implements Runnable {
        private final String url;
        private final OkHttpClient okHttpClient;
        private final Context context;
        private final String fileName;

        public GetFileRunnable(String url, String fileName, Context context) {
            okHttpClient = new OkHttpClient();
            this.url = url;
            this.context = context;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            //创造http请求
            Request request = new Request.Builder()
                    .url(this.url)
                    .build();
            Response response = null;
            try {
                System.out.println(this.url);
                response = okHttpClient.newCall(request).execute();
                System.out.println(response.message());
                File file = new File(this.context.getFilesDir().getPath() + "/" + fileName);
                System.out.println(this.context.getFilesDir().getPath() + "/" + fileName);

                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}