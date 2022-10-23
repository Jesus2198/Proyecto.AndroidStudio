package com.cibertec.movil_modelo_proyecto_2022_2.vista.consulta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.SalaAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Sala;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceSala;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalaConsultaActivity extends NewAppCompatActivity {

    //ListView
    ListView lstSala;
    ArrayList<Sala> data = new ArrayList<Sala>();
    SalaAdapter adaptador;

    //Boton
    Button btnFiltrar;

    //Conexion al Servicio REST
    ServiceSala serviceSala;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sala_consulta);

        btnFiltrar = findViewById(R.id.idConsSalaFiltrar);

        lstSala = findViewById(R.id.idConsSalaListView);
        adaptador = new SalaAdapter(this, R.layout.activity_alumno_consulta_item,data);
        lstSala.setAdapter(adaptador);

        serviceSala = ConnectionRest.getConnection().create(ServiceSala.class);

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listar();
            }
        });
    }

    public  void listar(){
        Call<List<Sala>> call = serviceSala.listaSala();
        call.enqueue(new Callback<List<Sala>>() {
            @Override
            public void onResponse(Call<List<Sala>> call, Response<List<Sala>> response) {
                if (response.isSuccessful()){
                    List<Sala> lstSalida = response.body();
                    data.clear();
                    data.addAll(lstSalida);
                    adaptador.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Sala>> call, Throwable t) {

            }
        });
    }
}