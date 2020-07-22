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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.idecargentina.Entidades.Candidato;
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NuevocandidatoActivity extends AppCompatActivity {

    EditText campo_nombre, campo_apellido, campo_mail, campo_telefono;
    ProgressBar progressBar;
    Usuario u;
    boolean editar,borrar;
    Candidato c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevocandidato);

        campo_nombre = (EditText)findViewById(R.id.edNombre_Aspirante);
        campo_apellido = (EditText)findViewById(R.id.edApellido_Aspirante);
        campo_mail = (EditText)findViewById(R.id.edMail_Aspirante);
        campo_telefono = (EditText)findViewById(R.id.edTelefono_Aspirante);
        progressBar = (ProgressBar)findViewById(R.id.prBar_aspirante);
        u = (Usuario)getIntent().getSerializableExtra("usuario");

        editar = getIntent().getBooleanExtra("editar",false);

        if(editar){
            c= (Candidato)getIntent().getSerializableExtra("candidato");

            campo_nombre.setText(c.getNombre());
            campo_apellido.setText(c.getApellido());
            campo_mail.setText(c.getMail());
            campo_telefono.setText(c.getTelefono());
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void onClick(View view){
        if(editar){
            if(!validarCampos())
                Toast.makeText(this, R.string.registro_toast_campos, Toast.LENGTH_SHORT).show();
            else if(!validarMail(campo_mail.getText().toString())){
                Toast.makeText(this, R.string.registro_toast_mail, Toast.LENGTH_SHORT).show();
            }else{
                AlertDialog.Builder alerta = new AlertDialog.Builder(NuevocandidatoActivity.this);
                alerta.setMessage(R.string.editaraspirante_confirmacion)
                        .setTitle(R.string.atencion)
                        .setCancelable(false)
                        .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.setVisibility(View.VISIBLE);
                                editarAspirante_Servicio("http://www.boxwakanda.site/servicios/editar_candidato.php");
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
        }else{
            if(!validarCampos())
                Toast.makeText(this, R.string.registro_toast_campos, Toast.LENGTH_SHORT).show();
            else if(!validarMail(campo_mail.getText().toString())){
                Toast.makeText(this, R.string.registro_toast_mail, Toast.LENGTH_SHORT).show();
            }else {
                AlertDialog.Builder alerta = new AlertDialog.Builder(NuevocandidatoActivity.this);
                alerta.setMessage(R.string.texto_crearaspirante)
                        .setTitle(R.string.atencion)
                        .setCancelable(false)
                        .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.setVisibility(View.VISIBLE);
                                crearAspirante_Servicio("http://www.boxwakanda.site/servicios/insertar_aspirante.php");
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
        }
    }

    private void editarAspirante_Servicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), R.string.aspiranteactualizado_toast, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), InfluencerActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("usuario",u);
                        startActivity(i);
                        finish();
                    }
                }, 2000);
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
                parametros.put("mail",campo_mail.getText().toString());
                parametros.put("telefono",campo_telefono.getText().toString());
                parametros.put("codcandidato",String.valueOf(c.getCodcandidato()));
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
                                        Intent i = new Intent(getApplicationContext(), InfluencerActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.putExtra("usuario",u);
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
                Toast.makeText(getApplicationContext(),R.string.toast_internet, Toast.LENGTH_SHORT).show();
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
        rq.add(stringRequest);
    }

    private boolean validarMail(String email) {
        boolean valido=false;
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        if (mather.find() == true) {
            valido = true;
        }else{
            campo_mail.setBackgroundResource(R.drawable.edit_error);
        }
        return valido;
    }

    private boolean validarCampos() {
        boolean valido=false;
        if(campo_nombre.getText().length() != 0 && campo_apellido.getText().length()!=0
                && campo_mail.getText().length()!=0
                && campo_telefono.getText().length()!=0)
            valido=true;
        return  valido;
    }

}
