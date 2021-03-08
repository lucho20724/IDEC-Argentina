package com.example.idecargentina.User;

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
import com.example.idecargentina.Entidades.Candidato;
import com.example.idecargentina.Entidades.Lista;
import com.example.idecargentina.Entidades.Punto;
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.Informes.InformepuntoActivity;
import com.example.idecargentina.R;
import com.example.idecargentina.Utilidades.CustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListapuntosActivity extends AppCompatActivity {

    Usuario u;
    Punto p;
    boolean eliminar, editar, ver;

    ListView listViewPuntos;

    ProgressBar progressBar;
    String responseglobal;

    int posicion;

    ArrayList<Punto> listaPuntos;
    ArrayList<String> listaInformacion;

    List<Lista> lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listapuntos);

        listViewPuntos = (ListView) findViewById(R.id.listViewPuntosUser);
        u = (Usuario)getIntent().getSerializableExtra("usuario");
        listaPuntos= new ArrayList<>();

        progressBar = (ProgressBar)findViewById(R.id.prBar_Listapuntosuser);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();

        buscarPuntos_Servicio("http://192.168.42.177/IDEC/buscar_puntos_usuario.php");

        eliminar = getIntent().getBooleanExtra("eliminar",false);
        editar = getIntent().getBooleanExtra("editar",false);
        ver = getIntent().getBooleanExtra("ver",false);

        listViewPuntos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                posicion=pos;
                p=listaPuntos.get(posicion);

                if(editar){
                    Intent i = new Intent(getApplicationContext(), InformepuntoActivity.class);
                    i.putExtra("editar",editar);
                    i.putExtra("usuario",u);
                    i.putExtra("punto",p);
                    startActivity(i);
                }else if(ver){
                    Intent i = new Intent(getApplicationContext(), InformepuntoActivity.class);
                    i.putExtra("ver",ver);
                    i.putExtra("usuario",u);
                    i.putExtra("punto",p);
                    startActivity(i);
                }else if(eliminar){
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ListapuntosActivity.this);
                    alerta.setMessage(R.string.toast_punto)
                            .setTitle(R.string.atencion)
                            .setCancelable(false)
                            .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    p=listaPuntos.get(posicion);
                                    progressBar.setVisibility(View.VISIBLE);
                                    eliminarPunto_Servicio("http://192.168.42.177/IDEC/eliminar_punto.php",p.getcodpunto());
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

    private void buscarPuntos_Servicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        responseglobal=response;
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("puntos");

                            for(int i=0;i<array.length();i++){
                                JSONObject punto = array.getJSONObject(i);
                                Punto p = new Punto(
                                        punto.getInt("codpunto"),
                                        punto.getDouble("latitud"),
                                        punto.getDouble("longitud"),
                                        punto.getString("titulo"),
                                        punto.getString("descripcion"),
                                        punto.getInt("codusuario")
                                );
                                listaPuntos.add(p);
                            }
                            CustomAdapter adaptador = new CustomAdapter(getApplicationContext(), obtenerLista());
                            listViewPuntos.setAdapter(adaptador);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ListapuntosActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("codusuario",String.valueOf(u.getCodusuario()));
                return parametros;
            }
        };
        RequestQueue rq= Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private void eliminarPunto_Servicio(String URL, int codpunto){
        final int codpunto_eliminar = codpunto; //variable global para el metodo getParams
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), R.string.toast_puntoeliminado, Toast.LENGTH_SHORT).show();
                listViewPuntos.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListapuntosActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("codpunto",String.valueOf(codpunto_eliminar));
                return parametros;
            }
        };
        RequestQueue rq= Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private List<Lista> obtenerLista() {
        lst = new ArrayList<Lista>();
        for (int i=0; i<listaPuntos.size();i++){
            Lista lista = null;
            lista = new Lista(listaPuntos.get(i).getcodpunto(),R.drawable.marcadorpunto, listaPuntos.get(i).getTitulo(),"lat:"+listaPuntos.get(i).getLatitud()+" | long:"+listaPuntos.get(i).getLongitud());
            lst.add(lista);
        }
        return lst;
    }
}