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
import com.teggkitchen.apitest.ItemListClickListener;
import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.adapter.UpdateProductsAdapter;
import com.teggkitchen.apitest.base.ActionBarActivity;
import com.teggkitchen.apitest.base.BaseFragment;
import com.teggkitchen.apitest.config.Config;
import com.teggkitchen.apitest.model.Products;
import com.teggkitchen.apitest.model.ResponseInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateProductsFragment extends BaseFragment {
    private View view;
    private ImageView imgItem;
    private EditText edtName, edtPrice;
    private Button btnSelectImg;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private boolean isReload = false;

    // Adapter
    private UpdateProductsAdapter adapter;

    // data
    private List<Products> datas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_products, container, false);
        initView();
        initParam();
        initListener();
        return view;
    }


    @Override
    protected void initView() {
        baseActivity.setTitle("編輯商品");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(baseActivity);
        recyclerView.setLayoutManager(layoutManager);
        datas = new ArrayList<>();
    }

    @Override
    protected void initParam() {
        OkHttpClient client = new OkHttpClient();

        // 設置連線
        Request request = new Request.Builder()
                .get()
                .url(Config.PRODUCT_SHOW)
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
                            Products[] dataArray = gson.fromJson(getData, Products[].class);
                            datas = new ArrayList<>(Arrays.asList(dataArray));
                            recyclerView.addItemDecoration(new DividerItemDecoration(baseActivity, DividerItemDecoration.VERTICAL));
                            setRecyclerViewData();
                            isReload = true;

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
        adapter = new UpdateProductsAdapter(datas, new ItemListClickListener() {
            @Override
            public void onItemClicked(final int index, String element) {
                if (element.equals(ItemListClickListener.ELEMENT_ITEM)) {


                    final Bundle args = new Bundle();
                    args.putSerializable("PRODUCT", datas.get(index));

                    baseActivity.goContainerPage(ActionBarActivity.class,
                            UpdateProductsContentFragment.class.getName(), args, false, baseActivity.PAGE_DEFAULT_ANIMATION);


                } else if (element.equals(ItemListClickListener.ELEMENT_REMOVE)) {
                    AlertDialog.Builder alertDialog;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        alertDialog = new AlertDialog.Builder(baseActivity, android.R.style.Theme_Material_Light_Dialog_Alert);
                    } else {
                        alertDialog = new AlertDialog.Builder(baseActivity);
                    }


                    alertDialog.setMessage("是否刪除")
                            .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("是", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteProduct(datas.get(index).getId());
                                    datas.remove(index);
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }


    private void deleteProduct(int id) {
        Log.e("deleteProduct ID:", id + "");

        OkHttpClient client = new OkHttpClient();

        // 設置連線
        Request request = new Request.Builder()
                .delete()
                .url(Config.PRODUCT_DELETE + "/" + id)
                .build();

        // 執行連線與監聽回傳
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                baseActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        show_toast("刪除失敗");
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
                            show_toast("刪除成功");

                        }

                    }
                });

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isReload) resetDatas();
    }


    private void resetDatas() {
        OkHttpClient client = new OkHttpClient();

        // 設置連線
        Request request = new Request.Builder()
                .get()
                .url(Config.PRODUCT_SHOW)
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
                            Products[] dataArray = gson.fromJson(getData, Products[].class);
                            datas.clear();
                            for (Products product : dataArray
                                    ) {
                                datas.add(product);
                            }
                            adapter.notifyDataSetChanged();

                        }
                    }
                });

            }
        });
    }


}
