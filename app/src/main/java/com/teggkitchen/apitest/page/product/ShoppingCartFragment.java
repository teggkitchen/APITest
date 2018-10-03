package com.teggkitchen.apitest.page.product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teggkitchen.apitest.ItemListClickListener;
import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.adapter.ShoppingCartAdapter;
import com.teggkitchen.apitest.adapter.ShoppingProductsAdapter;
import com.teggkitchen.apitest.adapter.UpdateProductsAdapter;
import com.teggkitchen.apitest.base.ActionBarActivity;
import com.teggkitchen.apitest.base.BaseFragment;
import com.teggkitchen.apitest.config.Config;
import com.teggkitchen.apitest.helper.MemberHelper;
import com.teggkitchen.apitest.helper.ShoppingHelper;
import com.teggkitchen.apitest.model.OrderCreate;
import com.teggkitchen.apitest.model.Products;
import com.teggkitchen.apitest.model.ResponseInfo;
import com.teggkitchen.apitest.page.member.MemberSignInActivity;
import com.teggkitchen.apitest.util.ItemOffsetDecoration;
import com.teggkitchen.apitest.util.MetricsHelper;
import com.teggkitchen.apitest.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShoppingCartFragment extends BaseFragment {
    private View view;
    private TextView txtCount,txtNoDataLabel;
    private RecyclerView recyclerView;
    private RelativeLayout bottom;

    // Adapter
    private ShoppingCartAdapter adapter;

    // data
    private List<Products> products;
    private List<Integer> nums;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        setHasOptionsMenu(true);
        initView();
        initParam();
        initListener();
        return view;
    }


    @Override
    protected void initView() {
        baseActivity.setTitle("購物車");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        bottom = (RelativeLayout) view.findViewById(R.id.bottom);
        txtNoDataLabel= (TextView) view.findViewById(R.id.txt_no_data_label);
        txtCount = (TextView) view.findViewById(R.id.txt_count);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(baseActivity, DividerItemDecoration.VERTICAL));
        products = new ArrayList<>();
        nums = new ArrayList<>();


        // 設定MetricsHelper
        DisplayMetrics metrics = new DisplayMetrics();
        baseActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        MetricsHelper.density = metrics.density;
        MetricsHelper.screenWidth = metrics.widthPixels;
        MetricsHelper.screenHeight = metrics.heightPixels;


    }

    @Override
    protected void initParam() {

        if (!Util.isEmpty(ShoppingHelper.getShoppingCartProducts(baseActivity)) &&
                !Util.isEmpty(ShoppingHelper.getShoppingCartNums(baseActivity))) {
            products = ShoppingHelper.getShoppingCartProducts(baseActivity);
            nums = ShoppingHelper.getShoppingCartNums(baseActivity);
            setRecyclerViewData();
            getPriceCount();

            showDataPage();
        }else {
            hideDataPage();
        }

        OkHttpClient client = new OkHttpClient();


    }

    @Override
    protected void initListener() {
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isEmpty(MemberHelper.getToken(baseActivity))){

                    Bundle args = new Bundle();
                    args.putString(Config.GOPAGE_KEY_GOBACK,Config.GOPAGE_GOBACK);
                    show_toast("請登入");
                    baseActivity.goPage(MemberSignInActivity.class,args,false,baseActivity.PAGE_DEFAULT_ANIMATION);
                }
                if (Util.isEmpty(ShoppingHelper.convertShoppingCartToOrder(baseActivity,"test")))return;
                String token = MemberHelper.getToken(baseActivity);
                OrderCreate orderCreate = ShoppingHelper.convertShoppingCartToOrder(baseActivity,token);
                if (!Util.isEmpty(orderCreate)){
                    sendRequest(new GsonBuilder().create().toJson(orderCreate));
                }
            }
        });
    }


    private void setRecyclerViewData() {
        adapter = new ShoppingCartAdapter(products, nums, new ItemListClickListener() {
            @Override
            public void onItemClicked(final int index, String element) {
                if (element.equals(ItemListClickListener.ELEMENT_REMOVE)) {
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
                                    ShoppingHelper.deleteShoppingCartItem(baseActivity, products.get(index));
                                    products.remove(index);
                                    nums.remove(index);
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                    getPriceCount();
                                    if (Util.isEmpty(products))hideDataPage();
                                }
                            })
                            .create()
                            .show();
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_clear, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_clear);
        RelativeLayout layoutClear = (RelativeLayout) menuItem.getActionView().findViewById(R.id.layout_clear);
        layoutClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Util.isEmpty(products))return;

                AlertDialog.Builder alertDialog;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alertDialog = new AlertDialog.Builder(baseActivity, android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    alertDialog = new AlertDialog.Builder(baseActivity);
                }

                alertDialog.setMessage("是否清空購物車")
                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("是", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ShoppingHelper.clearShoppingCartItem(baseActivity);
                                products.clear();
                                nums.clear();
                                adapter.notifyDataSetChanged();
                                getPriceCount();
                                dialog.dismiss();
                                if (Util.isEmpty(products))hideDataPage();
                            }
                        })
                        .create()
                        .show();

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //進不來
            case R.id.action_clear: {
                Log.e("購物車", "清除");
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }


    private void getPriceCount() {
        txtCount.setText("$ "+ShoppingHelper.getShoppingCartPriceCount(baseActivity));
    }

    private void showDataPage(){
        recyclerView.setVisibility(View.VISIBLE);
        bottom.setVisibility(View.VISIBLE);
        txtCount.setVisibility(View.VISIBLE);
        txtNoDataLabel.setVisibility(View.GONE);
    }

    private void hideDataPage(){
        recyclerView.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);
        txtCount.setVisibility(View.GONE);
        txtNoDataLabel.setVisibility(View.VISIBLE);
    }


    private void sendRequest(String json){
        Log.e("sendRequest",json);
        OkHttpClient client = new OkHttpClient();

        // 設置參數
        RequestBody body = RequestBody.create(Config.JSON_TYPE,json);

        // 設置連線
        Request request = new Request.Builder()
                .post(body)
                .url(Config.ORDER_ADD)
                .build();

        // 執行連線與監聽回傳
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                baseActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        show_toast("傳送失敗");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                baseActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("狀態","訂單發送");
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                        // Json 解析
                        ResponseInfo responseInfo = gson.fromJson(res, ResponseInfo.class);
                        if (responseInfo.getCode() == Config.CODE_ERROR) {
                            show_toast(responseInfo.getMsg());
                            return;
                        }

                        if (responseInfo.getCode() == Config.CODE_SUCCESS) {
                            show_toast("訂單成立");

                            //清空購物車
                            ShoppingHelper.clearShoppingCartItem(baseActivity);
                            baseActivity.finish();
                        }

                    }
                });

            }
        });
    }


}
