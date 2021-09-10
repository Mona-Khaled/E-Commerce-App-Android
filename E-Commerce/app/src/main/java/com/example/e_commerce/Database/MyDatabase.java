package com.example.e_commerce.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.e_commerce.Model.CategoryModel;
import com.example.e_commerce.Model.CustomerModel;
import com.example.e_commerce.Model.Order;
import com.example.e_commerce.Model.OrderDetails;
import com.example.e_commerce.Model.ProductModel;

public class MyDatabase extends SQLiteOpenHelper {

    final static String dataName = "Mydatabase";
    SQLiteDatabase database;

    public MyDatabase(@Nullable Context context) {
        super(context, dataName, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table  customer (id integer primary key  autoincrement , name text not null, email text not null," +
                "password text not null, gender text not null, birthdate text , jop text )");

        db.execSQL("create table category (id integer primary key  autoincrement , name text not null )");

        db.execSQL("create table product (id integer primary key autoincrement, name text not null  ," +
                "price real not null , quantity integer not null , cate_id integer not null ," +
                "foreign key (cate_id)references category (id))");

        db.execSQL("create table orders (order_id integer primary key autoincrement, o_date text not null  ," +
                " cust_id integer not null ," +
                "foreign key (cust_id)references customer (id))");

        db.execSQL("create table order_det (order_id integer   , pro_id integer  , quantity integer," +
                "PRIMARY KEY (order_id, pro_id) ,"+
                "foreign key (pro_id)references product (id) ,"+
                "foreign key (order_id)references orders (id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists customer");
        db.execSQL("drop table if exists category");
        db.execSQL("drop table if exists product");
        db.execSQL("drop table if exists orders");
        db.execSQL("drop table if exists order_det");

        onCreate(db);

    }
    public void insertOrder_det(OrderDetails  od) {
        database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_id", od.getOrderId());
        values.put("pro_id", od.getProId());
        values.put("quantity", od.getQuantity());

        database.insert("order_det", null, values);
        database.close();

    }
    public void insertOrder(Order order) {
        database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("o_date", order.getOrderDate().toString());
        values.put("cust_id",order.getCustId() );

        database.insert("orders", null, values);
        database.close();

    }

    //add new customer
    public void insertCustomer(CustomerModel cust) {
        database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", cust.getCustName());
        values.put("email", cust.getMail());
        values.put("password", cust.getPassword());
        values.put("birthdate", cust.getCustBirthDate());
        values.put("gender", cust.getGender());
        values.put("jop", cust.getCustJop());

        database.insert("customer", null, values);
        //database.close();

    }

    public Cursor userLogin(String username, String pass) {
        database = getReadableDatabase();
        String[] args = {username, pass};
        //database.query("customer","id",raw,null,null,null,null,null);
        Cursor cursor = database.rawQuery("select id from customer where name =? and  password =? ", args);

        if (cursor != null)
            cursor.moveToFirst();

        database.close();
        return cursor;

    }


    //forget password
    public String getPassword(String mail) {
        database = getReadableDatabase();
        String[] args = {mail};
        Cursor cursor = database.rawQuery("select password from customer where email =?", args);
        if (cursor.getCount()>0) {

            cursor.moveToFirst();
            database.close();
            return cursor.getString(0);
        }
        database.close();

        cursor.close();
        return null;
    }

    public void insertProduct(ProductModel product){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",product.getProName());
        values.put("price",product.getPrice());
        values.put("quantity",product.getPro_quantity());
        values.put("cate_id",product.getCatId());

        database.insert("product",null,values);
        database.close();
    }

    //get all products from database "to display it in home and customer be able to buy anything from it"
    public Cursor getProducts(){
        database=getReadableDatabase();
        String[]fields={"id","name","price","quantity","cate_id"};
       Cursor cursor= database.query("product",fields,null,null,null,null,null);

       if (cursor!=null)
           cursor.moveToFirst();

      // database.close();

       return cursor;


    }

    public void insertCategory(CategoryModel cate){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",cate.getCat_name());
        database.insert("category",null,values);
        database.close();
    }

    //get all categories from database
    public Cursor getCategory(){
        database=getReadableDatabase();
        String []fields={"id","name"};
       Cursor cursor= database.query("category",fields,null,null,null,null,null);

       if (cursor.getCount()>0)
            cursor.moveToFirst();

       database.close();

       return cursor;
    }
    public Cursor getProductbyCategor(String cate){
        database=getReadableDatabase();
        String []args={cate};
      Cursor cursor=  database.rawQuery("select * from product where cate_id =? ",args);
        if (cursor!=null)
            cursor.moveToFirst();

        return cursor;

    }
    //get product by "product id"
    public Cursor getProductbyId(String id){
        database=getReadableDatabase();
        String []args={id};
        Cursor cursor=  database.rawQuery("select * from product where id =? ",args);
        if (cursor!=null)
            cursor.moveToFirst();

        return cursor;

    }
    //return 'category id' using its name
    public String getCatId(String name ){
        database=getReadableDatabase();
        String[]args={name};
        Cursor cursor=database.rawQuery("select id from category where name =?",args);

        if (cursor.getCount()>0) {

            cursor.moveToFirst();
            database.close();
            return cursor.getString(0);
        }
        database.close();

        cursor.close();
        return null;

    }

    public String get_last_order_id(){
        database=getReadableDatabase();
        String v;
        String[]args={};
//        SELECT * FROM    TABLE WHERE   ID = (SELECT MAX(ID)  FROM TABLE);
        Cursor cursor=database.rawQuery("select * from orders where order_id = (select MAX(order_id) from orders);",args);

        if (cursor.getCount()>0) {

            cursor.moveToFirst();


            v= cursor.getString(0);
        }
        else
            v= String.valueOf(0);

        cursor.close();
        database.close();
        return v;

    }
}
