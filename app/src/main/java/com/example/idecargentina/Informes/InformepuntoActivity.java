package com.example.idecargentina.Informes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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
import com.example.idecargentina.Admin.ColportoresadminActivity;
import com.example.idecargentina.Entidades.Punto;
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;
import com.example.idecargentina.User.NuevopuntoActivity;
import com.example.idecargentina.User.OpcionespuntoActivity;

import java.util.HashMap;
import java.util.Map;

public class InformepuntoActivity extends AppCompatActivity {

    Usuario u;
    Punto p;
    Boolean ver,editar;

    Button btnGuardar;

    ProgressBar progressBar;

    TextView campoLatitud, campoLongitud, campoTitulo, campoDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informepunto);

        u = (Usuario)getIntent().getSerializableExtra("usuario");
        p = (Punto)getIntent().getSerializableExtra("punto");

        campoLatitud = (EditText)findViewById(R.id.edLatitud_informepunto);
        campoLongitud = (EditText)findViewById(R.id.edLongitud_informepunto);
        campoTitulo = (EditText)findViewById(R.id.edTitulo_informepunto);
        campoDescripcion = (EditText)findViewById(R.id.edDescripcion_informepunto);

        ver = getIntent().getBooleanExtra("ver",false);
        editar = getIntent().getBooleanExtra("editar",false);

        progressBar = (ProgressBar)findViewById(R.id.prBar_informepunto);
        progressBar.setVisibility(View.INVISIBLE);

        btnGuardar = (Button)findViewById(R.id.btnGuardar_Informepunto);

        campoLatitud.setText(String.valueOf(p.getLatitud()));
        campoLongitud.setText(String.valueOf(p.getLongitud()));
        campoTitulo.setText(p.getTitulo());
        campoDescripcion.setText(p.getDescripcion());

        campoLongitud.setEnabled(false);
        campoLatitud.setEnabled(false);
        if(ver){
            campoTitulo.setEnabled(false);
            campoDescripcion.setEnabled(false);
            btnGuardar.setVisibility(View.INVISIBLE);
        }
    }

    public void onClick(View view){
        AlertDialog.Builder alerta = new AlertDialog.Builder(InformepuntoActivity.this);
        alerta.setMessage(R.string.texto_editarpunto)
                .setTitle(R.string.atencion)
                .setCancelable(false)
                .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        editarPunto_Servicio("http://192.168.42.177/IDEC/editar_punto.php");
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

    private void editarPunto_Servicio(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), R.string.texto_Puntomodificado, Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), OpcionespuntoActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("usuario",u);
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(i);
                        finish();
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), R.string.toast_internet,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("codpunto",String.valueOf(p.getcodpunto()));
                parametros.put("latitud",campoLatitud.getText().toString());
                parametros.put("longitud",campoLatitud.getText().toString());
                parametros.put("titulo",campoTitulo.getText().toString());
                parametros.put("descripcion",campoDescripcion.getText().toString());
                return parametros;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }
}
