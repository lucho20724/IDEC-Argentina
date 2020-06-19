package com.example.idecargentina.Informes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
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
import com.example.idecargentina.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InformecandidatoadminActivity extends AppCompatActivity {
    TextView txtNombre, txtApellido, txtMail, txtTelefono, txtColportor;

    Candidato c;
    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informecandidatoadmin);

        txtNombre = (TextView)findViewById(R.id.lblNombre_infocandidatoadmin);
        txtApellido= (TextView)findViewById(R.id.lblApellido_infocandidatoadmin);
        txtMail = (TextView)findViewById(R.id.lblMail_infocandidatoadmin);
        txtTelefono = (TextView)findViewById(R.id.lblTelefono_infocandidatoadmin);
        txtColportor = (TextView)findViewById(R.id.lblColportor_infocandidatoadmin);


        u = (Usuario)getIntent().getSerializableExtra("usuario");
        c = (Candidato)getIntent().getSerializableExtra("candidato");

        buscarColportor_Servicio("http://192.168.42.177/IDEC/buscar_colportor.php", c.getCodusuario());

        txtNombre.setText(c.getNombre());
        txtApellido.setText(c.getApellido());
        txtMail.setText(c.getMail());
        txtTelefono.setText(c.getTelefono());
    }


    private void buscarColportor_Servicio(String URL, final int codusuario){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Usuario colportor = obtenerColportor(response);
                    txtColportor.setText(colportor.getNombre()+" "+colportor.getApellido());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("codusuario",String.valueOf(codusuario));
                return parametros;
            }
        };
        RequestQueue rq= Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    private Usuario obtenerColportor(String response){
        Usuario colportor = new Usuario();
        try{
            JSONObject jsonObject=new JSONObject(response);
            colportor.setCodusuario(jsonObject.getInt("codusuario"));
            colportor.setNombre(jsonObject.getString("nombre"));
            colportor.setApellido(jsonObject.getString("apellido"));
            colportor.setMail(jsonObject.getString("mail"));
            colportor.setPassword(jsonObject.getString("password"));
            colportor.setNroalumno(jsonObject.getInt("nroalumno"));
            colportor.setTelefono(jsonObject.getString("telefono"));
            colportor.setCodrol(jsonObject.getInt("codrol"));

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
        return colportor;
    }
}
