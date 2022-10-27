package com.cibertec.movil_modelo_proyecto_2022_2.vista.consulta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.ProveedorAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Proveedor;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceProveedor;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProveedorConsultaActivity extends NewAppCompatActivity {

    ListView lstProveedor;
    ArrayList<Proveedor> data = new ArrayList<>();
    ProveedorAdapter adapter;
    Button btnFiltrar;

    ServiceProveedor serviceProveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor_consulta);

        btnFiltrar=findViewById(R.id.idConsProveedorFiltrar);

        lstProveedor=findViewById(R.id.idConsProveedorListView);
        adapter=new ProveedorAdapter(this,R.layout.activity_proveedor_consulta_item,data);
        lstProveedor.setAdapter(adapter);

        serviceProveedor= ConnectionRest.getConnection().create(ServiceProveedor.class);


        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista();
            }
        });

    }
    private void lista(){
        Call<List<Proveedor>> call=serviceProveedor.listaProveedor();
        call.enqueue(new Callback<List<Proveedor>>() {
            @Override
            public void onResponse(Call<List<Proveedor>> call, Response<List<Proveedor>> response) {
                if(response.isSuccessful()){
                    List<Proveedor> lstSalida=response.body();
                    data.clear();
                    data.addAll(lstSalida);
                    adapter.notifyDataSetChanged();}
            }

            @Override
            public void onFailure(Call<List<Proveedor>> call, Throwable t) {

            }
        });
    }



 }