package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Modalidad;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceModalidad;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceRevista;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevistaRegistraActivity extends NewAppCompatActivity {

    Spinner spnModalidad;
    ArrayAdapter<String> adaptador;
    ArrayList<String> modalidades = new ArrayList<String>();

    ServiceRevista serviceRevista;
    ServiceModalidad serviceModalidad;

    EditText txtRevistaNombre, txtRevistaFrecuencia, txtRevistaFechaCre;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revista_registra);

        txtRevistaNombre = findViewById(R.id.txtRegRevNombre);
        txtRevistaFrecuencia = findViewById(R.id.txtRegRevFrecuencia);
        txtRevistaFechaCre = findViewById(R.id.txtRegRevFechaCre);
        btnRegistrar = findViewById(R.id.btnRegRevRegistrar);

        serviceModalidad = ConnectionRest.getConnection().create(ServiceModalidad.class);
        serviceRevista = ConnectionRest.getConnection().create(ServiceRevista.class);

        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, modalidades);
        spnModalidad = findViewById(R.id.spnRegRevModalidad);
        spnModalidad.setAdapter(adaptador);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = txtRevistaNombre.getText().toString();
                String fre = txtRevistaFrecuencia.getText().toString();
                String fec = txtRevistaFechaCre.getText().toString();
                int indModalidad = spnModalidad.getSelectedItemPosition();
                if (!nom.matches(ValidacionUtil.TEXTO)){
                    mensajeToast("El nombre de la revista debe ser de 2 a 20 caracteres");
                }
                else if (!fre.matches(ValidacionUtil.TEXTO)){
                    mensajeToast("La frecuencia debe ser de 2 a 20 caracteres");
                }
                else if (!fec.matches(ValidacionUtil.FECHA)){
                    mensajeToast("La fecha es YYYY-MM-DD");
                }
                else{
                    String modalidad = spnModalidad.getSelectedItem().toString();
                    String idModalidad = modalidad.split(":")[0];

                    Modalidad objModalidad = new Modalidad();
                    objModalidad.setIdModalidad(Integer.parseInt(idModalidad));

                    Revista objRevista = new Revista();
                    objRevista.setNombre(nom);
                    objRevista.setFrecuencia(fre);
                    objRevista.setFechaCreacion(fec);
                    objRevista.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objRevista.setEstado(1);
                    objRevista.setModalidad(objModalidad);
                    registrar(objRevista);
                }
            }
        });

        cargaModalidad();
    }
    public void registrar(Revista obj){
        Call<Revista> call = serviceRevista.insertaRevista(obj);
        call.enqueue(new Callback<Revista>() {
            @Override
            public void onResponse(Call<Revista> call, Response<Revista> response) {
                if (response.isSuccessful()){
                    Revista objRevista = response.body();
                    mensajeAlert("Se ha registrado la revista de ID >>> " + objRevista.getIdRevista());
                }
                else {
                    mensajeAlert(response.message() + " - " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Revista> call, Throwable t) {
                mensajeToast("Error al accede al Servicio Rest >>> " + t.getMessage());
            }
        });
    }
    public void cargaModalidad(){
        Call<List<Modalidad>> call = serviceModalidad.listaTodos();
        call.enqueue(new Callback<List<Modalidad>>() {
            @Override
            public void onResponse(Call<List<Modalidad>> call, Response<List<Modalidad>> response) {
                if (response.isSuccessful()) {
                    List<Modalidad> lstModalidades = response.body();
                    for (Modalidad obj : lstModalidades) {
                        modalidades.add(obj.getIdModalidad() + ":" + obj.getDescripcion());
                    }
                    adaptador.notifyDataSetChanged();
                } else {
                    mensajeToast("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Modalidad>> call, Throwable t) {
                    mensajeToast("Error al accede al Servicio Rest >>> " + t.getMessage());
            }
        });
    }
    public void mensajeToast(String mensaje){
        Toast toast1 =  Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_LONG);
        toast1.show();
    }
    public void mensajeAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

}