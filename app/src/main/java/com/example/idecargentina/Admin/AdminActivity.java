package com.example.idecargentina.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.idecargentina.ActivitiesCommon.EditarusuarioActivity;
import com.example.idecargentina.ActivitiesCommon.MainActivity;
import com.example.idecargentina.Entidades.Usuario;
import com.example.idecargentina.R;
import com.example.idecargentina.User.UserActivity;
import com.google.gson.Gson;

public class AdminActivity extends AppCompatActivity {
    TextView txt_saludo;
    Usuario u;
    Intent iglobal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        txt_saludo=(TextView)findViewById(R.id.lblSaludo);
        u = (Usuario)getIntent().getSerializableExtra("usuario");
        txt_saludo.setText(getResources().getString(R.string.admin_saludo)+" "+ u.getNombre()+"!");

        guardarPreferencias(u);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }


    public void onClick(View v){
        Intent i=null;
        iglobal=null;
        switch (v.getId()){
            case R.id.btnSalir_Admin:
            AlertDialog.Builder alerta = new AlertDialog.Builder(AdminActivity.this);
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
            case R.id.btnIConfigpersonal_Admin:
                i = new Intent(this, EditarusuarioActivity.class);
                i.putExtra("usuario",u);
                startActivity(i);
                break;
            case R.id.btnInfluencer_Admin:
                i = new Intent(this, InfluenceradminActivity.class);
                i.putExtra("usuario",u);
                startActivity(i);
                break;
            case R.id.btnAdminColportores_Admin:
                i = new Intent(this, ColportoresadminActivity.class);
                i.putExtra("usuario",u);
                startActivity(i);
                break;
            case R.id.btnAdminPasaporte_Admin:
                i = new Intent(this, PasaporteadminActivity.class);
                i.putExtra("usuario",u);
                startActivity(i);
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
