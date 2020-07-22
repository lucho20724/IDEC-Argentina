package com.example.idecargentina.ActivitiesCommon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.idecargentina.Admin.AdminActivity;
import com.example.idecargentina.Entidades.Campo;
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;
import com.example.idecargentina.User.UserActivity;
import com.example.idecargentina.Utilidades.Funciones;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    EditText campo_nombre, campo_apellido, campo_mail, campo_telefono, campo_nro, campo_pass, campo_pass2, campo_campo;
    int codcampo;
    ProgressBar progressBar;
    Spinner spCampos;

    ArrayList<Campo> listaCampos;
    ArrayList<String> listaInformacion;

    String responseGlobal;

    Funciones fun;

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
        campo_campo = (EditText)findViewById(R.id.edCampo_registro);

        spCampos = (Spinner)findViewById(R.id.spCampo_registro);

        progressBar=(ProgressBar)findViewById(R.id.prBar_Registro);

        progressBar.setVisibility(View.INVISIBLE);

        fun = new Funciones();

        listaCampos = new ArrayList<>();
        buscarCampos_Servicio("http://www.boxwakanda.site/servicios/buscar_campos.php");
        spCampos.setSelection(1);

        spCampos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                campo_campo.setText(parent.getItemAtPosition(position).toString());
                codcampo = obtenerIdCampo(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void onClick(View v){
        Intent i= new Intent(this, MainActivity.class);
        if(!validarPassword()) {
            Toast.makeText(this, R.string.registro_toast_password, Toast.LENGTH_SHORT).show();
        }else if(!validarCampos()){
            Toast.makeText(this, R.string.registro_toast_campos, Toast.LENGTH_SHORT).show();
        }else if(!validarMail(campo_mail.getText().toString())){
            Toast.makeText(this, R.string.registro_toast_mail, Toast.LENGTH_SHORT).show();
        }else if(!validarSelectCampo()){
            Toast.makeText(this, R.string.registro_toast_camposcolp, Toast.LENGTH_SHORT).show();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            crearUsuario_Servicio("http://www.boxwakanda.site/servicios/insertar_usuario.php");
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

    private boolean validarSelectCampo(){
        boolean valido=false;
        if(!spCampos.getSelectedItem().toString().equals(R.string.registro_campo)){
            valido=true;
        }else{
            campo_campo.setBackgroundResource(R.drawable.edit_error);
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
                                        progressBar.setVisibility(View.VISIBLE);
                                        validarUsuario_Servicio("http://www.boxwakanda.site/servicios/verificar_usuario.php");
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
                parametros.put("codcampo",String.valueOf(codcampo));
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

    private void buscarCampos_Servicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("campos");

                    for(int i=0;i<array.length();i++){
                        JSONObject campo = array.getJSONObject(i);
                        Campo c = new Campo(
                                campo.getInt("codcampo"),
                                campo.getString("nombre"),
                                campo.getString("abreviatura")
                        );
                        listaCampos.add(c);
                    }

                    obtenerLista();
                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,listaInformacion);
                    spCampos.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistroActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }
    private void obtenerLista() {
        listaInformacion = new ArrayList<String>();
        listaInformacion.add(String.valueOf(getResources().getString(R.string.registro_campo)));
        for (int i=0; i<listaCampos.size();i++){
            listaInformacion.add(listaCampos.get(i).getAbreviatura());
        }
    }

    private void validarUsuario_Servicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseGlobal=response;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(!responseGlobal.isEmpty()){

                            Usuario u =fun.obtenerUsuario(responseGlobal, getApplicationContext());
                            //Usuario u = obtenerUsuario(responseGlobal);
                            guardarPreferencias(u);
                            Intent i;

                            if(u.getCodrol()==1 || u.getCodrol()==2){
                                i = new Intent(getApplicationContext(), AdminActivity.class);
                            }else{
                                i = new Intent(getApplicationContext(), UserActivity.class);
                            }
                            i.putExtra("usuario",u);
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(RegistroActivity.this, R.string.toast_datosincorrectos,Toast.LENGTH_SHORT).show();
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
                parametros.put("password",campo_pass.getText().toString());
                return parametros;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private Usuario obtenerUsuario(String response){
        Usuario u = new Usuario();
        try{
            JSONObject jsonObject=new JSONObject(response);
            u.setCodusuario(jsonObject.getInt("codusuario"));
            u.setNombre(jsonObject.getString("nombre"));
            u.setApellido(jsonObject.getString("apellido"));
            u.setMail(jsonObject.getString("mail"));
            u.setPassword(jsonObject.getString("password"));
            u.setNroalumno(jsonObject.getInt("nroalumno"));
            u.setTelefono(jsonObject.getString("telefono"));
            u.setCodrol(jsonObject.getInt("codrol"));
            u.setCodcampo(jsonObject.getInt("codcampo"));

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
        return u;
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

    private int obtenerIdCampo(String campo){
        int codcampo;
        switch (campo){
            case "ABO":codcampo=1;break;
            case "AAC":codcampo=2;break;
            case "AAN":codcampo=3;break;
            case "AAS":codcampo=4;break;
            case "MACO":codcampo=5;break;
            case "MANO":codcampo=6;break;
            case "MIBON":codcampo=7;break;
            default:
                codcampo=0;
        }
        return codcampo;
    }

}
