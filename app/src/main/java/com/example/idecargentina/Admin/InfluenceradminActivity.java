package com.example.idecargentina.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;

public class InfluenceradminActivity extends AppCompatActivity {
    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influenceradmin);

        u = (Usuario)getIntent().getSerializableExtra("usuario");
    }

    public void onClick(View view){
        Intent i = null;

        switch (view.getId()){
            case R.id.btnVer_influenceradmin:
                i = new Intent(this, ListacandidatosadminActivity.class);
                i.putExtra("usuario",u);
                break;
        }

        startActivity(i);
    }
}
