package com.example.e_commerce.Adabter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Model.ProductModel;
import com.example.e_commerce.R;
import com.example.e_commerce.fragment.Cart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartAdapter extends BaseAdapter {
    private ArrayList<ProductModel> data;
    private Context context;

    private double total_cost = Double.parseDouble(Cart.orignal_price.getText().toString());
    private ArrayList<Integer> list_id = new ArrayList<>();


    SharedPreferences sharedPreferences;

    public CartAdapter(Context context, ArrayList<ProductModel> data) {
        this.data = data;
        this.context = context;

        getProductsids();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.cart_item, parent, false);
        }

        // get current item to be displayed
        final ProductModel currentItem = (ProductModel) getItem(position);

        // get the TextView for item name and item description
        TextView product_name = convertView.findViewById(R.id.name_of_cart_product);
        TextView product_price = convertView.findViewById(R.id.price_of__cart_product);
        Button remove = convertView.findViewById(R.id.remove_from_cart);
        Button add_quan = convertView.findViewById(R.id.increase_quantity);
        Button dec_quan = convertView.findViewById(R.id.decrase_quantity);
        final TextView item_quan = convertView.findViewById(R.id.quantity_at_cart);

        //TextView finat_cost=(TextView) convertView.findViewById(R.id.total_cost);


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();

               list_id.remove(new Double(currentItem.getPro_id()));

                SharedPreferences.Editor editor;
                Gson gson = new Gson();
                String json = gson.toJson(list_id);
                editor = sharedPreferences.edit();
                editor.putString("lastorder", json);
                list_id.clear();

                editor.apply();

                //product_name.setText("total cost :"+total_cost+" p*q : "+currentItem.getPrice()+"* "+currentItem.getPro_quantity());
                total_cost = Double.parseDouble(Cart.orignal_price.getText().toString());
                total_cost=total_cost - (currentItem.getPrice()*currentItem.getPro_quantity());

                //currentItem.setPrice(total_cost);

                product_price.setText(String.valueOf(total_cost));


                Cart.orignal_price.setText(String.valueOf(total_cost));
                Cart.total_cost.setText(String.valueOf(total_cost+20));

            }
        });


        add_quan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int old_quan = Integer.parseInt(item_quan.getText().toString());
                double res_before = old_quan * currentItem.getPrice();

                total_cost = total_cost - res_before;

                int new_qun=old_quan+1;

                double res_after = new_qun * currentItem.getPrice();

                total_cost += res_after;

                //currentItem.setPrice(res_after);
                currentItem.setPro_quantity(new_qun);
                item_quan.setText(String.valueOf(new_qun));

                product_price.setText(String.valueOf(currentItem.getPrice()));

                Cart.orignal_price.setText(String.valueOf(total_cost));
                Cart.total_cost.setText(String.valueOf(total_cost+20));



            }
        });

        dec_quan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quan = Integer.parseInt(item_quan.getText().toString());
                double res_before = quan * currentItem.getPrice();
                total_cost = total_cost - res_before;

                if (quan > 1)
                    quan--;


                item_quan.setText(String.valueOf(quan));

                double res_after = quan * currentItem.getPrice();

                total_cost += res_after;


                //currentItem.setPrice(res_after);
                currentItem.setPro_quantity(quan);

                item_quan.setText(String.valueOf(quan));

                product_price.setText(String.valueOf(currentItem.getPrice()));

                Cart.orignal_price.setText(String.valueOf(total_cost));

                Cart.total_cost.setText(String.valueOf(total_cost+20));



            }
        });



        product_name.setText(currentItem.getProName());
        product_price.setText(currentItem.getPrice() + " LE");

        return convertView;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public double getTotal_cost() {
        return total_cost;
    }


    private void getProductsids() {
        sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        String ids = sharedPreferences.getString("lastorder", null);
        if (ids != null) {
            Gson gson = new Gson();
            list_id=gson.fromJson(ids,ArrayList.class);

        }


    }
}