package com.example.idecargentina.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.idecargentina.R;

public class ColportoresadminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colportoresadmin);

    }

    public void onClick(View view ){
        Intent i=null;

        switch (view.getId()){
            case R.id.btnVer_Admincolportor:
                i=new Intent(this,ListacolportoresadminActivity.class);
                break;
            case R.id.btnAdmin_Admincolportor:
                i=new Intent(this,ListacolportoresadminActivity.class);
                i.putExtra("cambiarrol",true);
                break;
            case R.id.btnBorrar_Admincolportor:
                i=new Intent(this,ListacolportoresadminActivity.class);
                i.putExtra("eliminar",true);
                break;
        }
        startActivity(i);
    }
}
