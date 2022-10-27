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
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Libro;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class LibroAdapter extends ArrayAdapter<Libro>  {

    private Context context;
    private List<Libro> lista;

    public LibroAdapter(@NonNull Context context, int resource, @NonNull List<Libro> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_libro_consulta_item, parent, false);

        Libro objLibro = lista.get(position);

        TextView txtID = row.findViewById(R.id.idLibroItemId);
        txtID.setText(String.valueOf(objLibro.getIdLibro()));

        TextView  txtTitulo = row.findViewById(R.id.idLibroItemTitulo);
        txtTitulo.setText(String.valueOf(objLibro.getTitulo()));

        TextView  txtSerie = row.findViewById(R.id.idLibroSerie);
        txtSerie.setText(String.valueOf(objLibro.getSerie()));


        TextView  txtCategoria = row.findViewById(R.id.idConsultaItemCategoria);
        txtCategoria.setText(String.valueOf(objLibro.getCategoria().getDescripcion()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ruta ;
                    if (objLibro.getCategoria().getIdCategoria() == 1 ){
                        ruta = "https://i.postimg.cc/pXKZKRNT/libro1.jpg";
                    }else if (objLibro.getCategoria().getIdCategoria() == 2){
                        ruta = "https://i.postimg.cc/bvVHqvn6/libro2.png";
                    }else if (objLibro.getCategoria().getIdCategoria() == 3){
                        ruta = "https://i.postimg.cc/4dr6hFBV/libro3.png";
                    }else if (objLibro.getCategoria().getIdCategoria() == 4){
                        ruta = "https://i.postimg.cc/brkQ0HBM/libro4.png";
                    }else{
                        ruta = "https://i.postimg.cc/DyQr86s4/libro5.jpg";
                    }
                    URL rutaImagen  = new URL(ruta);
                    InputStream is = new BufferedInputStream(rutaImagen.openStream());
                    Bitmap b = BitmapFactory.decodeStream(is);
                    ImageView vista = row.findViewById(R.id.idLibroItemImagen);
                    vista.setImageBitmap(b);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        return row;
    }

}
