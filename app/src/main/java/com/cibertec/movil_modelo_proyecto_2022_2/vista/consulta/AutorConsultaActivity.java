package com.cibertec.movil_modelo_proyecto_2022_2.vista.consulta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.AutorAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceAutor;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class    AutorConsultaActivity extends NewAppCompatActivity {

    ListView lstAutor;
    ArrayList<Autor> data = new ArrayList<>();
    AutorAdapter adapter;
    Button btnFiltrar;

    ServiceAutor serviceAutor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autor_consulta);

        btnFiltrar=findViewById(R.id.idConsAutorFiltrar);

        lstAutor=findViewById(R.id.idConsAutorListView);
        adapter=new AutorAdapter(this,R.layout.activity_autor_consulta_item,data);
        lstAutor.setAdapter(adapter);

        serviceAutor= ConnectionRest.getConnection().create(ServiceAutor.class);


        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista();
            }
        });


    }

    private void lista(){
        Call<List<Autor>> call=serviceAutor.listaAutor();
        call.enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(Call<List<Autor>> call, Response<List<Autor>> response) {
                if(response.isSuccessful()){
                List<Autor> lstSalida=response.body();
                data.clear();
                data.addAll(lstSalida);
                adapter.notifyDataSetChanged();}
            }

            @Override
            public void onFailure(Call<List<Autor>> call, Throwable t) {

            }
        });
    }


}