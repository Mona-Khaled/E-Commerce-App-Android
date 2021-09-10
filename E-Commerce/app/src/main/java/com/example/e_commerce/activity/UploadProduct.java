package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.Model.CategoryModel;
import com.example.e_commerce.Model.ProductModel;
import com.example.e_commerce.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.WriterException;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class UploadProduct extends AppCompatActivity {

    //ImageView productimage;
    EditText productname, productprice, productquantity;
    Spinner proCategory;
    ArrayAdapter adapter;
    Button upload_btn;
    TextView reset_btn;
    MyDatabase database;
    Button gen_qr;
    private ImageView qrCodeIV;
    //product_image

    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    final static int GALLERY_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        intiView();


        addCategory();
        getAllcategory();

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadProduct();

            }
        });



    }

    protected void addCategory(){
        database.insertCategory(new CategoryModel("Women_fashion"));
        database.insertCategory(new CategoryModel("Men_fashion"));
        database.insertCategory(new CategoryModel("Kids_fashion"));
        database.insertCategory(new CategoryModel("cars"));
        database.insertCategory(new CategoryModel("Books"));
        database.insertCategory(new CategoryModel("sport"));
        database.insertCategory(new CategoryModel("MakeUp"));


    }

    protected void intiView() {

        productname = findViewById(R.id.product_name);
        productprice = findViewById(R.id.product_price);
        productquantity = findViewById(R.id.product_quantity);
        proCategory = findViewById(R.id.category);
        upload_btn = findViewById(R.id.btn_upload);
        reset_btn = findViewById(R.id.reset);
        database = new MyDatabase(this);


    }

    protected void chooseImage() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    //adds all categories from db in spinner
    protected void getAllcategory(){

        List<String>cate=new ArrayList<>();
        Cursor cursor=database.getCategory();
        if (cursor!=null){
            while (!cursor.isAfterLast()){
                cate.add(cursor.getString(1));
                cursor.moveToNext();
            }
            adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,cate);
            proCategory.setAdapter(adapter);
        }
    }

    protected void uploadProduct(){

        String name=productname.getText().toString().trim();
        String price=productprice.getText().toString().trim();
        String quan=productquantity.getText().toString().trim();
        int catid=Integer.parseInt(database.getCatId(proCategory.getSelectedItem().toString()));

        if(!name.equals("")||!price.equals("")||!quan.equals(""))
        {
            ProductModel productModel = new ProductModel(Integer.parseInt(quan), catid,name,Double.parseDouble(price));
            database.insertProduct(productModel);


            productname.setText("");
            productprice.setText("");
            productquantity.setText("");


            Toast.makeText(this, "product added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Check data again", Toast.LENGTH_SHORT).show();
        }
    }

}
