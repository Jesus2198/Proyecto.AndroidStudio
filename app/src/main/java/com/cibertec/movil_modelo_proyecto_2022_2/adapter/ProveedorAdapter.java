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
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Proveedor;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Pais;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ProveedorAdapter extends ArrayAdapter<Proveedor>  {

    private Context context;
    private List<Proveedor> lista;

    public ProveedorAdapter(@NonNull Context context, int resource, @NonNull List<Proveedor> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_proveedor_consulta_item, parent, false);

        Proveedor objProveedor = lista.get(position);


        TextView txtID = row.findViewById(R.id.idProveedorItemId);
        txtID.setText(String.valueOf(objProveedor.getIdProveedor()));

        TextView  txtRazonSocial = row.findViewById(R.id.idProveedorItemRazonSocial);
        txtRazonSocial.setText(String.valueOf(objProveedor.getRazonsocial()));

        TextView  txtDireccion = row.findViewById(R.id.idProveedorDireccion);
        txtDireccion.setText(String.valueOf(objProveedor.getDireccion()));


        TextView  txtPais = row.findViewById(R.id.idConsultaItemPais);
        txtPais.setText(String.valueOf(objProveedor.getRuc()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ruta ;
                    if (objProveedor.getPais().getIdPais() == 1 ){
                        ruta = "https://i.postimg.cc/859FDMft/Afganistan.png";
                    }else if (objProveedor.getPais().getIdPais() == 2){
                        ruta = "https://i.postimg.cc/sgmvxtZB/Islas-Gland.jpg";
                    }else if (objProveedor.getPais().getIdPais() == 3){
                        ruta = "https://i.postimg.cc/pLzmvmPB/Albania.png";
                    }else if (objProveedor.getPais().getIdPais() == 4){
                        ruta = "https://i.postimg.cc/zvhyfvn5/Alemania.webp";
                    }else if (objProveedor.getPais().getIdPais() == 5){
                        ruta = "https://i.postimg.cc/fWStC21F/andorra.png";
                    }else if (objProveedor.getPais().getIdPais() == 6){
                        ruta = "https://i.postimg.cc/02tvhPgy/angola.png";
                    }else if (objProveedor.getPais().getIdPais() == 7){
                        ruta = "https://i.postimg.cc/jjjC7ZzM/Anguilla.png";
                    }else if (objProveedor.getPais().getIdPais() == 8){
                        ruta = "https://i.postimg.cc/XvDqcdL4/antartida.webp";
                    }else if (objProveedor.getPais().getIdPais() == 9){
                        ruta = "https://i.postimg.cc/hP9JWJD1/antigua-y-barbuda.png";
                    }else if (objProveedor.getPais().getIdPais() == 10){
                        ruta = "https://i.postimg.cc/sDfyn1NL/Antillas-holandesas.png";
                    }else if (objProveedor.getPais().getIdPais() == 11){
                        ruta = "https://i.postimg.cc/pXN96QHd/arabia-saudita.png";
                    }else{
                        ruta = "https://i.postimg.cc/Y0420psp/Imagen-no-disponible.png";
                    }
                    URL rutaImagen  = new URL(ruta);
                    InputStream is = new BufferedInputStream(rutaImagen.openStream());
                    Bitmap b = BitmapFactory.decodeStream(is);
                    ImageView vista = row.findViewById(R.id.idProveedorItemImagen);
                    vista.setImageBitmap(b);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


        return row;
    }

}
