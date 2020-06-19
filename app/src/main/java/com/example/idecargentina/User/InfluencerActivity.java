package com.example.idecargentina.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;

public class InfluencerActivity extends AppCompatActivity {
    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influencer);

        u = (Usuario)getIntent().getSerializableExtra("usuario");
    }

    public void onClick(View view){
        Intent i = null;
        switch (view.getId()){
            case R.id.btnNuevo_influencer:
                i= new Intent(this, NuevocandidatoActivity.class);
                i.putExtra("usuario",u);
                break;

            case R.id.btnVer_influencer:
                i= new Intent(this, ListacandidatosActivity.class);
                i.putExtra("usuario",u);
                break;

            case R.id.btnEditar_influencer:
                i= new Intent(this, ListacandidatosActivity.class);
                i.putExtra("usuario",u);
                i.putExtra("editar",true);
                break;

            case R.id.btnEliminar_influencer:
                i= new Intent(this, ListacandidatosActivity.class);
                i.putExtra("usuario",u);
                i.putExtra("eliminar",true);
                break;
        }

        startActivity(i);
    }
}
