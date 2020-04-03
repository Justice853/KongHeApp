package com.example.kongheapp.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongheapp.Util.QRcodeUtil;
import com.example.kongheapp.R;
import com.example.kongheapp.Util.ImgUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class QrcodeActivity extends BaseActivity  {
    TextView title;
    ImageView fanhui;
    EditText et_ewm;
    Button bt_ewm;
    Button bt_img;
    ImageView code_image;
    private Uri imageUri;
    public static final int TAKE_PHOTO =1;
    public static final int CHOOSE_PHOTO=2;
    Bitmap bitmap;
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
        setContentView(R.layout.erwema_layout);
        title=findViewById(R.id.rt_title);
        fanhui=findViewById(R.id.returnimage);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText("二维码制作");
        et_ewm=findViewById(R.id.input_ewm);
        bt_ewm=findViewById(R.id.bt_ewm);
        bt_img=findViewById(R.id.bt_image);
        code_image=findViewById(R.id.code_image);
        ActivityCompat.requestPermissions(QrcodeActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        bt_ewm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(et_ewm);
                if(TextUtils.isEmpty(et_ewm.getText().toString())){
                    Toast.makeText(QrcodeActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    bitmap = QRcodeUtil.createQRcodeBitmap(et_ewm.getText().toString(),480,480);
                    code_image.setImageBitmap(bitmap);
                }

            }
        });
        bt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(et_ewm);
                if(TextUtils.isEmpty(et_ewm.getText().toString())){
                    Toast.makeText(QrcodeActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }else {

                    showBottomdialog();
                }
            }
        });
        code_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                registerForContextMenu(v);
                openContextMenu(v);
                return true;
            }
        });
    }
    private void hideKeyboard(View view) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im.isActive(view)) {
            im.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }
    private void showBottomdialog(){
        final Dialog dialog = new Dialog(this,R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(QrcodeActivity.this,R.layout.pupue_dialog,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.findViewById(R.id.bt_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputimage = new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputimage.exists()){
                        outputimage.delete();
                    }
                    outputimage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(Build.VERSION.SDK_INT>=24){
                    imageUri = FileProvider.getUriForFile(QrcodeActivity.this,"com.example.cameraalbumtest.fileprovider",outputimage);
                }
                else {
                    imageUri =Uri.fromFile(outputimage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.bt_bdimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(QrcodeActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(QrcodeActivity.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },1);
                }else {
                    openAlbum();

                }
                dialog.dismiss();
            }

        });
        dialog.findViewById(R.id.bt_mcancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void openAlbum(){
        Intent intent =new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    try {
                        Bitmap logo = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        bitmap = QRcodeUtil.createQRCodeBitmap(et_ewm.getText().toString(),480,480,"UTF-8"
                                ,"H","2", Color.BLACK,Color.WHITE,logo);
                        code_image.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
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

    private void displayImage(String imagePath) {
        if(imagePath != null){
            Bitmap logo = BitmapFactory.decodeFile(imagePath);
            bitmap = QRcodeUtil.createQRCodeBitmap(et_ewm.getText().toString(),480,480,"UTF-8","H","2", Color.BLACK,Color.WHITE,logo);
            code_image.setImageBitmap(bitmap);

        }
        else {
            Toast.makeText(this,"没有得到这个图片",Toast.LENGTH_SHORT).show();
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath =getImagePath(uri,null);
        displayImage(imagePath);//生成二维码。
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

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,1,0,"保存");

    }
    public boolean onContextItemSelected(MenuItem item) {

        // 得到当前被选中的item信息
        int itemId=item.getItemId();
        switch (itemId){
            case 1:
                ImgUtil.saveImageToGallery(this, bitmap);
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
        }
        return true;
    }




}
