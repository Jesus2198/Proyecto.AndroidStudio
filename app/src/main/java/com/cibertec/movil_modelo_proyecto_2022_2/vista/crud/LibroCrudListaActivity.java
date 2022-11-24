package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.LibroAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Libro;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceLibro;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LibroCrudListaActivity extends NewAppCompatActivity {

    ListView lstLibro;
    ArrayList<Libro> data = new ArrayList<Libro>();
    LibroAdapter adapter;
    Button btnFiltrar,btnRegistrar;
    EditText txtFiltrar;

    ServiceLibro serviceLibro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro_crud_lista);

        btnFiltrar=findViewById(R.id.idCrudLibroBtnFiltrar);
        btnRegistrar=findViewById(R.id.idCrudLibroBtnRegistrar);
        txtFiltrar=findViewById(R.id.idCrudLibroTxtFiltrar);

        lstLibro=findViewById(R.id.idCrudLibroListView);
        adapter= new LibroAdapter(this,R.layout.activity_libro_crud_item,data);
        lstLibro.setAdapter(adapter);

        serviceLibro= ConnectionRest.getConnection().create(ServiceLibro.class);

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtro=txtFiltrar.getText().toString();
                filtrarPorNombre(filtro);
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LibroCrudListaActivity.this,LibroCrudFormularioActivity.class);
                intent.putExtra("var_tipo", "REGISTRAR");
                startActivity(intent);

            }
        });

        lstLibro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent= new Intent(LibroCrudListaActivity.this,LibroCrudFormularioActivity.class);
                intent.putExtra("var_tipo","ACTUALIZAR");

                Libro obj=data.get(i);
                intent.putExtra("var_item",obj);

                startActivity(intent);
            }
        });

        filtrarPorNombre("");
    }
















    public void filtrarPorNombre(String filtro){
        Call<List<Libro>> call=serviceLibro.listaLibroPorTitulo(filtro);
        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                if(response.isSuccessful()){
                    List<Libro> salida=response.body();
                    data.clear();
                    data.addAll(salida);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {

            }
        });

    }





}