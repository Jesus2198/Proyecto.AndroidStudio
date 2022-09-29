package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AlertDialog;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Libro;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Categoria;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceLibro;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceCategoria;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibroRegistraActivity extends NewAppCompatActivity {

    Spinner spnCategLibro;
    ArrayAdapter<String> adaptador;
    ArrayList<String> categorias = new ArrayList<>();

    ServiceLibro serviceLibro;
    ServiceCategoria serviceCategoria;

    EditText txtTitulo,txtAnLibro,txtSerie;
    Button btnRegLibro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_libro_registra);

        txtTitulo=findViewById(R.id.txtTitLibro);
        txtAnLibro=findViewById(R.id.txtAnLibro);
        txtSerie=findViewById(R.id.txtSerieLibro);

        btnRegLibro=findViewById(R.id.btnRegLibro);

        serviceLibro = ConnectionRest.getConnection().create(ServiceLibro.class);
        serviceCategoria=ConnectionRest.getConnection().create(ServiceCategoria.class);

        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.
                support_simple_spinner_dropdown_item, categorias);
        spnCategLibro=findViewById(R.id.spnCategLibro);
        spnCategLibro.setAdapter(adaptador);


        btnRegLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tit =txtTitulo.getText().toString();
                String ani=txtAnLibro.getText().toString();
                String ser=txtSerie.getText().toString();

                int indcategoria =spnCategLibro.getSelectedItemPosition();
                btnRegLibro=findViewById(R.id.btnRegLibro);

                if (!tit.matches(ValidacionUtil.TEXTO)){
                    mensajeToast("Título es de 3 a 30 carácteres");
                }else if (!ani.matches(ValidacionUtil.ANIO)){
                    mensajeToast("Año es de 4 carácteres");
                }else if (!ser.matches(ValidacionUtil.DNI)){
                    mensajeToast("Serie es de 8 carácteres");
                }else{

                    String categoria =spnCategLibro.getSelectedItem().toString();
                    String idCategoria= categoria.split(":")[0];

                    Categoria objCategoria = new Categoria();
                    objCategoria.setIdCategoria(Integer.parseInt(idCategoria));

                    Libro objLibro = new Libro();
                    objLibro.setTitulo(tit);
                    objLibro.setAnio(ani);
                    objLibro.setSerie(ser);
                    objLibro.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objLibro.setEstado(1);
                    objLibro.setCategoria(objCategoria);

                    registrarLibro(objLibro);
                }
            }

        });

        cargarCategoria();

    }

    public  void registrarLibro(Libro obj){
        Call<Libro> call= serviceLibro.insertarLibro(obj);
        call.enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                Libro objLibro= response.body();
                mensajeAlert("Se ha Registrado el Libro:"+ "\n ID   >>>" + objLibro.getIdLibro()+
                        "\n Titulo   >>> " + objLibro.getTitulo()+
                        "\n Año >>> " + objLibro.getAnio());
            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                mensajeToast("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }


    public void cargarCategoria(){
        Call<List<Categoria>> call = serviceCategoria.TodaLasCategorias();
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if(response.isSuccessful()){
                    List<Categoria> lstCategoria = response.body();
                    for (Categoria obj:lstCategoria){
                        categorias.add(obj.getIdCategoria()+":"+obj.getDescripcion());
                    }
                    adaptador.notifyDataSetChanged();
                }else{
                    mensajeToast("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                mensajeToast("Error al Acceder al Servicio Rest >>> " + t.getMessage());
            }
        });

    }

    public void mensajeAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }


    public void mensajeToast(String mensaje){
        Toast toast1 =  Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_LONG);
        toast1.show();
    }


}