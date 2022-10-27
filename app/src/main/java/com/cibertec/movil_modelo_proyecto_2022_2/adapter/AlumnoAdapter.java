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
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Alumno;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class AlumnoAdapter extends ArrayAdapter<Alumno>  {

    private Context context;
    private List<Alumno> lista;

    public AlumnoAdapter(@NonNull Context context, int resource, @NonNull List<Alumno> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_alumno_consulta_item, parent, false);

        Alumno objAlumno = lista.get(position);

        TextView txtID = row.findViewById(R.id.idAlumnoItemId);
        txtID.setText(String.valueOf(objAlumno.getIdAlumno()));

        TextView  txtNombre = row.findViewById(R.id.idAlumnoItemNombre);
        txtNombre.setText(String.valueOf(objAlumno.getNombres()));

        TextView  txtApellido = row.findViewById(R.id.idAlumnoApellido);
        txtApellido.setText(String.valueOf(objAlumno.getApellidos()));


        TextView  txtPais = row.findViewById(R.id.idConsultaItemPais);
        txtPais.setText(String.valueOf(objAlumno.getPais().getNombre()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ruta ;
                    if (objAlumno.getPais().getIdPais() == 1 ){
                        ruta = "https://i.postimg.cc/JzFNdtVn/afganistan.png";
                    }else if (objAlumno.getPais().getIdPais() == 2){
                        ruta = "https://i.postimg.cc/1thpsTz1/islas-agland.png";
                    }else if (objAlumno.getPais().getIdPais() == 3){
                        ruta = "https://i.postimg.cc/L5vfk83m/albania.png";
                    }else if (objAlumno.getPais().getIdPais() == 4){
                        ruta = "https://i.postimg.cc/HL7wGHdt/alemania.webp";
                    }else if (objAlumno.getPais().getIdPais() == 5) {
                        ruta = "https://i.postimg.cc/ydm0dPJg/andorra.png";
                    }else if (objAlumno.getPais().getIdPais() == 173){
                        ruta = "https://i.postimg.cc/HnTX7y5S/peru.webp";
                    }else{
                        ruta = "https://i.postimg.cc/gcHBSk14/no-disponible.png";
                    }
                    URL rutaImagen  = new URL(ruta);
                    InputStream is = new BufferedInputStream(rutaImagen.openStream());
                    Bitmap b = BitmapFactory.decodeStream(is);
                    ImageView vista = row.findViewById(R.id.idAlumnoItemImagen);
                    vista.setImageBitmap(b);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


        return row;
    }
}
