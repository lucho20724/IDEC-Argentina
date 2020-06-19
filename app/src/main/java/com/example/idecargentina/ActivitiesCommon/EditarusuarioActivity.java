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
import com.example.idecargentina.User.NuevocandidatoActivity;
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

public class EditarusuarioActivity extends AppCompatActivity {

    EditText campo_nombre, campo_apellido, campo_mail, campo_telefono, campo_nro, campo_campo;
    int codcampo;
    ProgressBar progressBar;
    Spinner spCampos;

    ArrayList<Campo> listaCampos;
    ArrayList<String> listaInformacion;

    Usuario u, usuariomodificado;

    Funciones fun;

    String responseGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarusuario);

        u = (Usuario)getIntent().getSerializableExtra("usuario");
        campo_nombre = (EditText)findViewById(R.id.edNombre_modusuario);
        campo_apellido = (EditText)findViewById(R.id.edApellido_modusuario);
        campo_telefono = (EditText)findViewById(R.id.edTelefono_modusuario);
        campo_mail = (EditText)findViewById(R.id.edMail_modusuario);
        campo_nro = (EditText)findViewById(R.id.edNro_modusuario);
        campo_campo = (EditText)findViewById(R.id.edCampo_modusuario);

        spCampos = (Spinner)findViewById(R.id.spCampo_modusuario);

        progressBar=(ProgressBar)findViewById(R.id.prBar_modusuario);

        progressBar.setVisibility(View.INVISIBLE);

        fun = new Funciones();

        listaCampos = new ArrayList<>();
        buscarCampos_Servicio("http://192.168.42.177/IDEC/buscar_campos.php");

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

        completarCampos();
    }

    public void onClick(View v){
        Intent i=null;
        switch (v.getId()){
            case R.id.btnGuardar_modusuario:
                i= new Intent(this, MainActivity.class);
                if(!validarCampos()){
                    Toast.makeText(this, R.string.registro_toast_campos, Toast.LENGTH_SHORT).show();
                }else if(!validarMail(campo_mail.getText().toString())){
                    Toast.makeText(this, R.string.registro_toast_mail, Toast.LENGTH_SHORT).show();
                }else if(!validarSelectCampo()){
                    if(fun.comprobarConexion(getApplicationContext())) {
                        Toast.makeText(this, R.string.registro_toast_camposcolp, Toast.LENGTH_SHORT).show();
                        campo_campo.setBackgroundResource(R.drawable.edit_error);
                    }else
                        Toast.makeText(getApplicationContext(), R.string.toast_internet,Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(EditarusuarioActivity.this);
                    alerta.setMessage("Desesa guardar las modificaciones?")//TODO
                            .setTitle(R.string.atencion)
                            .setCancelable(false)
                            .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    editarUsuario_Servicio("http://192.168.42.177/IDEC/editar_usuario.php");
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
                break;
            case R.id.btnPassword_modusuario:
                i = new Intent(getApplicationContext(),PasswordActivity.class);
                i.putExtra("usuario",u);
                startActivity(i);
                break;
        }
    }

    private void editarUsuario_Servicio(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                obtenerUserMod_Servicio("http://192.168.42.177/IDEC/obtener_usuario_id.php");
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
                parametros.put("email",campo_mail.getText().toString());
                parametros.put("nombre",campo_nombre.getText().toString());
                parametros.put("apellido",campo_apellido.getText().toString());
                parametros.put("telefono",campo_telefono.getText().toString());
                parametros.put("nro",campo_nro.getText().toString());
                parametros.put("codcampo",String.valueOf(codcampo));
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

    private void obtenerUserMod_Servicio(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()) {
                    responseGlobal = response;
                    Usuario usermod = fun.obtenerUsuario(response, getApplicationContext());
                    usuariomodificado=usermod;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent i;
                            guardarPreferencias(usuariomodificado);
                            if (u.getCodrol() == 3) {
                                i = new Intent(getApplicationContext(), UserActivity.class);
                            } else {
                                i = new Intent(getApplicationContext(), AdminActivity.class);
                            }
                            i.putExtra("usuariomod", usuariomodificado);
                            i.putExtra("mod", true);

                            startActivity(i);
                        }
                    },3000);
                }
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
                && campo_mail.getText().length()!=0 && campo_nro.getText().length()!=0
                && campo_telefono.getText().length()!=0)
            valido=true;
        return  valido;
    }

    private boolean validarSelectCampo(){
        boolean valido=false;
        try {
            if (!spCampos.getSelectedItem().toString().equals("Seleccione el campo donde vas a colportar")) {//TODO
                valido = true;
            } else if (u.getCodrol() == 0 || u.getCodrol() == 1) {
                valido = true;
            }
        }catch (Exception e){
            valido=false;
        }
        return valido;
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
                Toast.makeText(EditarusuarioActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }
    private void obtenerLista() {
        listaInformacion = new ArrayList<String>();
        listaInformacion.add(getApplicationContext().getResources().getString(R.string.registro_campo));
        for (int i=0; i<listaCampos.size();i++){
            listaInformacion.add(listaCampos.get(i).getAbreviatura());
        }
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

    private int setearSpinner(int codcampo){
        int posicion=0;
        switch (codcampo){
            case 0: posicion=0;break;
            case 1: posicion=1;break;
            case 2: posicion=2;break;
            case 3: posicion=3;break;
            case 4: posicion=4;break;
            case 5: posicion=5;break;
            case 6: posicion=6;break;
            case 7: posicion=7;break;
        }
        return  posicion;
    }

    private void completarCampos(){
        campo_nombre.setText(u.getNombre());
        campo_apellido.setText(u.getApellido());
        campo_telefono.setText(u.getTelefono());
        campo_mail.setText(u.getMail());
        campo_nro.setText(String.valueOf(u.getNroalumno()));
        spCampos.setSelection(setearSpinner(u.getCodcampo()));
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
