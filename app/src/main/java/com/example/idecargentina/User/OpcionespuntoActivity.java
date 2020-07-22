package com.example.idecargentina.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;

public class OpcionespuntoActivity extends AppCompatActivity {

    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcionespunto);

        u = (Usuario)getIntent().getSerializableExtra("usuario");
    }

    public void onClick(View view){
        Intent i = new Intent(getApplicationContext(),ListapuntosActivity.class);
        switch (view.getId()){
            case R.id.btnVer_Opciones:
                i.putExtra("ver",true);
                break;
            case R.id.btnEditar_Opciones:
                i.putExtra("editar",true);
                break;
            case R.id.btnEliminar_Opciones:
                i.putExtra("eliminar",true);
                break;
        }

        i.putExtra("usuario",u);
        startActivity(i);
    }
}
