package com.teggkitchen.apitest.page.product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.teggkitchen.apitest.ItemListClickListener;
import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.adapter.OrdersAdapter;
import com.teggkitchen.apitest.adapter.OrdersContentAdapter;
import com.teggkitchen.apitest.base.ActionBarActivity;
import com.teggkitchen.apitest.base.BaseFragment;
import com.teggkitchen.apitest.config.Config;
import com.teggkitchen.apitest.helper.MemberHelper;
import com.teggkitchen.apitest.model.Orders;
import com.teggkitchen.apitest.model.OrdersContent;
import com.teggkitchen.apitest.model.Products;
import com.teggkitchen.apitest.model.ResponseInfo;
import com.teggkitchen.apitest.page.member.MemberSignInActivity;
import com.teggkitchen.apitest.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrdersContentFragment extends BaseFragment {
    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private int orderId;

    // Adapter
    private OrdersContentAdapter adapter;

    // data
    private List<OrdersContent> datas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orders_content, container, false);
        setHasOptionsMenu(true);
        initView();
        initParam();
        initListener();
        return view;
    }


    @Override
    protected void initView() {
        baseActivity.setTitle("查詢訂單明細");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(layoutManager);
        datas = new ArrayList<>();
    }

    @Override
    protected void initParam() {
        // 取得Fragment參數
        Bundle bundle = getArguments();
        if (bundle != null) {
            // 取得商品資訊
            orderId = bundle.getInt("ORDER");
        }

        Log.e("orderId",orderId+"");

        OkHttpClient client = new OkHttpClient();

        //判斷登入狀態
        if (Util.isEmpty(MemberHelper.getToken(baseActivity))) {

            Bundle args = new Bundle();
            args.putString(Config.GOPAGE_KEY_GOBACK, Config.GOPAGE_GOBACK);
            show_toast("請登入");
            baseActivity.goPage(MemberSignInActivity.class, args, false, baseActivity.PAGE_DEFAULT_ANIMATION);
        }

        //設置參數
        FormBody formBody = new FormBody.Builder()
                .add("order_id", orderId+"")
                .build();

        // 設置連線
        Request request = new Request.Builder()
                .post(formBody)
                .url(Config.ORDER_DETAIL_SHOW)
                .build();

        // 執行連線與監聽回傳
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                baseActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        show_toast("沒有資料");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                baseActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                        // Json 解析
                        ResponseInfo responseInfo = gson.fromJson(res, ResponseInfo.class);
                        if (responseInfo.getCode() == Config.CODE_ERROR) {
                            show_toast(responseInfo.getMsg());
                            return;
                        }
                        if (responseInfo.getCode() == Config.CODE_SUCCESS) {
                            String getData = gson.toJson(responseInfo.getData());
                            datas = gson.fromJson(getData, new TypeToken<List<OrdersContent>>() {
                            }.getType());
                            recyclerView.addItemDecoration(new DividerItemDecoration(baseActivity, DividerItemDecoration.VERTICAL));
                            setRecyclerViewData();

                        }

                    }
                });

            }
        });


    }

    @Override
    protected void initListener() {

    }


    private void setRecyclerViewData() {
        adapter = new OrdersContentAdapter(datas);
        recyclerView.setAdapter(adapter);
    }

}
