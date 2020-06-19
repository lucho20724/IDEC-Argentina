package com.example.idecargentina.ActivitiesCommon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.idecargentina.Admin.AdminActivity;
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;
import com.example.idecargentina.User.UserActivity;
import com.example.idecargentina.Utilidades.Funciones;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    RequestQueue rq;
    EditText campo_mail, campo_password;
    ProgressBar progressBar;
    String responseGlobal;
    Funciones fun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fun= new Funciones();

        campo_mail=(EditText)findViewById(R.id.edMail_Login);
        campo_password=(EditText)findViewById(R.id.edPassword_Login);
        progressBar=(ProgressBar)findViewById(R.id.prBar_Login);

        progressBar.setVisibility(View.INVISIBLE);
        rq= Volley.newRequestQueue(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnEntrar_Login:
                try{
                    if(!campo_mail.getText().toString().isEmpty() && !campo_password.getText().toString().isEmpty()){
                        progressBar.setVisibility(View.VISIBLE);
                        validarUsuario_Servicio("http://192.168.42.177/IDEC/verificar_usuario.php");
                    }else {
                        Toast.makeText(this,R.string.registro_toast_campos,Toast.LENGTH_SHORT).show();
                    }
                }catch (Error e){
                    Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void validarUsuario_Servicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                responseGlobal=response;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(!responseGlobal.isEmpty()){
                            Usuario u = fun.obtenerUsuario(response,getApplicationContext());
                            guardarPreferencias(u);
                            Intent i;

                            if(u.getCodrol()==1 || u.getCodrol()==2){
                                i = new Intent(getApplicationContext(), AdminActivity.class);
                            }else{
                                i = new Intent(getApplicationContext(), UserActivity.class);
                            }
                            i.putExtra("usuario",u);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, R.string.toast_datosincorrectos,Toast.LENGTH_SHORT).show();
                        }
                    }
                },3000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),R.string.toast_internet, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("email",campo_mail.getText().toString());
                parametros.put("password",campo_password.getText().toString());
                return parametros;
            }
        };
        rq = Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private void guardarPreferencias(Usuario usuario){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();

        Gson gson = new Gson();
        String json=gson.toJson(usuario);
        editor.putString("usuario",json);
        editor.putBoolean("sesion",true);
        editor.commit();
    }

}
