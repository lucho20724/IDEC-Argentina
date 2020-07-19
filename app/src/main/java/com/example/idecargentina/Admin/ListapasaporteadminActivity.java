package com.example.idecargentina.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.example.idecargentina.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ListapasaporteadminActivity extends AppCompatActivity {
    Usuario u, colportor;
    boolean habilitar;

    ArrayList<Usuario> listaColportores;
    ArrayList<String> listaInformacion;

    ProgressBar progressBar;

    int posicion;

    ListView listViewPasaporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listapasaporteadmin);

        u = (Usuario)getIntent().getSerializableExtra("usuario");

        listaColportores= new ArrayList<>();
        listViewPasaporte = (ListView) findViewById(R.id.listViewPasaporteAdmin);
        progressBar = (ProgressBar)findViewById(R.id.prBar_Listapasaporteadmin);

        habilitar = getIntent().getBooleanExtra("habilitar",false);

        progressBar.setVisibility(View.VISIBLE);
        buscarPasaportes_Servicio("http://www.boxwakanda.site/servicios/buscar_pasaportes.php");

        listViewPasaporte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                posicion=pos;
                if(habilitar){
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ListapasaporteadminActivity.this);
                    alerta.setMessage("Desea habilitar el pasaporte al colportor seleccionado?")//TODO
                            .setTitle(R.string.atencion)
                            .setCancelable(false)
                            .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    colportor=listaColportores.get(posicion);
                                    progressBar.setVisibility(View.VISIBLE);
                                    cambiarPasaporte_Servicio("http://www.boxwakanda.site/servicios/cambiar_pasaporte.php",colportor.getCodusuario(), TRUE);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alerta.show();
                }else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ListapasaporteadminActivity.this);
                    alerta.setMessage("Desea deshabilitar el pasaporte al colportor seleccionado?")//TODO
                            .setTitle(R.string.atencion)
                            .setCancelable(false)
                            .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    colportor=listaColportores.get(posicion);
                                    progressBar.setVisibility(View.VISIBLE);
                                    cambiarPasaporte_Servicio("http://www.boxwakanda.site/servicios/cambiar_pasaporte.php",colportor.getCodusuario(), FALSE);
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
        });
    }

    private void buscarPasaportes_Servicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    String hola = response;
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("colportores");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject colportor = array.getJSONObject(i);
                        Usuario u = new Usuario(
                                colportor.getInt("codusuario"),
                                colportor.getString("nombre"),
                                colportor.getString("apellido"),
                                colportor.getString("mail"),
                                colportor.getString("password"),
                                colportor.getInt("nroalumno"),
                                colportor.getString("telefono"),
                                colportor.getInt("codrol"),
                                colportor.getInt("codcampo"),
                                Boolean.parseBoolean(colportor.getString("pasaporte"))
                        );
                        listaColportores.add(u);
                    }
                    obtenerLista();
                    ArrayAdapter<String> adaptador = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, listaInformacion);
                    listViewPasaporte.setAdapter(adaptador);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ListapasaporteadminActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                if(habilitar) parametros.put("habilitar","True");
                else
                    parametros.put("habilitar","False");
                return parametros;
            }
        };
        RequestQueue rq= Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private void cambiarPasaporte_Servicio(String URL, final int codusuario, final boolean habilitacion ){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String respuesta=response;
                Toast.makeText(getApplicationContext(), R.string.toast_rol_actualizado, Toast.LENGTH_SHORT).show();
                listViewPasaporte.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), PasaporteadminActivity.class);
                        i.putExtra("usuario",u);
                        startActivity(i);
                        finish();
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListapasaporteadminActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("codusuario",String.valueOf(codusuario));
                if(habilitacion){
                    parametros.put("habilitacion","True");
                }else{
                    parametros.put("habilitacion","False");
                }
                return parametros;
            }
        };
        RequestQueue rq= Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private void obtenerLista(){
        listaInformacion = new ArrayList<String>();
        for (int i=0; i<listaColportores.size();i++){
            listaInformacion.add(listaColportores.get(i).getNombre()+"  "+listaColportores.get(i).getApellido());
        }
    }
}
