package com.teggkitchen.apitest;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teggkitchen.apitest.config.Config;
import com.teggkitchen.apitest.model.Products;
import com.teggkitchen.apitest.model.ResponseInfo;
import com.tingk.apibase.activity.TBaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends TBaseActivity {
//    private TextView txtTest;
    // 元件
    private RecyclerView recyclerView;

    // Adapter
    private ListAdapter adapter;

    // data
    private List<String> data1,data2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        OkHttpClient client = new OkHttpClient();

        Log.e("Connect",Config.PRODUCT_SHOW);
        Request request = new Request.Builder()
                .get()
                .url(Config.PRODUCT_SHOW)
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error",e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        txtTest.setText(res);

                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                        ResponseInfo responseInfo =gson.fromJson(res, ResponseInfo.class);
//                        List<Products> list = (List<Products>)responseInfo.getData();

                        String getData = gson.toJson(responseInfo.getData());
                        Products[] dataArray = gson.fromJson(getData,Products[].class);
                        List<Products> list = new ArrayList<>(Arrays.asList(dataArray));


                        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));
                        adapter = new ListAdapter(list, new ItemListClickListener() {
                            @Override
                            public void onItemClicked(int index, String element) {
                                Log.e("Item", index + "");

                            }
                        });

                        recyclerView.setAdapter(adapter);



                    }
                });
            }
        });

    }


}
