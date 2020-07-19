package com.example.idecargentina.Utilidades;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Funciones {

    Usuario usuario;

    public Funciones() {
    }

    public Usuario obtenerUsuarioId_Servicio(final int codusuario, final Context context){
        String URL = "http://www.boxwakanda.site/servicios/obtener_usuario_id.php";
        usuario = new Usuario();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Usuario u =obtenerUsuario(response,context);
                usuario = u;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,R.string.toast_internet, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("codusuario",String.valueOf(codusuario));
                return parametros;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(context);
        rq.add(stringRequest);
        return usuario;
    }

    public Usuario obtenerUsuario(String response, Context context){
        Usuario u = new Usuario();
        try{
            JSONObject jsonObject=new JSONObject(response);

            String response2=response;
            u.setCodusuario(jsonObject.getInt("codusuario"));
            u.setNombre(jsonObject.getString("nombre"));
            u.setApellido(jsonObject.getString("apellido"));
            u.setMail(jsonObject.getString("mail"));
            u.setPassword(jsonObject.getString("password"));
            u.setNroalumno(jsonObject.getInt("nroalumno"));
            u.setTelefono(jsonObject.getString("telefono"));
            u.setCodrol(jsonObject.getInt("codrol"));
            u.setCodcampo(jsonObject.getInt("codcampo"));
            int estadopasaporte=jsonObject.getInt("pasaporte");
            if(estadopasaporte==0) u.setPasaporte(false);
            else u.setPasaporte(true);

        }catch (Exception e){
            Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
        return u;
    }

    public Boolean comprobarConexion(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
