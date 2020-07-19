package com.example.idecargentina.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;

public class PasaporteadminActivity extends AppCompatActivity {
    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasaporteadmin);

        u = (Usuario)getIntent().getSerializableExtra("usuario");
    }

    public void onClick(View view){
        Intent i=null;
        switch (view.getId()){
            case R.id.btnHabilitar_AdminPasaporte:
                i = new Intent(getApplicationContext(),ListapasaporteadminActivity.class);
                i.putExtra("habilitar", true);
                i.putExtra("usuario",u);
                startActivity(i);
                break;

            case R.id.btnDeshabilitar_AdminPasaporte:
                i = new Intent(getApplicationContext(),ListapasaporteadminActivity.class);
                i.putExtra("habilitar", false);
                i.putExtra("usuario",u);
                startActivity(i);
                break;
        }

    }
}
