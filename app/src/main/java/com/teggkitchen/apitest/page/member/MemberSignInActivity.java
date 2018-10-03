package com.teggkitchen.apitest.page.member;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.base.BaseActivity;
import com.teggkitchen.apitest.base.DrawerMenuActivity;
import com.teggkitchen.apitest.config.Config;
import com.teggkitchen.apitest.helper.MemberHelper;
import com.teggkitchen.apitest.model.Member;
import com.teggkitchen.apitest.model.ResponseInfo;
import com.teggkitchen.apitest.page.product.AddProductFragment;
import com.teggkitchen.apitest.page.product.ShoppingProductsFragment;
import com.teggkitchen.apitest.util.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MemberSignInActivity extends BaseActivity {
    private Button btnSignIn, btnSignUp;
    private ImageView imgClose;
    private EditText editEmail,editPw;
    private String argsGoMessgae;

    //定義interface
    public interface Listener {
        void getURL(String url);
    }




    //定義Class
    public class SendData {
        Listener listener;

        public SendData(Listener listener) {
            this.listener = listener;
        }
        public void sendURL(String url){
            listener.getURL(url);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_signin);

        initView();
        initParam();
        initListener();
    }

    @Override
    protected void initView() {
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPw= (EditText) findViewById(R.id.edit_pw);
        imgClose = (ImageView) findViewById(R.id.img_close);
    }

    @Override
    protected void initListener() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                OkHttpClient client = new OkHttpClient();
                FormBody formBody = new FormBody.Builder()
                        .add("email",editEmail.getText().toString())
                        .add("password",editPw.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .post(formBody)
                        .url(Config.MEMBER_SIGNIN)
                        .build();
                Call call = client.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                show_toast("登入失敗");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String res = response.body().string();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                                // Json 解析
                                ResponseInfo responseInfo =gson.fromJson(res, ResponseInfo.class);
                                if (responseInfo.getCode()==Config.CODE_ERROR){
                                    show_toast(responseInfo.getMsg().toString());
                                    return;
                                }
                                String getData = gson.toJson(responseInfo.getData());
                                Member member =gson.fromJson(getData, Member.class);
                                if (Util.isEmpty(member))return;

                                //SP儲存 - member json
                                MemberHelper.setMember(MemberSignInActivity.this,getData);
                                MemberHelper.setToken(MemberSignInActivity.this,member.getToken());



                                show_toast("登入成功");


                                touchRC();

                                if (!Util.isEmpty(argsGoMessgae)){
                                    if (argsGoMessgae.equals(Config.GOPAGE_GOBACK))finish();
                                }else {
                                    goContainerPage(DrawerMenuActivity.class, ShoppingProductsFragment.class.getName(), null, true, PAGE_NO_ANIMATION);
                                }


                            }
                        });

                    }
                });

            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goPage(MemberSignUpActivity.class, false);
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void initParam() {
        // 取得參數
        // 取得參數

        Intent intent = getIntent();
        if (intent != null)
        {
            Bundle bundle = intent.getBundleExtra(INTENT_BUNDLE_KEY);
            if (bundle != null)
            {
                argsGoMessgae = bundle.get(Config.GOPAGE_KEY_GOBACK).toString();

            }
        }

    }


    private void touchRC(){
        //觸發廣播
        Intent intent = new Intent();
        intent.setAction(Config.SEND_MENU_STATUS_CHANGE);
        intent.putExtra("status",Config.SEND_MENU_STATUS_CHANGE); //傳遞資料
        this.sendBroadcast(intent);
    }




}
