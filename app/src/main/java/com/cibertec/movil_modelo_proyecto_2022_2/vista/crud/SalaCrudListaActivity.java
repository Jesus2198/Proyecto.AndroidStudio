package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.SalaAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.SalaCrudAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Sala;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceSala;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SalaCrudListaActivity extends NewAppCompatActivity {

    ListView lstSala;
    ArrayList<Sala> data = new ArrayList<Sala>();
    SalaCrudAdapter adaptador;
    Button btnFiltrar, btnRegistrar;
    EditText txtFiltro;

    ServiceSala serviceSala;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_crud_lista);

        txtFiltro = findViewById(R.id.idCrudSalaTxtFiltrar);
        btnFiltrar = findViewById(R.id.idCrudSalaBtnFiltrar);
        btnRegistrar = findViewById(R.id.idCrudSalaBtnRegistrar);

        lstSala = findViewById(R.id.idCrudSalaListView);
        adaptador = new SalaCrudAdapter(this, R.layout.activity_sala_crud_item,data);
        lstSala.setAdapter(adaptador);

        serviceSala = ConnectionRest.getConnection().create(ServiceSala.class);

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtro = txtFiltro.getText().toString().trim();
                    listaSalaPorNumero(filtro);
                }


        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SalaCrudListaActivity.this, SalaCrudFormularioActivity.class);
                intent.putExtra("var_tipo", "REGISTRAR");
                startActivity(intent);
            }
        });

        lstSala.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Sala objSala = data.get(i);
                Intent intent = new Intent(SalaCrudListaActivity.this, SalaCrudFormularioActivity.class);
                intent.putExtra("var_tipo", "ACTUALIZAR");
                intent.putExtra("var_item", objSala);
                startActivity(intent);
            }
        });

        listaSalaPorNumero("");


    }

    public void listaSalaPorNumero(String filtro){
        Call<List<Sala>> call = serviceSala.listaSalaPorNumero(filtro);
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
                mensajeAlert(t.getMessage());
                t.fillInStackTrace();

            }
        });
    }



}