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
import com.example.idecargentina.Informes.InformecolportorActivity;
import com.example.idecargentina.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListacolportoresadminActivity extends AppCompatActivity {

    Usuario u, colportor;

    ListView listViewColportores;

    int posicion;
    boolean eliminar, cambiarrol, pasaporte;

    ArrayList<Usuario> listaColportores;
    ArrayList<String> listaInformacion;


    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listacolportoresadmin);

        u = (Usuario)getIntent().getSerializableExtra("usuario");

        listaColportores= new ArrayList<>();
        listViewColportores = (ListView) findViewById(R.id.listViewPasaporteAdmin);
        progressBar = (ProgressBar)findViewById(R.id.prBar_Listacolportoresadmin);

        eliminar = getIntent().getBooleanExtra("eliminar",false);
        cambiarrol = getIntent().getBooleanExtra("cambiarrol",false);
        pasaporte = getIntent().getBooleanExtra("pasaporte",false);

        progressBar.setVisibility(View.VISIBLE);
        buscarColportores_Servicio("http://www.boxwakanda.site/servicios/buscar_colportores.php");


        listViewColportores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                posicion = pos;
                if(eliminar){
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ListacolportoresadminActivity.this);
                    alerta.setMessage(R.string.toast_borrar_colportor)
                            .setTitle(R.string.atencion)
                            .setCancelable(false)
                            .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    colportor=listaColportores.get(posicion);
                                    progressBar.setVisibility(View.VISIBLE);
                                    eliminarColportor_Servicio("http://www.boxwakanda.site/servicios/eliminar_colportor.php",colportor.getCodusuario());

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alerta.show();
                }else if(cambiarrol){
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ListacolportoresadminActivity.this);
                    if(listaColportores.get(posicion).getCodrol()==3){
                        alerta.setMessage(R.string.alert_rol_admin)
                                .setTitle(R.string.atencion)
                                .setCancelable(false)
                                .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        colportor=listaColportores.get(posicion);
                                        progressBar.setVisibility(View.VISIBLE);
                                        cambiarRol_Servicio("http://www.boxwakanda.site/servicios/cambiar_rol.php",colportor.getCodusuario(),colportor.getCodrol());
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        alerta.show();
                    }else if(listaColportores.get(posicion).getCodrol()==2){
                        alerta.setMessage(R.string.alert_rol_user)
                                .setTitle(R.string.atencion)
                                .setCancelable(false)
                                .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        colportor=listaColportores.get(posicion);
                                        progressBar.setVisibility(View.VISIBLE);
                                        cambiarRol_Servicio("http://www.boxwakanda.site/servicios/cambiar_rol.php",colportor.getCodusuario(),colportor.getCodrol());
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

                }else if(pasaporte){
                    Toast.makeText(ListacolportoresadminActivity.this, "pasaporte", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(getApplicationContext(), InformecolportorActivity.class);
                    i.putExtra("usuario",u);
                    i.putExtra("colportor",listaColportores.get(posicion));
                    startActivity(i);
                }
            }
        });
    }

    private void buscarColportores_Servicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
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
                    listViewColportores.setAdapter(adaptador);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ListacolportoresadminActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rq= Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private void cambiarRol_Servicio(String URL, final int codusuario, final int codrol){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String respuesta=response;
                Toast.makeText(getApplicationContext(), R.string.toast_rol_actualizado, Toast.LENGTH_SHORT).show();
                listViewColportores.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), ColportoresadminActivity.class);
                        i.putExtra("usuario",u);
                        startActivity(i);
                        finish();
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListacolportoresadminActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("codusuario",String.valueOf(codusuario));
                parametros.put("codrol",String.valueOf(codrol));
                return parametros;
            }
        };
        RequestQueue rq= Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private void eliminarColportor_Servicio(String URL, int codusuario){
        final int codusuario_eliminar= codusuario;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), R.string.toast_colportor_eliminado, Toast.LENGTH_SHORT).show();
                listViewColportores.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), ColportoresadminActivity.class);
                        i.putExtra("usuario",u);
                        startActivity(i);
                        finish();
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListacolportoresadminActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("codusuario",String.valueOf(codusuario_eliminar));
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
