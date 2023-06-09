package com.cibertec.movil_modelo_proyecto_2022_2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class SalaAdapter extends ArrayAdapter<Sala>  {

    private Context context;
    private List<Sala> lista;

    public SalaAdapter(@NonNull Context context, int resource, @NonNull List<Sala> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_sala_consulta_item, parent, false);

        Sala objSala = lista.get(position);

        TextView txtID = row.findViewById(R.id.idConsSalaItemID);
        txtID.setText(String.valueOf(objSala.getIdSala()));

        TextView txtNumero = row.findViewById(R.id.idConsSalaItemNumero);
        txtNumero.setText(objSala.getNumero());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ruta;
                    if (objSala.getIdSala() == 1){
                        ruta = "https://i.postimg.cc/qqq28wHW/1.png";
                    } else if (objSala.getIdSala() == 2){
                        ruta = "https://i.postimg.cc/c4KQCFmn/2.png";
                    } else if (objSala.getIdSala() == 3){
                        ruta = "https://i.postimg.cc/5yrLpzxy/3.png";
                    } else {
                        ruta = "https://i.postimg.cc/PfVWdgmH/4.png";
                    }
                    URL rutaImagen = new URL(ruta);
                    InputStream is = new BufferedInputStream(rutaImagen.openStream());
                    Bitmap b = BitmapFactory.decodeStream(is);
                    ImageView vista = row.findViewById(R.id.idConsSalaItemImagen);
                    vista.setImageBitmap(b);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        return row;
    }

}
