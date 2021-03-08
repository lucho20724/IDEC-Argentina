package com.example.idecargentina.Utilidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idecargentina.Entidades.Lista;
import com.example.idecargentina.R;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<Lista> lst;

    public CustomAdapter(Context context, List<Lista> lst) {
        this.context = context;
        this.lst = lst;
    }

    @Override
    public int getCount( ) { return lst.size();}

    @Override
    public Object getItem(int position) {return position;}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageViewLista;
        TextView textViewNombre;
        TextView textViewNombre2;

        Lista l= lst.get(position);

        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_personalizado,null);

            imageViewLista= convertView.findViewById(R.id.imagenid);
            textViewNombre= convertView.findViewById(R.id.textViewNombre);
            textViewNombre2= convertView.findViewById(R.id.textViewNombre2);


            imageViewLista.setImageResource(l.imagen);
            textViewNombre.setText(l.nombre);
            textViewNombre2.setText(l.nombre2);
        }
        return convertView ;

    }
}
