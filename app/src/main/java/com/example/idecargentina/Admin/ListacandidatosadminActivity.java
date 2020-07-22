package com.example.idecargentina.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.Informes.InformecandidatoadminActivity;
import com.example.idecargentina.R;
import com.example.idecargentina.User.ListacandidatosActivity;
import com.example.idecargentina.User.NuevocandidatoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListacandidatosadminActivity extends AppCompatActivity {

    Usuario u;
    Candidato c;

    ListView listViewCandidatos;

    ArrayList<Candidato> listaCandidatos;
    ArrayList<String> listaInformacion;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listacandidatosadmin);

        u = (Usuario)getIntent().getSerializableExtra("usuario");

        progressBar = (ProgressBar)findViewById(R.id.prBar_Listacandidatosadmin);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();

        listaCandidatos= new ArrayList<>();
        listViewCandidatos = (ListView) findViewById(R.id.listViewCandidatosAdmin);
        buscarCandidatos_Servicio("http://www.boxwakanda.site/servicios/buscar_candidatos.php");

        listViewCandidatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent i = new Intent(getApplicationContext(), InformecandidatoadminActivity.class);
                c=listaCandidatos.get(pos);
                i.putExtra("usuario",u);
                i.putExtra("candidato",c);
                startActivity(i);
            }
        });
    }

    private void buscarCandidatos_Servicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
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
                            obtenerLista();
                            ArrayAdapter<String> adaptador = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, listaInformacion);
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
                Toast.makeText(ListacandidatosadminActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rq= Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private void obtenerLista() {
        listaInformacion = new ArrayList<String>();
        for (int i=0; i<listaCandidatos.size();i++){
            listaInformacion.add(listaCandidatos.get(i).getNombre()+"  "+listaCandidatos.get(i).getApellido());
        }
    }


}

