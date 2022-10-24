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
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class AutorAdapter extends ArrayAdapter<Autor>  {

    private Context context;
    private List<Autor> lista;

    public AutorAdapter(@NonNull Context context, int resource, @NonNull List<Autor> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_autor_consulta_item, parent, false);

        Autor objAutor = lista.get(position);

        TextView txtID = row.findViewById(R.id.idAutorItemId);
        txtID.setText(String.valueOf(objAutor.getIdAutor()));

        TextView  txtNombre = row.findViewById(R.id.idAutorItemNombre);
        txtNombre.setText(String.valueOf(objAutor.getNombres()));

        TextView  txtApellido = row.findViewById(R.id.idAutorApellido);
        txtApellido.setText(String.valueOf(objAutor.getApellidos()));


        TextView  txtGrado = row.findViewById(R.id.idConsultaItemGrado);
        txtGrado.setText(String.valueOf(objAutor.getGrado().getDescripcion()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ruta ;
                    if (objAutor.getGrado().getIdGrado() == 1 ){
                        ruta = "https://i.postimg.cc/sg0SBwxM/tecnico.jpg";
                    }else if (objAutor.getGrado().getIdGrado() == 2){
                        ruta = "https://i.postimg.cc/bv7tgrQ8/profesional.jpg";
                    }else if (objAutor.getGrado().getIdGrado() == 3){
                        ruta = "https://i.postimg.cc/N09XRm2p/magister.png";
                    }else if (objAutor.getGrado().getIdGrado() == 4){
                        ruta = "https://i.postimg.cc/C1bfQ0N8/doctor.jpg";
                    }else{
                        ruta = "https://i.postimg.cc/gcHBSk14/no-disponible.png";
                    }
                    URL rutaImagen  = new URL(ruta);
                    InputStream is = new BufferedInputStream(rutaImagen.openStream());
                    Bitmap b = BitmapFactory.decodeStream(is);
                    ImageView vista = row.findViewById(R.id.idAutorItemImagen);
                    vista.setImageBitmap(b);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


        return row;
    }
}
