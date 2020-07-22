package com.example.idecargentina.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class NuevopuntoActivity extends AppCompatActivity {
    TextView txt_latitud, txt_longitud;
    ArrayList<String> coordenadas;
    ProgressBar progressBar;
    Usuario u;
    EditText campo_titulo, campo_descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevopunto);

        coordenadas = (ArrayList<String>) getIntent().getStringArrayListExtra("coordenadas");
        progressBar = (ProgressBar)findViewById(R.id.prBar_punto);
        progressBar.setVisibility(INVISIBLE);


        campo_titulo = (EditText)findViewById(R.id.edTitulo_punto);
        campo_descripcion = (EditText)findViewById(R.id.edDescripcion_punto);

        u = (Usuario)getIntent().getSerializableExtra("usuario");

        txt_latitud = (TextView)findViewById(R.id.lblLatitud);
        txt_longitud = (TextView)findViewById(R.id.lblLongitud);

        txt_latitud.setText("Latitud: "+coordenadas.get(0));
        txt_longitud.setText("Longitud: "+coordenadas.get(1));
    }

    public void onClick(View view){
        AlertDialog.Builder alerta = new AlertDialog.Builder(NuevopuntoActivity.this);
        alerta.setMessage(R.string.texto_crearpunto)
                .setTitle(R.string.atencion)
                .setCancelable(false)
                .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(VISIBLE);
                        crearPunto_Servicio("http://www.boxwakanda.site/servicios/insertar_punto.php");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alerta.show();
    }

    private void crearPunto_Servicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(INVISIBLE);
                Toast.makeText(NuevopuntoActivity.this, R.string.toast_nuevopunto, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(),MapsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("usuario",u);
                startActivity(i);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(INVISIBLE);
                Toast.makeText(getApplicationContext(),R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("latitud",coordenadas.get(0));
                parametros.put("longitud",coordenadas.get(1));
                parametros.put("titulo",campo_titulo.getText().toString());
                parametros.put("descripcion",campo_descripcion.getText().toString());
                parametros.put("codusuario",String.valueOf(u.getCodusuario()));
                return parametros;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }
}
