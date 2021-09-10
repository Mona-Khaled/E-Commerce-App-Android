package com.example.e_commerce.fragment;


import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.e_commerce.Adabter.ProductAdabter;
import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.Model.ProductModel;
import com.example.e_commerce.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    private ListView listView;
    private ArrayList<ProductModel> products = new ArrayList<>();
    private ProductAdabter adabter;
    private MyDatabase database;

    private EditText search_keyword;

    private Spinner categories;
    ArrayAdapter adapter_cate;


    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = view.findViewById(R.id.list_products);
        search_keyword=view.findViewById(R.id.search_keyword);
        categories=view.findViewById(R.id.cate_search);


        database = new MyDatabase(getActivity());

        getAllcategory();
        getAllProduct();

        if (adabter==null) {
            adabter = new ProductAdabter(getContext(), 0, products);
            listView.setAdapter(adabter);
        }


        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(categories.getSelectedItem().toString().equals("All")) {

                    getAllProduct();
                    adabter=new ProductAdabter(getContext(),0,products);
                    listView.setAdapter(adabter);
                }
                else

                searchByCategory(categories.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().equals(""))
                filter(s.toString());

                else
                {
                    getAllProduct();
                    adabter=new ProductAdabter(getContext(),0,products);
                    listView.setAdapter(adabter);
                }


            }
        });




        return view;
    }

    private void getAllProduct() {
        Cursor cursor = database.getProducts();

        products.clear();
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                ProductModel productModel=new ProductModel(Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        cursor.getString(1),
                        Double.parseDouble(cursor.getString(2)));


                productModel.setPro_id(Integer.parseInt(cursor.getString(0)));
                products.add(productModel);
                cursor.moveToNext();
            }
        }
    }
    private void getAllcategory(){

        List<String> cate=new ArrayList<>();
        cate.add("All");
        Cursor cursor=database.getCategory();
        if (cursor!=null){
            while (!cursor.isAfterLast()){
                cate.add(cursor.getString(1));
                cursor.moveToNext();
            }
            adapter_cate=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,cate);
            categories.setAdapter(adapter_cate);
        }
    }

    private void searchByCategory(String name) {

        ArrayList<ProductModel>filterlist=new ArrayList<>();


            String cat_id = database.getCatId(name);
            Cursor cursor = database.getProductbyCategor(cat_id);
            if (cursor != null) {
                while (!cursor.isAfterLast()) {

                    ProductModel productModel=new ProductModel(Integer.parseInt(cursor.getString(3)),
                            Integer.parseInt(cursor.getString(4)),
                            cursor.getString(1),
                            Double.parseDouble(cursor.getString(2)));
                    productModel.setPro_id(Integer.parseInt(cursor.getString(0)));
                    filterlist.add(productModel);

                    cursor.moveToNext();
                }


                   adabter.filter(filterlist);

                if (filterlist.size()==0)
                    Toast.makeText(getActivity(), "No products to show", Toast.LENGTH_SHORT).show();
            }
    }

    private void filter(String text) {
        ArrayList<ProductModel> filteredList = new ArrayList<>();

        for (ProductModel item : products) {
            if (item.getProName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adabter.filter(filteredList);
    }


}
