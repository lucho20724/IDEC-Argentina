package com.example.idecargentina.Informes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.idecargentina.Entidades.Candidato;
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;

public class InformecandidatoActivity extends AppCompatActivity {

    TextView txtNombre, txtApellido, txtMail, txtTelefono;
    Usuario u;
    Candidato c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informecandidato);

        txtNombre = (TextView)findViewById(R.id.lblNombre_infocandidato);
        txtApellido= (TextView)findViewById(R.id.lblApellido_infocandidato);
        txtMail = (TextView)findViewById(R.id.lblMail_infocandidato);
        txtTelefono = (TextView)findViewById(R.id.lblTelefono_infocandidato);



        u = (Usuario)getIntent().getSerializableExtra("usuario");
        c = (Candidato)getIntent().getSerializableExtra("candidato");

        txtNombre.setText(c.getNombre());
        txtApellido.setText(c.getApellido());
        txtMail.setText(c.getMail());
        txtTelefono.setText(c.getTelefono());
    }
}
