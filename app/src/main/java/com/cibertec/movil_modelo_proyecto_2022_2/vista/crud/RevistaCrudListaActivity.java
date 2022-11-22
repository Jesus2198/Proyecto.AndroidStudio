package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.RevistaAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Editorial;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceRevista;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RevistaCrudListaActivity extends NewAppCompatActivity {

    ListView lstRevista;
    ArrayList<Revista> data = new ArrayList<Revista>();
    RevistaAdapter adaptador;
    Button btnFiltrar, btnRegistrar, btnEliminar;
    EditText txtFiltro;

    ServiceRevista serviceRevista;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revista_crud_lista);

        txtFiltro = findViewById(R.id.idCrudRevistaTxtFiltrar);
        btnFiltrar = findViewById(R.id.idCrudRevistaBtnFiltrar);
        btnRegistrar = findViewById(R.id.idCrudRevistaBtnRegistrar);
        btnEliminar = findViewById(R.id.idCrudRevistaFrmEliminar);

        lstRevista = findViewById(R.id.idCrudRevistaListView);
        adaptador = new RevistaAdapter(this, R.layout.activity_revista_crud_item, data);
        lstRevista.setAdapter(adaptador);

        serviceRevista = ConnectionRest.getConnection().create(ServiceRevista.class);

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtro = txtFiltro.getText().toString().trim();
                listaRevistaPorNombre(filtro);
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RevistaCrudListaActivity.this, RevistaCrudFormularioActivity.class);
                intent.putExtra("var_tipo", "REGISTRAR");
                startActivity(intent);
            }
        });

        lstRevista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Revista objRevista = data.get(i);

                Intent intent = new Intent(RevistaCrudListaActivity.this, RevistaCrudFormularioActivity.class);
                intent.putExtra("var_tipo", "ACTUALIZAR");
                intent.putExtra("var_item", objRevista);
                startActivity(intent);
            }
        });

        listaRevistaPorNombre("");
    }

    public void listaRevistaPorNombre(String filtro){
        Call<List<Revista>> call = serviceRevista.listaRevistaPorNombre(filtro);
        call.enqueue(new Callback<List<Revista>>() {
            @Override
            public void onResponse(Call<List<Revista>> call, Response<List<Revista>> response) {
                if (response.isSuccessful()){
                    List<Revista> lstSalida = response.body();
                    data.clear();
                    data.addAll(lstSalida);
                    adaptador.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Revista>> call, Throwable t) {
                mensajeAlert(t.getMessage());
                t.fillInStackTrace();
            }
        });
    }


}