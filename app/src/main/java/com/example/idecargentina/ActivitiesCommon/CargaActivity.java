package com.example.idecargentina.ActivitiesCommon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.idecargentina.Admin.AdminActivity;
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;
import com.example.idecargentina.User.UserActivity;
import com.google.gson.Gson;

public class CargaActivity extends AppCompatActivity {

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);

        progressBar=(ProgressBar)findViewById(R.id.prBar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                Boolean sesion=preferences.getBoolean("sesion",false);


                if(sesion){
                    Gson gson = new Gson();
                    String json = preferences.getString("usuario", "");
                    Usuario usuario = gson.fromJson(json, Usuario.class);

                    Intent i= null;

                    if(usuario.getCodrol()==1 || usuario.getCodrol()==2){
                        i= new Intent(CargaActivity.this, AdminActivity.class);
                    }else{
                        i= new Intent(getApplicationContext(), UserActivity.class);
                    }
                    i.putExtra("usuario",usuario);
                    startActivity(i);
                    finish();

                }else {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        },3000);


    }
}
