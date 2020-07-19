package com.example.idecargentina.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idecargentina.ActivitiesCommon.EditarusuarioActivity;
import com.example.idecargentina.ActivitiesCommon.MainActivity;
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;
import com.example.idecargentina.Utilidades.Funciones;
import com.google.gson.Gson;

public class UserActivity extends AppCompatActivity {
    TextView txt_saludo;
    Usuario usuario;
    boolean modificado;
    Intent iglobal;
    Funciones fun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        modificado = getIntent().getBooleanExtra("mod",false);
        txt_saludo=(TextView)findViewById(R.id.lblSaludo);
        fun = new Funciones();

        Boolean internet=fun.comprobarConexion(getApplicationContext());

        if(modificado) usuario = (Usuario)getIntent().getSerializableExtra("usuariomod");
        else usuario = (Usuario)getIntent().getSerializableExtra("usuario");

        guardarPreferencias(usuario);
        txt_saludo.setText(getResources().getString(R.string.admin_saludo)+" "+ usuario.getNombre()+"!");
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

    public void onClick(View v){
        Intent i =null;
        iglobal=null;
        switch (v.getId()){
            case R.id.btnInfluencer_User:
                i= new Intent(this, InfluencerActivity.class);
                i.putExtra("usuario",usuario);
                startActivity(i);
                break;
            case R.id.btnIConfigpersonal_User:
                i=new Intent(this, EditarusuarioActivity.class);
                i.putExtra("usuario",usuario);
                startActivity(i);
                break;

            case R.id.btnGeocolportaje_User:
                i=new Intent(this, MapsActivity.class);
                i.putExtra("usuario",usuario);
                startActivity(i);
                break;

            case R.id.btnAdminColportores_Admin:
                Toast.makeText(getApplicationContext(),R.string.toast_proximamente,Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnPasaporte_User:
                i=new Intent(this,PasaporteuserActivity.class);
                i.putExtra("usuario",usuario);
                startActivity(i);
                break;

            case R.id.btnSalir_User:
                AlertDialog.Builder alerta = new AlertDialog.Builder(UserActivity.this);
                alerta.setMessage(R.string.toast_cerrarsesion)
                        .setTitle(R.string.atencion)
                        .setCancelable(false)
                        .setPositiveButton(R.string.registro_alert_si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                                preferences.edit().clear().commit();

                                iglobal = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(iglobal);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alerta.show();
                break;
        }
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
