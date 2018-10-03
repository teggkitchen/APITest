package com.teggkitchen.apitest.page.product;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.base.BaseFragment;
import com.teggkitchen.apitest.config.Config;
import com.teggkitchen.apitest.model.Products;
import com.teggkitchen.apitest.model.ResponseInfo;
import com.teggkitchen.apitest.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateProductsContentFragment extends BaseFragment {

    private static final int PHOTO_PICKED_WITH_CAMERA = 3021;
    private static final int PHOTO_PICKED_WITH_ALBUN = 3022;

    private View view;
    private ImageView imgItem;
    private EditText edtName, edtPrice;
    private Button btnSelectImg;
    private RelativeLayout bottom;

    File filesDir;
    File imageFile;

    Products product;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_products_content, container, false);
        initView();
        initParam();
        initListener();
        return view;
    }

    /* -----------------------------------------------------------
     * Init Process
     * --------------------------------------------------------- */

    @Override
    protected void initView() {

        // 設定標題
        baseActivity.setTitle("修改商品");
        imgItem = (ImageView) view.findViewById(R.id.img_item);
        edtName = (EditText) view.findViewById(R.id.edt_name);
        edtPrice = (EditText) view.findViewById(R.id.edt_price);
        ;
        btnSelectImg = (Button) view.findViewById(R.id.btn_select_img);
        ;
        bottom = (RelativeLayout) view.findViewById(R.id.bottom);
        ;
        imageFile = new File("");
    }

    @Override
    protected void initParam() {
        // 取得Fragment參數
        Bundle bundle = getArguments();
        if (bundle != null) {
            // 取得商品資訊
            product = (Products) bundle.getSerializable("PRODUCT");
            edtName.setText(product.getName());
            edtPrice.setText(product.getPrice() + "");
            if (!Util.isEmpty(product.getImg())) {
                Picasso.get().load(Config.IMAGE_SHOW + product.getImg()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(target);

            }

        }
    }

    @Override
    protected void initListener() {
        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(baseActivity)
                        .setTitle("選擇照片")
                        .setItems(new String[]{"拍照", "相簿"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    onPhotoCameraClick();
                                } else {
                                    onPhotoAlbumClick();
                                }
                            }
                        })
                        .create()
                        .show();
            }
        });

        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("txtSubmit", "onClick");


                // 要上傳的檔案
                final File file = imageFile;
                if (Util.isEmpty(file)) {
                    show_toast("沒有找到圖片");
                    return;
                }

                OkHttpClient client = new OkHttpClient();

                // 設置參數
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("productImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                        .addFormDataPart("productName", edtName.getText().toString())
                        .addFormDataPart("productPrice", edtPrice.getText().toString())
                        .build();

                // 設置連線
                Request request = new Request.Builder()
                        .put(requestBody)
                        .url(Config.PRODUCT_UPDATE+"/"+product.getId())
                        .build();

                // 執行連線與監聽回傳
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        baseActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                show_toast("修改失敗");
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
                                    show_toast("修改成功");
                                }

                                //刪除暫存檔案
                                if (file.exists()) file.delete();
                                clearParams();
                            }
                        });

                    }
                });


            }
        });
    }

    /* -----------------------------------------------------------
     * Custom Process
     * --------------------------------------------------------- */


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case PHOTO_PICKED_WITH_CAMERA: {
                    try {
                        // 取得圖檔URI
                        Uri uri = data.getData();

                        // 判斷是否取得圖檔
                        Bitmap origin;
                        if (uri != null) {
                            InputStream is = getActivity().getContentResolver().openInputStream(uri);
                            origin = BitmapFactory.decodeStream(is);
                        } else {
                            origin = data.getParcelableExtra("data");
                        }

                        // 縮放圖檔
                        final Bitmap photo = Bitmap.createScaledBitmap(origin,
                                imgItem.getWidth(), origin.getHeight() *
                                        imgItem.getWidth() / origin.getWidth(), false);


                        if (!Util.isEmpty(photo)) {
                            // 顯示圖檔
                            imgItem.setImageBitmap(photo);

                            // 轉存為file，路徑：context.getFilesDir()
                            persistImage(baseActivity, photo, "photoName");
                        }

                        // 此處可寫上傳帳戶相片
                    } catch (Exception e) {
                        Log.e("CAMERA-catch", e.getMessage());
                    }
                }

                break;


                case PHOTO_PICKED_WITH_ALBUN: {
                    try {
                        // 取得圖檔URI
                        Uri uri = data.getData();

                        // 判斷是否取得圖檔
                        Bitmap origin;
                        if (uri != null) {
                            InputStream is = getActivity().getContentResolver().openInputStream(uri);
                            origin = BitmapFactory.decodeStream(is);
                        } else {
                            origin = data.getParcelableExtra("data");
                        }

                        // 縮放圖檔
                        final Bitmap photo = Bitmap.createScaledBitmap(origin,
                                imgItem.getWidth(), origin.getHeight() *
                                        imgItem.getWidth() / origin.getWidth(), false);

                        if (!Util.isEmpty(photo)) {
                            // 顯示圖檔
                            imgItem.setImageBitmap(photo);

                            // 轉存為file，路徑：context.getFilesDir()
                            persistImage(baseActivity, photo, "photoName");
                        }

                        // 此處可寫上傳帳戶相片
                    } catch (Exception e) {
                        Log.e("ALBUN-catch", e.getMessage());
                    }
                }
                break;
            }
        }
    }


    /**
     * 相簿Click事件
     */
    private void onPhotoAlbumClick() {
        final Intent intent = getPhotoPickIntent();
        startActivityForResult(intent, PHOTO_PICKED_WITH_ALBUN);
    }

    /**
     * 相機Click事件
     */
    private void onPhotoCameraClick() {
        final Intent intent = getPhotoCameraIntent();
        startActivityForResult(intent, PHOTO_PICKED_WITH_CAMERA);
    }

    /**
     * 相簿，選取產生相片PickIntent
     */
    private Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        return intent;
    }


    /**
     * 相機，開啟相機模式
     */
    private Intent getPhotoCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);

        return intent;
    }


    private void persistImage(Context context, Bitmap bitmap, String name) {
        filesDir = context.getFilesDir();
        imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("persistImage-catch", e.getMessage());
        }
    }

    private void clearParams() {
        edtName.setText("");
        edtPrice.setText("");
        imgItem.setImageBitmap(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //刪除暫存檔案
        if (imageFile.exists()) imageFile.delete();
    }


    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            imgItem.setImageBitmap(bitmap);
            persistImage(baseActivity, bitmap, "photoName");
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


}
