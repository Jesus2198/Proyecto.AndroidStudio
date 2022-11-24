package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

public class LibroCrudFormularioActivity extends NewAppCompatActivity {

    Spinner spnCategoria;
    ArrayAdapter<String> adaptador;
    ArrayList<String> categorias = new ArrayList<String>();

    ServiceLibro serviceLibro;
    ServiceCategoria serviceCategoria;

    TextView txtTitulo;
    EditText txtNombre,txtAnio,txtSerie;
    Button btnRegistrar,btnRegresar;

    String tipo;

    Libro obj=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro_crud_formulario);

        txtTitulo=findViewById(R.id.idCrudLibroFrmTitulo);
        txtNombre=findViewById(R.id.idCrudLibroFrmNombre);
        txtAnio=findViewById(R.id.idCrudLibroFrmAnio);
        txtSerie=findViewById(R.id.idCrudLibroFrmSerie);

        btnRegistrar=findViewById(R.id.idCrudLibroFrmBtnRegistrar);
        btnRegresar=findViewById(R.id.idCrudLibroFrmBtnRegresar);

        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categorias);
        spnCategoria=findViewById(R.id.idCrudLibroFrmCategoria);
        spnCategoria.setAdapter(adaptador);

        serviceLibro= ConnectionRest.getConnection().create(ServiceLibro.class);
        serviceCategoria=ConnectionRest.getConnection().create(ServiceCategoria.class);

        ///Cargar Spiner Categoria
        cargarCategoria();

        Bundle extras=getIntent().getExtras();
        tipo=extras.getString("var_tipo");

        if(tipo.equals("REGISTRAR")){
            txtTitulo.setText("Mantenimiento Libro - REGISTRA");
            btnRegistrar.setText("REGISTRA");
        }else if(tipo.equals("ACTUALIZAR")){
            txtTitulo.setText("Mantenimiento Libro - ACTUALIZA");
            btnRegistrar.setText("ACTUALIZA");

            obj=(Libro) extras.get("var_item");

            txtNombre.setText(obj.getTitulo());
            txtAnio.setText(obj.getAnio());
            txtSerie.setText(obj.getSerie());
        }

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom=txtNombre.getText().toString();
                String ani=txtAnio.getText().toString();
                String ser=txtSerie.getText().toString();


                if(!nom.matches(ValidacionUtil.TEXTO)){
                    mensajeAlert("El Nombre es 2 a 20 caracteres");
                }else if(!ani.matches(ValidacionUtil.ANIO)){
                    mensajeAlert("El Año debe ser de 4 caracteres");
                }else if(!ser.matches(ValidacionUtil.DNI)){
                    mensajeAlert("La serie debe ser de 8 numeros");
                }else{

                    String categoria=spnCategoria.getSelectedItem().toString();
                    String idCategoria=categoria.split(":")[0];



                    Categoria objCategoria = new Categoria();
                    objCategoria.setIdCategoria(Integer.parseInt(idCategoria));


                    Libro objLibro= new Libro();
                    objLibro.setTitulo(nom);
                    objLibro.setAnio(ani);
                    objLibro.setSerie(ser);
                    objLibro.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objLibro.setEstado(1);
                    objLibro.setCategoria(objCategoria);



                    if(tipo.equals("REGISTRAR")){
                        registraLibro(objLibro);
                    }else if(tipo.equals("ACTUALIZAR")){
                        Libro obj=(Libro) extras.get("var_item");
                        objLibro.setIdLibro(obj.getIdLibro());
                        actualizaLibro(objLibro);
                    }

                }



            }


        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LibroCrudFormularioActivity.this, LibroCrudListaActivity.class);
                startActivity(intent);
            }
        });



    }

    public void registraLibro(Libro objLibro) {
        Call<Libro> call = serviceLibro.insertarLibro(objLibro);
        call.enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful()) {
                    Libro objSalida = response.body();
                    mensajeAlert("Se registro Libro " +
                            "\nID >> " + objSalida.getIdLibro() +
                            "\nNombre >> " + objSalida.getTitulo());
                }
            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                mensajeAlert("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }

    public void cargarCategoria(){
        Call<List<Categoria>> call = serviceCategoria.TodaLasCategorias();
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful()){
                    List<Categoria> lstCategoria =  response.body();
                    for(Categoria obj: lstCategoria){
                        categorias.add(obj.getIdCategoria() +":"+ obj.getDescripcion());
                    }
                    adaptador.notifyDataSetChanged();



                    if (tipo.equals("ACTUALIZAR")){

                        int idCategoria = obj.getCategoria().getIdCategoria();
                        String nombreCategoria = obj.getCategoria().getDescripcion();

                        String itemCategoria = idCategoria+":"+nombreCategoria;
                        int posSeleccionada = -1;
                        for(int i=0; i< categorias.size(); i++){
                            if (categorias.get(i).equals(itemCategoria)){
                                posSeleccionada = i;
                                break;
                            }
                        }
                        spnCategoria.setSelection(posSeleccionada);
                    }

                }else{
                    mensajeToastLong("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                mensajeToastLong("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }

    public void actualizaLibro(Libro obj){
        Call<Libro> call = serviceLibro.actualizarLibro(obj);
        call.enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful()){
                    Libro objSalida =   response.body();
                    mensajeAlert("Se actualizo Libro " +
                            "\nID >> " + objSalida.getIdLibro() +
                            "\nNombre libro >> " + objSalida.getTitulo() );
                }
            }
            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                mensajeAlert("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }




}