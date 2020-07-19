package com.example.idecargentina.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasaporteuserActivity extends AppCompatActivity {

    ImageView imagen;
    Usuario u;
    ProgressBar progressBar;
    TextView txtUsuario, txtPasaporte;
    boolean pasaporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasaporteuser);

        u=(Usuario)getIntent().getSerializableExtra("usuario");
        imagen= (ImageView)findViewById(R.id.imagen_Pasaporte);
        progressBar = (ProgressBar)findViewById(R.id.prBar_Pasaporte);
        txtUsuario = (TextView)findViewById(R.id.lblUsuario_pasaporte);
        txtPasaporte = (TextView)findViewById(R.id.lblPasaporte_pasaporte);

        progressBar.setVisibility(View.VISIBLE);

        boolean passport=u.getPasaporte();

        if(u.getPasaporte()) {
            txtPasaporte.setText("PASAPORTE HABILITADO");
            txtPasaporte.setTextColor(Color.GREEN);
        }else {
            txtPasaporte.setText("PASAPORTE DENEGADO");
            txtPasaporte.setTextColor(Color.RED);
        }

        txtUsuario.setText(u.getNombre()+" "+u.getApellido()+" ("+String.valueOf(u.getNroalumno())+")");

        progressBar.setVisibility(View.INVISIBLE);
    }



}
