package com.example.e_commerce.fragment;



import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Adabter.ProductAdabter;
import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.Model.ProductModel;
import com.example.e_commerce.R;
import com.example.e_commerce.Scan_code_activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {


    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private static final int REQUEST_CODE = 1234;
    private final int CAMERA_REQUST =1888;


    public static EditText ed;
    Button speak;
    private ListView listView2;
    private ArrayList<ProductModel> products = new ArrayList<>();
    private ProductAdabter adabter;

    private MyDatabase database;
    private Spinner categories2;
    ArrayAdapter adapter_cate;

    public Search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        speak =(Button)view.findViewById(R.id.button_speak);
        ed = (EditText) view.findViewById(R.id.editTextTextPersonName);


        categories2=view.findViewById(R.id.sppiner_id_search);


        database = new MyDatabase(getActivity());

        listView2 = view.findViewById(R.id.list_pro_2_id);
        getAllcategory();

///////////////////////////////////// VOICE //////////////////////////////////////////////
        speak.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Toast.makeText(getActivity(),"Hi",Toast.LENGTH_LONG).show();

               speakk();
               if (!ed.getText().toString().equals(""))
                   filter(ed.getText().toString());

               else
               {
                   getAllProduct();
                   adabter=new ProductAdabter(getContext(),0,products);
                   listView2.setAdapter(adabter);
               }
           }
       });

//////////////////////////////////BAR CODE/////////////////////////////////////////////////
        Button scan=(Button)view.findViewById(R.id.search_barcode_id);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Scan_code_activity.class));
            }
        });

        getAllProduct();

        if (adabter==null) {
            adabter = new ProductAdabter(getContext(), 0, products);
            listView2.setAdapter(adabter);
        }


        categories2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(categories2.getSelectedItem().toString().equals("All")) {

                    getAllProduct();
                    adabter=new ProductAdabter(getContext(),0,products);
                    listView2.setAdapter(adabter);
                }
                else

                    searchByCategory(categories2.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(""))
                    filter(s.toString());

                else
                {
                    getAllProduct();
                    adabter=new ProductAdabter(getContext(),0,products);
                    listView2.setAdapter(adabter);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().equals(""))
                    filter(s.toString());

                else
                {
                    getAllProduct();
                    adabter=new ProductAdabter(getContext(),0,products);
                    listView2.setAdapter(adabter);
                }


            }
        });
    return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                if(resultCode== getActivity().RESULT_OK && requestCode== voiceCode)
                {
                    ArrayList<String> result= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    ed.setText(result.get(0));
                }
    }
     int voiceCode=1;
    private void speakk()
    {

        //intent to show speech to text
//        final Intent i =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//
//        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//
//        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//
//        startActivityForResult(i,voiceCode);




        Intent intent= new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
        intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault() );
        intent.putExtra( RecognizerIntent.EXTRA_PROMPT, "HI speak something" );

        // Start intent
        try {
            //in there was no errror
            //show dilog
            startActivityForResult( intent, REQUEST_CODE_SPEECH_INPUT);
        }

        catch(Exception e){
            //show messageof error and show
            Toast.makeText(getActivity(),"caaaaaaaaaaaaaatttttttttttttttttt",Toast.LENGTH_LONG).show();
        }

    }
    private void getAllcategory(){

        List<String> cate=new ArrayList<>();
        cate.add("All");
        //Toast.makeText(getActivity(),"caaaaaaaaaaaaaatttttttttttttttttt",Toast.LENGTH_LONG).show();

        Cursor cursor=database.getCategory();
        if (cursor!=null){
            while (!cursor.isAfterLast()){
                cate.add(cursor.getString(1));
                cursor.moveToNext();
            }
            adapter_cate=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,cate);
            categories2.setAdapter(adapter_cate);
            //Toast.makeText(getActivity(),"22222222222222222222222222",Toast.LENGTH_LONG).show();

        }
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
