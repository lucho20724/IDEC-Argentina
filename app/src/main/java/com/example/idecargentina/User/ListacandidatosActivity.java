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
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.Informes.InformecandidatoActivity;
import com.example.idecargentina.R;
import com.example.idecargentina.Utilidades.CustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListacandidatosActivity extends AppCompatActivity {

    Usuario u;
    Candidato c;
    boolean eliminar, editar;
    ListView listViewCandidatos;

    ProgressBar progressBar;

    String responseglobal;

    int posicion;

    ArrayList<Candidato> listaCandidatos;
    ArrayList<String> listaInformacion;

    List<Lista> lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listacandidatos);

        listViewCandidatos = (ListView) findViewById(R.id.listViewCandidatos);
        u = (Usuario)getIntent().getSerializableExtra("usuario");
        listaCandidatos= new ArrayList<>();

        progressBar = (ProgressBar)findViewById(R.id.prBar_Listacandidatosadmin);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();

        buscarCandidatos_Servicio("http://192.168.42.177/IDEC/buscar_candidatos.php");

        eliminar = getIntent().getBooleanExtra("eliminar",false);
        editar = getIntent().getBooleanExtra("editar",false);


        listViewCandidatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                posicion=pos;
                if(editar){
                    Intent i = new Intent(getApplicationContext(), NuevocandidatoActivity.class);
                    c=listaCandidatos.get(pos);
                    i.putExtra("usuario",u);
                    i.putExtra("candidato",c);
                    i.putExtra("editar",true);
                    startActivity(i);

                }else if(eliminar){
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ListacandidatosActivity.this);
                    alerta.setMessage(R.string.texto_borraraspirante)
                            .setTitle(R.string.atencion)
                            .setCancelable(false)
                            .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    c=listaCandidatos.get(posicion);
                                    progressBar.setVisibility(View.VISIBLE);
                                    eliminarCanditado_Servicio("http://192.168.42.177/IDEC/eliminar_candidato.php",c.getCodcandidato());
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
                else{
                    Intent i = new Intent(getApplicationContext(), InformecandidatoActivity.class);
                    c=listaCandidatos.get(pos);
                    i.putExtra("usuario",u);
                    i.putExtra("candidato",c);
                    startActivity(i);
                }
            }
        });
    }

    private void buscarCandidatos_Servicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        responseglobal=response;
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("aspirantes");

                            for(int i=0;i<array.length();i++){
                                JSONObject candidato = array.getJSONObject(i);
                                Candidato c = new Candidato(
                                        candidato.getInt("codaspirante"),
                                        candidato.getString("nombre"),
                                        candidato.getString("apellido"),
                                        candidato.getString("mail"),
                                        candidato.getString("telefono"),
                                        candidato.getInt("codusuario")
                                );
                                listaCandidatos.add(c);
                            }
                            CustomAdapter adaptador = new CustomAdapter(getApplicationContext(), obtenerLista());
                            listViewCandidatos.setAdapter(adaptador);

                        } catch (JSONException e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ListacandidatosActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
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


    private void eliminarCanditado_Servicio(String URL, int codcandidato){
        final int codcandidato_eliminar = codcandidato; //variable global para el metodo getParams
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), R.string.toast_aspiranteeliminado, Toast.LENGTH_SHORT).show();
                listViewCandidatos.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(),InfluencerActivity.class);
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
                Toast.makeText(ListacandidatosActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("codcandidato",String.valueOf(codcandidato_eliminar));
                return parametros;
            }
        };
        RequestQueue rq= Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private List<Lista> obtenerLista() {
        listaInformacion = new ArrayList<String>();
        Lista lista = null;

        for (int i=0; i<listaCandidatos.size();i++){
            lista = new Lista( listaCandidatos.get(i).getCodcandidato(),R.drawable.ic_usuario,listaCandidatos.get(i).getNombre()+" "+listaCandidatos.get(i).getApellido(), listaCandidatos.get(i).getNombre());
        }
        return lst;
    }
}
