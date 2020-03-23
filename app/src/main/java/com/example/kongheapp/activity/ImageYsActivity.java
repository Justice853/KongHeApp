package com.example.kongheapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongheapp.QRcode.QRcodeUtil;
import com.example.kongheapp.R;
import com.example.kongheapp.Util.ImgUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageYsActivity extends BaseActivity {
    TextView title;
    ImageView fanhui;
    Button bt_ys;
    Button bt_xzimg;
    public static final int CHOOSE_PHOTO=2;
    Bitmap bitmap = null;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int zt =intent.getIntExtra("zt",0);
        if(zt==1){
            //设置主题
            setTheme(R.style.AppDarkTheme);
        }else{
            setTheme(R.style.AppLightTheme);
        }
        setContentView(R.layout.img_layout);
        title=findViewById(R.id.rt_title);
        title.setText("图片压缩");
        fanhui=findViewById(R.id.returnimage);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_ys=findViewById(R.id.bt_ysimage);
        bt_xzimg=findViewById(R.id.bt_xzimage);
        imageView=findViewById(R.id.images);
        bt_ys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap==null){
                    Toast.makeText(ImageYsActivity.this,"没有可压缩的照片",Toast.LENGTH_SHORT).show();
                }
                else {
                    ImgUtil.saveImageToGallery(ImageYsActivity.this,compressImage(bitmap));
                    Toast.makeText(ImageYsActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    bitmap=null;
                }

            }
        });
        bt_xzimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ImageYsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ImageYsActivity.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },1);
                }else {
                    openAlbum();

                }
            }
        });
    }
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

        return bitmap;
    }

    private void openAlbum(){
        Intent intent =new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CHOOSE_PHOTO:
                if(resultCode ==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }
                    else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                 break;
        }
    }

    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+ "=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }
                else {
                    Toast.makeText(this,"你拒绝了许可",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }
    private void displayImage(String imagePath) {
        if(imagePath != null){
            bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        }
        else {
            Toast.makeText(this,"没有得到这个图片",Toast.LENGTH_SHORT).show();
        }
    }
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath =getImagePath(uri,null);
        displayImage(imagePath);
    }
}
