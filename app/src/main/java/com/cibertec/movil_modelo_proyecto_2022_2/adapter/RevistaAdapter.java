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
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class RevistaAdapter extends ArrayAdapter<Revista>  {

    private Context context;
    private List<Revista> lista;

    public RevistaAdapter(@NonNull Context context, int resource, @NonNull List<Revista> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.activity_revista_consulta_item, parent, false);
        Revista objRevista = lista.get(position);

        TextView txtID = row.findViewById(R.id.idConsultaRevItemId);
        txtID.setText(String.valueOf(objRevista.getIdRevista()));

        TextView txtReviNombre = row.findViewById(R.id.idConsultaRevItemNombre);
        txtReviNombre.setText(objRevista.getNombre());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ruta ;
                    if (objRevista.getIdRevista() == 1){
                        ruta = "https://i.postimg.cc/WbcDv1Bk/gente.jpg";
                    }else if (objRevista.getIdRevista() == 2){
                        ruta = "https://i.postimg.cc/SQMX3Mv4/caretas.jpg";
                    }else if (objRevista.getIdRevista() == 3){
                        ruta = "https://i.postimg.cc/0NYsWC9B/etiquetanegra.jpg";
                    }else{
                        ruta = "https://i.postimg.cc/SRvF2TGJ/no-disponible.png";
                    }
                    URL rutaImagen  = new URL(ruta);
                    InputStream is = new BufferedInputStream(rutaImagen.openStream());
                    Bitmap b = BitmapFactory.decodeStream(is);
                    ImageView vista = row.findViewById(R.id.idConsultaRevImagen);
                    vista.setImageBitmap(b);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        return row;
    }
}
