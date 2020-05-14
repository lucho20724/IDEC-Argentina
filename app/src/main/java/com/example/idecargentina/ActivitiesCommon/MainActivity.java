package com.example.idecargentina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        Intent i= null;
        switch (view.getId()){
            case R.id.btnAcceder_main:
                i= new Intent(this,LoginActivity.class);
                break;
            case R.id.btnRegistro_main:
                i= new Intent(this,RegistroActivity.class);
                break;
            case R.id.btnInfo_main:
                i= new Intent(this,InfoActivity.class);
                break;
        }
        try{
            startActivity(i);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
