package com.cibertec.movil_modelo_proyecto_2022_2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Sala;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class SalaCrudAdapter extends ArrayAdapter<Sala> {

    private Context context;
    private List<Sala> lista;

    public SalaCrudAdapter(@NonNull Context context, int resource, @NonNull List<Sala> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_sala_crud_item, parent, false);

        Sala obj = lista.get(position);

        TextView txtID = row.findViewById(R.id.idCrudSalaItemID);
        txtID.setText(String.valueOf(obj.getIdSala()));

        TextView txtNumero = row.findViewById(R.id.idCrudSalaItemNumero);
        txtNumero.setText(String.valueOf(obj.getNumero()));


        if (position%2 ==0){
            txtNumero.setBackgroundColor(Color.rgb(204, 255, 204));
            txtID.setBackgroundColor(Color.rgb(204, 255, 204));
        }



        return row;
    }

}
