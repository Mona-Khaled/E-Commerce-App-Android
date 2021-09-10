package com.example.e_commerce.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.telephony.gsm.GsmCellLocation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Adabter.CartAdapter;
import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.Model.Order;
import com.example.e_commerce.Model.OrderDetails;
import com.example.e_commerce.Model.ProductModel;
import com.example.e_commerce.R;
import com.example.e_commerce.activity.Login;
import com.example.e_commerce.activity.MainActivity;
import com.example.e_commerce.activity.SignUp;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cart extends Fragment {

    private ListView cart_products;
    private CartAdapter adapter;
    private ArrayList<ProductModel> data = new ArrayList<>();
    Order r;

    private MyDatabase database;
    private SharedPreferences sharedPreferences;


    TextView delivery_cost;
      public static TextView total_cost,orignal_price;

    double cost=0;
    Button confirm;

    int PERMISSION_ID = 44;

    public Cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        confirm=(Button)view.findViewById(R.id.confirm_order);



        cart_products = view.findViewById(R.id.cart_product);
        database = new MyDatabase(getContext());
        orignal_price=view.findViewById(R.id.order_price);
        delivery_cost=view.findViewById(R.id.delivery_cost);
        total_cost=view.findViewById(R.id.total_cost);
        String iii=getProductsids();
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                r=new Order(Login.CUST_ID, "", c);
                database.insertOrder(r);
                Toast.makeText(getActivity(), "Successfully operation thank you :) ", Toast.LENGTH_SHORT).show();
                if (iii != null) {
                    Gson gson = new Gson();
                    ArrayList id = gson.fromJson(iii, ArrayList.class);
                    new_ord_det(id);
                }
                data.clear();
                adapter.notifyDataSetChanged();

                data.clear();
                adapter.notifyDataSetInvalidated();
                orignal_price.setText("0 LE");
                delivery_cost.setText("0 LE");
                total_cost.setText("0 LE");

//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
                data.clear();
                adapter.notifyDataSetChanged();

                data.clear();
                adapter.notifyDataSetInvalidated();
                orignal_price.setText("0 LE");
                delivery_cost.setText("0 LE");
                total_cost.setText("0 LE");

            }

        });



        return view;
    }

    private String getProductsids() {
        sharedPreferences = this.getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        String ids = sharedPreferences.getString("lastorder", null);
        if (ids != null) {
            Gson gson = new Gson();
            ArrayList id = gson.fromJson(ids, ArrayList.class);
            getCartProduct(id);


            adapter = new CartAdapter(getContext(), data);
            adapter.setTotal_cost(cost);
            adapter.notifyDataSetChanged();

            cart_products.setAdapter(adapter);



            orignal_price.setText(String.valueOf(adapter.getTotal_cost()) + " LE");
            delivery_cost.setText("20.0 LE");
            total_cost.setText(cost + 20.0 + "LE");
        }
        return ids;
    }

    private void new_ord_det(ArrayList<Integer> ids)
    {

        String last_o_id=database.get_last_order_id();
        data.clear();

        for (int i = 0; i < ids.size(); i++) {
            Cursor cursor = database.getProductbyId(String.valueOf(ids.get(i)));
            //OrderDetails od=new OrderDetails(Integer.parseInt(last_o_id),ids.get(i),1);
            if (cursor != null) {

                ProductModel productModel = new ProductModel(Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        cursor.getString(1),
                        Double.parseDouble(cursor.getString(2)));
                productModel.setPro_id(Integer.parseInt(cursor.getString(0)));


                //Toast.makeText(getActivity(), ""+productModel.getPro_id()+" --- " +Integer.parseInt(last_o_id), Toast.LENGTH_SHORT).show();
                OrderDetails od=new OrderDetails(Integer.parseInt(last_o_id),productModel.getPro_id(),1);
                //Toast.makeText(getActivity(), ""+od.getOrderId()+" -///-- " +od.getProId(), Toast.LENGTH_SHORT).show();
                database.insertOrder_det(od);

            }
        }

    }

    private void getCartProduct(ArrayList<Integer> ids) {
        

        data.clear();
        cost=0;

        //Toast.makeText(getActivity(),"CCC-->"+p,Toast.LENGTH_LONG).show();
        //OrderDetails od=new OrderDetails(orderId, int proId, int quantity);
        for (int i = 0; i < ids.size(); i++) {
            Cursor cursor = database.getProductbyId(String.valueOf(ids.get(i)));
            //OrderDetails od=new OrderDetails(Integer.parseInt(last_o_id),ids.get(i),1);
            if (cursor != null) {
                ProductModel productModel = new ProductModel(Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        cursor.getString(1),
                        Double.parseDouble(cursor.getString(2)));
                productModel.setPro_id(Integer.parseInt(cursor.getString(0)));
                data.add(productModel);
                cost+=Double.parseDouble(cursor.getString(2));
            }

        }
    }
}
