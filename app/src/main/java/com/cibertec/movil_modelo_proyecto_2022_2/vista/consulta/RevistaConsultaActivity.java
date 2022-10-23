package com.cibertec.movil_modelo_proyecto_2022_2.vista.consulta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.RevistaAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceRevista;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevistaConsultaActivity extends NewAppCompatActivity {

    ListView lstRevista;
    ArrayList<Revista> data = new ArrayList<Revista>();
    RevistaAdapter adaptador;

    Button btnFiltrar;

    ServiceRevista serviceRevista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revista_consulta);

        btnFiltrar = findViewById(R.id.idConsReviFiltrar);

        lstRevista = findViewById(R.id.idConsReviListView);
        adaptador = new RevistaAdapter(this, R.layout.activity_revista_consulta_item, data);
        lstRevista.setAdapter(adaptador);

        serviceRevista = ConnectionRest.getConnection().create(ServiceRevista.class);

        btnFiltrar.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                listar();
            }
        });
    }
    public void listar() {
        Call<List<Revista>> call = serviceRevista.listaRevista();
        call.enqueue(new Callback<List<Revista>>() {
            @Override
            public void onResponse(Call<List<Revista>> call, Response<List<Revista>> response) {
                List<Revista> lstSalida = response.body();
                data.clear();
                data.addAll(lstSalida);
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Revista>> call, Throwable t) {

            }
        });
    }
}