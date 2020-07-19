package com.example.idecargentina.User;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.idecargentina.ActivitiesCommon.LoginActivity;
import com.example.idecargentina.Admin.AdminActivity;
import com.example.idecargentina.Admin.ListacolportoresadminActivity;
import com.example.idecargentina.Entidades.Punto;
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;

    ArrayList<Punto> listaPuntos;

    FloatingActionButton btnMiubicacion, btnGuardarubicacion, btnVerPuntos;

    ProgressBar progressBar;

    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        u = (Usuario)getIntent().getSerializableExtra("usuario");

        progressBar = (ProgressBar)findViewById(R.id.prBar_mapa);

        progressBar.setVisibility(View.INVISIBLE);
        progressBar.bringToFront();


        btnMiubicacion = (FloatingActionButton)findViewById(R.id.btnUbicacion_Maps);
        btnGuardarubicacion = (FloatingActionButton)findViewById(R.id.btnGuardarpunto_Maps);
        btnVerPuntos = (FloatingActionButton)findViewById(R.id.btnVerpuntos_Maps);

        btnMiubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miUbicacion();
            }
        });

        btnGuardarubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerUbicacion();
            }
        });

        btnVerPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buscarPuntos_Servicio("http://www.boxwakanda.site/servicios/buscar_puntos_usuario.php");
                    }
                },3000);

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }



    public void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (marcador != null)marcador.remove();

        marcador = mMap.addMarker(new MarkerOptions()
                        .position(coordenadas)
                        .title(getResources().getString(R.string.ubicacion_actual))
                //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)
        );
        mMap.animateCamera(miUbicacion);
    }

    /*private boolean checkLocation() {//TODO
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }*/

    private void buscarPuntos_Servicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    listaPuntos=new ArrayList<Punto>();
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("puntos");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject punto= array.getJSONObject(i);
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

                } catch (JSONException e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }

                agregarPuntosGuardados();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MapsActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
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


    private void agregarPuntosGuardados(){
        LatLng coordenadas;
        String titulomarcador="";

        if(listaPuntos.size()>0){
            for(int i=0;i<listaPuntos.size();i++) {
                coordenadas = new LatLng(listaPuntos.get(i).getLatitud(), listaPuntos.get(i).getLongitud());
                titulomarcador = listaPuntos.get(i).getTitulo();

                marcador = mMap.addMarker(new MarkerOptions()
                        .position(coordenadas)
                        .title(titulomarcador)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorpunto))
                );
            }
        }
    }

    public void ObtenerUbicacion() {
        String latitud="";
        String longitud="";


        //Obtener direccion
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //Transformar direccion en coordenadas
        if(location != null){
            latitud=String.valueOf(location.getLatitude());
            longitud=String.valueOf(location.getLongitude());
        }

        ArrayList<String> coordenadas = new ArrayList<>();
        coordenadas.add(latitud);
        coordenadas.add(longitud);

        Intent i = new Intent(getApplicationContext(),NuevopuntoActivity.class);
        i.putStringArrayListExtra("coordenadas",coordenadas);
        i.putExtra("usuario",u);
        startActivity(i);

    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcador(lat, lng);
        }
    }


    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location loc2= location;
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,1000,0,locListener);
    }

    LocationListener locListener= new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(getApplicationContext(),R.string.ubicacion_desactivada,Toast.LENGTH_LONG).show();
        }
    };
}
