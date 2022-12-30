package com.example.hang.ui.mine.myBooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import com.alibaba.fastjson.JSON;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hang.ui.mine.utils.function.GlideEngine;
import com.example.hang.R;
import com.example.hang.ui.mine.utils.view.SubmitButton;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddQuestionImageActivity extends AppCompatActivity {
    private EditText question_input;
    private ImageView image_input;
    private SubmitButton btn_confirm;

    private String question;
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_image);

        setTitleBar("添加题目");
        question_input = findViewById(R.id.question_input);
        image_input = findViewById(R.id.image_input);
        image_input.setOnClickListener(view -> photoAndAll(image_input));
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(view -> {
            question = question_input.getText().toString().trim();
            answer = "";
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("ques", question);
            bundle.putString("ans", answer);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            toast("提交成功");
            finish();
        });
    }

    private void photoAndAll(ImageView imageView){
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine()).setMaxSelectNum(1)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        Log.e("leo","图片路径"+result.get(0).getPath());
                        Log.e("leo","绝对路径"+result.get(0).getRealPath());
                        Glide.with(AddQuestionImageActivity.this).load(result.get(0).getPath()).into(imageView);
                        // 将bitmap图片传入后端
                        imageUpLoad(result.get(0).getRealPath());
                        //SubmitPicture(result.get(0).getRealPath());
                    }

                    @Override
                    public void onCancel() {
                        toast("error");
                    }
                });
    }

    public static void imageUpLoad(String localPath) {
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        File f = new File(localPath);
        builder.addFormDataPart("files", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
        final MultipartBody requestBody = builder.build();
        //构建请求
        final Request request = new Request.Builder()
                .url("后端接口")//地址
                .post(requestBody)//添加请求体
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("上传失败:e.getLocalizedMessage() = " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                Object model1 = JSON.parseObject(response.body().string());
                //pmodel model1 = JSON.parseObject(response.body().string(), pmodel.class);
                System.out.println(model1);
                Log.e("leo",JSON.toJSONString(model1));
                //pmodel model2 = JSON.parseObject(JSON.toJSONString(model), pmodel.class);

            }
        });
    }


    private void  SubmitPicture(String path){
        String url = "后端接口";
        String token="token，没有要求则无需写入";
        RequestParams params = new RequestParams(url);
        params.addHeader("Authorization",token);

        //设置表单传送
        params.setMultipart(true);
        params.addBodyParameter("file",new File(path));
        params.addParameter("fileType","PICTURE");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("leo","2222"+params);
                Toast.makeText(AddQuestionImageActivity.this, "上传成功!", Toast.LENGTH_SHORT).show();
                Log.e("leo","返回"+result);
                Object model = JSON.parseObject(result);
                System.out.println(model);
                //pmodel model = JSON.parseObject(result, pmodel.class);
                //PictureArr.add(model.getData().get(0).getUrl());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("leo","失败"+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    public void setTitleBar(String title) {
        //设置顶部菜单栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            TextView tv = new TextView(this);
            tv.setText(title);
            tv.setTextSize(20);
            tv.setTextColor(this.getResources().getColor(R.color.white));
            tv.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
            tv.setGravity(Gravity.CENTER);
            actionBar.setHomeButtonEnabled(true);//设置左上角的图标是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
            actionBar.setDisplayShowCustomEnabled(true);// 使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用
            actionBar.setCustomView(tv, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toast(String str) {
        Toast.makeText(AddQuestionImageActivity.this, str, Toast.LENGTH_SHORT).show();
        btn_confirm.reset();
    }
}
