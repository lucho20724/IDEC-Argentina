package com.example.idecargentina;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.idecargentina.Entidades.Usuario;

import java.util.HashMap;
import java.util.Map;

public class NuevocandidatoActivity extends AppCompatActivity {

    EditText campo_nombre, campo_apellido, campo_mail, campo_telefono;
    ProgressBar progressBar;
    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevocandidato);

        campo_nombre = (EditText)findViewById(R.id.edNombre_Aspirante);
        campo_apellido = (EditText)findViewById(R.id.edApellido_Aspirante);
        campo_mail = (EditText)findViewById(R.id.edMail_Aspirante);
        campo_telefono = (EditText)findViewById(R.id.edTelefono_Aspirante);
        progressBar = (ProgressBar)findViewById(R.id.prBar_aspirante);
        u = (Usuario)getIntent().getSerializableExtra("Usuario");


        progressBar.setVisibility(View.INVISIBLE);
    }

    public void onClick(View view){
        Intent i = null;
        progressBar.setVisibility(View.VISIBLE);
        crearAspirante_Servicio("http://192.168.42.177/IDEC/insertar_aspirante.php");
    }

    private void crearAspirante_Servicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),R.string.nuevoaspirante_toast_aspirantenuevo, Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder alerta = new AlertDialog.Builder(NuevocandidatoActivity.this);
                        alerta.setMessage(R.string.nuevoaspirante_alert_contenido)
                                .setTitle(R.string.nuevoaspirante_alert_titulo)
                                .setCancelable(false)
                                .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        campo_nombre.setText("");
                                        campo_apellido.setText("");
                                        campo_mail.setText("");
                                        campo_telefono.setText("");
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getApplicationContext(),InfluencerActivity.class);
                                        i.putExtra("Usuario",u);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                        alerta.show();

                    }
                }, 3000);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage().toString(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("nombre",campo_nombre.getText().toString());
                parametros.put("apellido",campo_apellido.getText().toString());
                parametros.put("email",campo_mail.getText().toString());
                parametros.put("telefono",campo_telefono.getText().toString());
                parametros.put("codusuario",String.valueOf(u.getCodusuario()));

                return parametros;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(this);
        try{
            rq.add(stringRequest);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }

    }
}
