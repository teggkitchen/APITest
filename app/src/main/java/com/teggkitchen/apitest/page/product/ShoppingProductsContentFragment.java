package com.teggkitchen.apitest.page.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.base.BaseFragment;
import com.teggkitchen.apitest.config.Config;
import com.teggkitchen.apitest.helper.ShoppingHelper;
import com.teggkitchen.apitest.model.Products;
import com.teggkitchen.apitest.util.MetricsHelper;
import com.teggkitchen.apitest.util.Util;

public class ShoppingProductsContentFragment extends BaseFragment {
    private View view;
    private ImageView imgItem,imgAdd, imgSub;
    private TextView txtTitle, txtPrice;
    private EditText edtNum;
    private RelativeLayout bottom;

    // data
    private Products product;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shopping_products_content, container, false);

        initView();
        initParam();
        initListener();
        return view;
    }


    @Override
    protected void initView() {
        baseActivity.setTitle("商品");
        imgItem = (ImageView)view.findViewById(R.id.img_item);
        txtTitle = (TextView)view.findViewById(R.id.txt_title);
        txtPrice = (TextView)view.findViewById(R.id.txt_price);
        edtNum = (EditText)view.findViewById(R.id.edt_num);
        imgAdd = (ImageView) view.findViewById(R.id.img_add);
        imgSub = (ImageView) view.findViewById(R.id.img_sub);
        bottom = (RelativeLayout) view.findViewById(R.id.bottom);

        // 設定MetricsHelper
        DisplayMetrics metrics = new DisplayMetrics();
        baseActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        MetricsHelper.density = metrics.density;
        MetricsHelper.screenWidth = metrics.widthPixels;
        MetricsHelper.screenHeight = metrics.heightPixels;




    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                baseActivity.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void initParam() {
// 取得Fragment參數
        Bundle bundle = getArguments();
        if (bundle != null) {
            // 取得商品資訊
            product = (Products) bundle.getSerializable("PRODUCT");
            txtTitle.setText(product.getName());
            txtPrice.setText("$ "+product.getPrice());
            if (!Util.isEmpty(product.getImg())) {
                Picasso.get().load(Config.IMAGE_SHOW + product.getImg()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(imgItem);

            }

        }

    }

    @Override
    protected void initListener() {

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(edtNum.getText().toString())>0){
                    edtNum.setText((Integer.valueOf(edtNum.getText().toString())+1)+"");

                }
            }
        });

        imgSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(edtNum.getText().toString())>1){
                    edtNum.setText((Integer.valueOf(edtNum.getText().toString())-1)+"");
                }
            }
        });


        edtNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Util.isEmpty(charSequence)){
                    edtNum.setText("1");
                }
                if (charSequence.length()>3){
                    edtNum.setText("999");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(edtNum.getText().toString())<1)return;

                if (ShoppingHelper.isShoppingCartItemRepeat(baseActivity,product)){
                    Util.show_toast(baseActivity,"商品已存在");
                    return;
                }
                // 新增
                ShoppingHelper.addShoppingCartItem(baseActivity,product,Integer.valueOf(edtNum.getText().toString()));

                //取得總筆數
//                Log.e("count: ",ShoppingHelper.getShoppingCartCount(baseActivity)+"");

                //刪除單一Item
//                ShoppingHelper.deleteShoppingCartItem(baseActivity,product);

                //清除
//                ShoppingHelper.clearShoppingCartItem(baseActivity);


                Util.show_toast(baseActivity,"加入購物車");
                baseActivity.finish();
            }
        });
    }


}
