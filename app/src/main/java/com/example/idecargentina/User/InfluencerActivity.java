package com.example.idecargentina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.idecargentina.Entidades.Usuario;

public class InfluencerActivity extends AppCompatActivity {
    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influencer);

        u = (Usuario)getIntent().getSerializableExtra("Usuario");
    }

    public void onClick(View view){
        Intent i = null;
        switch (view.getId()){
            case R.id.btnNuevo_influencer:
                i= new Intent(this, NuevocandidatoActivity.class);
                i.putExtra("Usuario",u);
                break;
        }

        startActivity(i);
    }
}
