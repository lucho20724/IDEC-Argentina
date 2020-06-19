package com.example.idecargentina.Informes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;

public class InformecolportorActivity extends AppCompatActivity {

    TextView txtNombre, txtApellido, txtMail, txtTelefono, txtNro;

    Usuario colportor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informecolportor);

        txtNombre=(TextView)findViewById(R.id.lblNombre_infocolportoradmin);
        txtApellido=(TextView)findViewById(R.id.lblApellido_infocolportoradmin);
        txtMail=(TextView)findViewById(R.id.lblMail_infocolportoradmin);
        txtTelefono=(TextView)findViewById(R.id.lblTelefono_infocolportoradmin);
        txtNro=(TextView)findViewById(R.id.lblNroalumno_infocolportoradmin);

        colportor = (Usuario)getIntent().getSerializableExtra("colportor");

        txtNombre.setText(colportor.getNombre());
        txtApellido.setText(colportor.getApellido());
        txtMail.setText(colportor.getMail());
        txtTelefono.setText(colportor.getTelefono());
        txtNro.setText(String.valueOf(colportor.getNroalumno()));

    }
}
