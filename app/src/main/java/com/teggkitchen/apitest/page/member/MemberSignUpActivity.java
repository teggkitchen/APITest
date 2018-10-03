package com.teggkitchen.apitest.page.member;

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
import com.teggkitchen.apitest.util.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MemberSignUpActivity extends BaseActivity {
    private Button btnSignUp;
    private ImageView imgBack, imgClose;
    private EditText editEmail, editPhone, editPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_signup);

        initView();
        initParam();
        initListener();
    }

    @Override
    protected void initView() {
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPhone = (EditText) findViewById(R.id.edit_phone);
        editPw = (EditText) findViewById(R.id.edit_pw);
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgClose = (ImageView) findViewById(R.id.img_close);
    }

    @Override
    protected void initListener() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                OkHttpClient client = new OkHttpClient();
                FormBody formBody = new FormBody.Builder()
                        .add("email", editEmail.getText().toString())
                        .add("phone", editPhone.getText().toString())
                        .add("password", editPw.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .post(formBody)
                        .url(Config.MEMBER_SIGNUP)
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
                                ResponseInfo responseInfo = gson.fromJson(res, ResponseInfo.class);
                                if (responseInfo.getCode()==Config.CODE_ERROR){
                                    show_toast(responseInfo.getMsg());
                                    return;
                                }
                                String getData = gson.toJson(responseInfo.getData());
                                Member member = gson.fromJson(getData, Member.class);
                                if (Util.isEmpty(member))return;

                                //儲存資訊
                                MemberHelper.setMember(MemberSignUpActivity.this,getData);
                                MemberHelper.setToken(MemberSignUpActivity.this,member.getToken());

                                show_toast("註冊成功");
                                goContainerPage(DrawerMenuActivity.class, AddProductFragment.class.getName(), null, true, PAGE_NO_ANIMATION);
                            }
                        });

                    }
                });

            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goContainerPage(DrawerMenuActivity.class, AddProductFragment.class.getName(), null, true, PAGE_NO_ANIMATION);
            }
        });

    }

    @Override
    protected void initParam() {

    }


}
