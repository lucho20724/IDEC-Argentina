package com.example.idecargentina.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasaporteuserActivity extends AppCompatActivity {

    ImageView imagen;
    Usuario usuario;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasaporteuser);

        usuario=(Usuario)getIntent().getSerializableExtra("usuario");

        imagen= (ImageView)findViewById(R.id.imagen_Pasaporte);

        progressBar = (ProgressBar)findViewById(R.id.prBar_Pasaporte);
        progressBar.setVisibility(View.VISIBLE);

        consultarPasaporte_Servicio("http://192.168.42.177/IDEC/obtener_pasaporte.php]");
    }

    private void consultarPasaporte_Servicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PasaporteuserActivity.this, "aca llega", Toast.LENGTH_SHORT).show();
                 /*if(!response.isEmpty()){
                     progressBar.setVisibility(View.VISIBLE);
                     try {
                         JSONObject jsonObject = new JSONObject(response);
                         int pasaporteint= jsonObject.getInt("pasaporte");
                         if(pasaporteint==0)
                             imagen.setImageResource(R.drawable.accesodenegado);
                         else
                             imagen.setImageResource(R.drawable.pasaporte);

                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }
                 else{
                     Toast.makeText(PasaporteuserActivity.this, "aca llega", Toast.LENGTH_SHORT).show();
                 }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyError error1= error;
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(PasaporteuserActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
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

}
