package com.example.idecargentina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.idecargentina.Entidades.Usuario;

public class UserActivity extends AppCompatActivity {
    TextView txt_saludo;
    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        txt_saludo=(TextView)findViewById(R.id.lblSaludo);
        u = (Usuario)getIntent().getSerializableExtra("Usuario");
        txt_saludo.setText(getResources().getString(R.string.admin_saludo)+" "+ u.getNombre()+"!");

    }

    public void onClick(View v){
        Intent i =null;
        switch (v.getId()){
            case R.id.btnInfluencer_User:
                i= new Intent(this,InfluencerActivity.class);
                i.putExtra("Usuario",u);
                startActivity(i);
                break;

            case R.id.btnSalir_Admin:
                SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                i = new Intent(this,MainActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
}
