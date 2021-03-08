package com.example.idecargentina.ActivitiesCommon;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends AppCompatActivity {

    EditText campo_passvieja, campo_pass1, campo_pass2;
    String passwordvieja;
    Usuario usuario;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        campo_passvieja = (EditText)findViewById(R.id.edPassvieja_password);
        campo_pass1 = (EditText)findViewById(R.id.edPass1_password);
        campo_pass2 = (EditText)findViewById(R.id.edPass2_password);

        progressBar=(ProgressBar)findViewById(R.id.prBar_Password);

        usuario = (Usuario)getIntent().getSerializableExtra("usuario");
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void onClick(View view){
        if(!validarCamposVacios())
            Toast.makeText(this, R.string.registro_toast_campos, Toast.LENGTH_SHORT).show();
        else if(!validarPasswordIguales()) {
            Toast.makeText(this, R.string.registro_toast_password, Toast.LENGTH_SHORT).show();
            campo_pass1.setBackgroundResource(R.drawable.edit_error);
            campo_pass2.setBackgroundResource(R.drawable.edit_error);
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    comprobarpassword_Servicio("http://192.168.42.177/IDEC/obtener_password.php");
                }
            },3000);
        }
    }

    private void comprobarpassword_Servicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()) {
                    try {
                        progressBar.setVisibility(View.INVISIBLE);
                        JSONObject jsonObject = new JSONObject(response);
                        String passwordvieja = jsonObject.getString("password");
                        String passwordvieja1 = campo_passvieja.getText().toString();
                        if (passwordvieja1.equals(passwordvieja)) {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(PasswordActivity.this);
                            alerta.setMessage(R.string.text_confirmacionpassword)
                                    .setTitle(R.string.atencion)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            modificarPassword_Servicio("http://192.168.42.177/IDEC/editar_password.php");
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            alerta.show();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            campo_passvieja.setBackgroundResource(R.drawable.edit_error);
                            Toast.makeText(PasswordActivity.this, R.string.toast_contraseña, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(PasswordActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
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
                parametros.put("codusuario",String.valueOf(usuario.getCodusuario()));
                return parametros;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private void modificarPassword_Servicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                finish();
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
                parametros.put("codusuario",String.valueOf(usuario.getCodusuario()));
                parametros.put("password",campo_pass2.getText().toString());
                return parametros;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private boolean validarPasswordIguales() {
        boolean valido=false;
        if(campo_pass1.getText().toString().equals(campo_pass2.getText().toString())){
            valido=true;
        }
        return valido;
    }

    private boolean validarCamposVacios() {
        boolean valido=false;
        if(campo_passvieja.getText().toString().isEmpty()||campo_pass1.getText().toString().isEmpty()||campo_pass2.getText().toString().isEmpty()){
            return valido;
        }else{
            return true;
        }
    }
}
