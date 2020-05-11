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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    EditText campo_nombre, campo_apellido, campo_mail, campo_telefono, campo_nro, campo_pass, campo_pass2;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        campo_nombre = (EditText)findViewById(R.id.edNombre_registro);
        campo_apellido = (EditText)findViewById(R.id.edApellido_registro);
        campo_telefono = (EditText)findViewById(R.id.edTelefono_Registro);
        campo_mail = (EditText)findViewById(R.id.edMail_registro);
        campo_nro = (EditText)findViewById(R.id.edNro_registro);
        campo_pass = (EditText)findViewById(R.id.edPass_registro);
        campo_pass2 = (EditText)findViewById(R.id.edPass2_registro);
        progressBar=(ProgressBar)findViewById(R.id.prBar_Registro);

        progressBar.setVisibility(View.INVISIBLE);
    }

    public void onClick(View v){
        Intent i= new Intent(this,MainActivity.class);
        if(!validarPassword()) {
            Toast.makeText(this, R.string.registro_toast_password, Toast.LENGTH_SHORT).show();
        }else if(!validarCampos()){
            Toast.makeText(this, R.string.registro_toast_campos, Toast.LENGTH_SHORT).show();
        }else if(!validarMail(campo_mail.getText().toString())){
            Toast.makeText(this, R.string.registro_toast_mail, Toast.LENGTH_SHORT).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            crearUsuario_Servicio("http://192.168.42.177/IDEC/insertar_usuario.php");
        }
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
                && campo_mail.getText().length()!=0 && campo_nro.getText().length()!=0
                && campo_pass.getText().length()!=0 && campo_pass2.getText().length()!=0
                && campo_telefono.getText().length()!=0)
            valido=true;
        return  valido;
    }

    private boolean validarPassword(){
        boolean valido=false;
        if(campo_pass.getText().toString().equals(campo_pass2.getText().toString())){
            valido=true;
        }else{
            campo_pass.setBackgroundResource(R.drawable.edit_error);
            campo_pass2 .setBackgroundResource(R.drawable.edit_error);
        }
        return valido;
    }

    private void crearUsuario_Servicio(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), R.string.registro_toast_cuentacreada, Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroActivity.this);
                        alerta.setMessage(R.string.registro_alert_contenido)
                                .setTitle(R.string.registro_alert_titulo)
                                .setCancelable(false)
                                .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //TODO
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                        alerta.show();
                    }
                },3000);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.toast_internet,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("email",campo_mail.getText().toString());
                parametros.put("password",campo_pass.getText().toString());
                parametros.put("nombre",campo_nombre.getText().toString());
                parametros.put("apellido",campo_apellido.getText().toString());
                parametros.put("telefono",campo_telefono.getText().toString());
                parametros.put("nro",campo_nro.getText().toString());
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
