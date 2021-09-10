package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.e_commerce.activity.MainActivity;
import com.example.e_commerce.fragment.Search;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
//ZXingScannerView.ResultHandle -->library for scanning barcode


public class Scan_code_activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView Scannerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Scannerview =new ZXingScannerView(this);

        setContentView(Scannerview);
        Toast.makeText(this,"scan",Toast.LENGTH_LONG).show();

    }

    @Override
    public void handleResult(Result result) {
        Search.ed.setText(result.getText());
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Scannerview.stopCamera();

    }

    @Override
    protected void onResume() {
        super.onResume();

        Scannerview.setResultHandler(this);
        Scannerview.startCamera();
    }

}