package com.teggkitchen.apitest.page.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import com.teggkitchen.apitest.adapter.ShoppingProductsAdapter;
import com.teggkitchen.apitest.base.ActionBarActivity;
import com.teggkitchen.apitest.base.BaseFragment;
import com.teggkitchen.apitest.config.Config;
import com.teggkitchen.apitest.helper.ShoppingHelper;
import com.teggkitchen.apitest.model.Products;
import com.teggkitchen.apitest.model.ResponseInfo;
import com.teggkitchen.apitest.util.ItemOffsetDecoration;
import com.teggkitchen.apitest.util.MetricsHelper;
import com.teggkitchen.apitest.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShoppingProductsFragment extends BaseFragment {
    private View view;
    private ImageView imgItem;
    private EditText edtName, edtPrice;
    private Button btnSelectImg;
    private RecyclerView recyclerView;

    // Adapter
    private ShoppingProductsAdapter adapter;

    // data
    private List<Products> datas;

    //購物車
    private TextView txtNum;
    private int productCount;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shopping_products, container, false);
        setHasOptionsMenu(true);
        initView();
        initParam();
        initListener();
        return view;
    }


    @Override
    protected void initView() {
        baseActivity.setTitle("購買商品");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemOffsetDecoration itemDecoration =
                new ItemOffsetDecoration(getContext(), R.dimen.grid_item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        datas = new ArrayList<>();


        // 設定MetricsHelper
        DisplayMetrics metrics = new DisplayMetrics();
        baseActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        MetricsHelper.density = metrics.density;
        MetricsHelper.screenWidth = metrics.widthPixels;
        MetricsHelper.screenHeight = metrics.heightPixels;


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
        adapter = new ShoppingProductsAdapter(datas, new ItemListClickListener() {
            @Override
            public void onItemClicked(final int index, String element) {
                if (element.equals(ItemListClickListener.ELEMENT_ITEM)) {

                    final Bundle args = new Bundle();
                    args.putSerializable("PRODUCT", datas.get(index));

                    baseActivity.goContainerPage(ActionBarActivity.class,
                            ShoppingProductsContentFragment.class.getName(), args, false, baseActivity.PAGE_DEFAULT_ANIMATION);
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_product, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_product);
        RelativeLayout layoutCart = (RelativeLayout) menuItem.getActionView().findViewById(R.id.layout_cart);
        txtNum = (TextView) menuItem.getActionView().findViewById(R.id.txt_num);
        int num = ShoppingHelper.getShoppingCartCount(baseActivity);
        txtNum.setText(num + "");
        layoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseActivity.onOptionsItemSelected(menuItem);
                baseActivity.goContainerPage(ActionBarActivity.class,
                        ShoppingCartFragment.class.getName(), null, false, baseActivity.PAGE_NO_ANIMATION);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("getItemId", item.getItemId() + "");
        switch (item.getItemId()) {
            //進不來
            case R.id.action_product: {
                // 顯示購物車首頁
//                goCartPage();
                Log.e("購物車", "購物車");
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        int num = ShoppingHelper.getShoppingCartCount(baseActivity);
        Log.e("num",num+"");
        //txtNum還未初始化，就不塞值
        if (!Util.isEmpty(txtNum)) txtNum.setText(num + "");
        // 更新預約數字
//        updateResvNumber();
    }


    /**
     * 更新預約數字
     */
    public void updateProductNumber() {
        if (txtNum != null) {
            //SP取得購物清單

//            Resv tempResv = ResvService.getTempResv(getActivity());
//            shopNumberText.setText(String.valueOf(tempResv.getResvRecp_resvSn().size()));
        }
    }


    /**
     * 設定預約數字
     */
    public void setProductNumber(int num) {
        productCount = num;
        txtNum.setText(String.valueOf(productCount));
    }

    /**
     * 回傳預約數字
     */
    public int getProductNumber() {
        return productCount;
    }


    /**
     * 更新預約數字
     */
    public void addTempProduct() {
        if (txtNum != null) {
            //SP取得購物清單

//            Resv tempResv = ResvService.getTempResv(getActivity());
//            shopNumberText.setText(String.valueOf(tempResv.getResvRecp_resvSn().size()));
        }
    }


}
