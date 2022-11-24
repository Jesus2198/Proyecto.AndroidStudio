package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import static com.cibertec.movil_modelo_proyecto_2022_2.R.id.idCrudSalaFrmPiso;
import static com.cibertec.movil_modelo_proyecto_2022_2.R.id.spnRegSede;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Sala;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Sede;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceSala;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceSede;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalaCrudFormularioActivity extends NewAppCompatActivity {

    Spinner spnSede;
    ArrayAdapter<String> adaptador;
    ArrayList<String> sedes = new ArrayList<String>();

    //Servicio
    ServiceSala serviceSala;
    ServiceSede serviceSede;

    //Componentes
    EditText txtNum, txtPiso, txtNumAlum, txtRec, txtFecReg, txtEst;
    Button btnRegistrar, btnRegresar;
    TextView txtTitulo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_crud_formulario);

        txtTitulo = findViewById(R.id.idCrudSalaFrmTitulo);
        txtNum = findViewById(R.id.idCrudSalaFrmNumero);
        txtPiso = findViewById(R.id.idCrudSalaFrmPiso);
        txtNumAlum = findViewById(R.id.idCrudSalaFrmNumAlumnos);
        txtRec = findViewById(R.id.idCrudSalaFrmRecursos);
        btnRegistrar = findViewById(R.id.idCrudSalaFrmEnviar);
        btnRegresar = findViewById(R.id.idCrudSalaFrmRegresar);


        serviceSede = ConnectionRest.getConnection().create(ServiceSede.class);
        serviceSala = ConnectionRest.getConnection().create(ServiceSala.class);

        //Para el adapatador
        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sedes);
        spnSede = findViewById(R.id.idCrudSalaFrmSede);
        spnSede.setAdapter(adaptador);




        Bundle extras = getIntent().getExtras();
        String tipo = extras.getString("var_tipo");
        if(tipo.equals("REGISTRAR")){
            txtTitulo.setText("Mantenimiento Sala - REGISTRA");
            btnRegistrar.setText("REGISTRA");
        }else if (tipo.equals("ACTUALIZAR")){
            txtTitulo.setText("Mantenimiento Sala - ACTUALIZA");
            btnRegistrar.setText("ACTUALIZA");

            Sala objSala = (Sala)extras.get("var_item");
            txtNum.setText(objSala.getNumero());
            txtPiso.setText(objSala.getPiso());
            txtNumAlum.setText(objSala.getNumAlumnos());
            txtRec.setText(objSala.getRecursos());


        }

        cargaSede();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = txtNum.getText().toString();
                String piso = txtPiso.getText().toString();
                String numalum = txtNumAlum.getText().toString();
                String rec = txtRec.getText().toString();
                int indSede = spnSede.getSelectedItemPosition();
                if (!num.matches(ValidacionUtil.NUMERO)){
                    mensajeToastLong("El Numero es de 3 a  30 caracteres");
                } else if (!piso.matches(ValidacionUtil.PISO)) {
                    mensajeToastLong("El Piso es 1 a 2 digitos");
                } else if (!numalum.matches(ValidacionUtil.NUMALUMNOS)) {
                    mensajeToastLong("El Numero de Alumnos son de 1 a 2 digitos ");
                } else if (!rec.matches(ValidacionUtil.RECURSOS)) {
                    mensajeToastLong("Los Recursos son de 3 a  30 caracteres");
                } else{
                    String sede = spnSede.getSelectedItem().toString();
                    String idSede = sede.split(":")[0];

                    Sede objSede = new Sede();
                    objSede.setIdSede(Integer.parseInt(idSede));

                    Sala objSala = new Sala();
                    objSala.setNumero(num);
                    objSala.setPiso(piso);
                    objSala.setNumAlumnos(numalum);
                    objSala.setRecursos(rec);
                    objSala.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objSala.setEstado(1);
                    objSala.setSede(objSede);

                    Bundle extras = getIntent().getExtras();
                    String tipo = extras.getString("var_tipo");
                    Sala obj = (Sala)extras.get("var_item");
                    if (tipo.equals("REGISTRAR")){
                        registr(objSala);
                    }else if (tipo.equals("ACTUALIZAR")){
                        objSala.setIdSala(obj.getIdSala());
                        actualiza(objSala);
                    }


                }
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SalaCrudFormularioActivity.this, SalaCrudListaActivity.class);
                startActivity(intent);
            }
        });



    }

    public void cargaSede(){
        Call<List<Sede>> call = serviceSede.listaSede();
        call.enqueue(new Callback<List<Sede>>() {
            @Override
            public void onResponse(Call<List<Sede>> call, Response<List<Sede>> response) {
                if (response.isSuccessful()){
                    List<Sede> lstSedes =  response.body();
                    for(Sede obj: lstSedes){
                        sedes.add(obj.getIdSede() +":"+ obj.getNombre());
                    }
                    adaptador.notifyDataSetChanged();

                    Bundle extras = getIntent().getExtras();
                    String tipo = extras.getString("var_tipo");
                    Sala obj = (Sala) extras.get("var_item");

                    if (tipo.equals("ACTUALIZAR")){

                        int idSede = obj.getSede().getIdSede();
                        String nombreSede = obj.getSede().getNombre();

                        String itemSede = idSede+":"+nombreSede;
                        int posSeleccionada = -1;
                        for(int i=0; i< sedes.size(); i++){
                            if (sedes.get(i).equals(itemSede)){
                                posSeleccionada = i;
                                break;
                            }
                        }
                        spnSede.setSelection(posSeleccionada);
                    }



                }else{
                    mensajeToastLong("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Sede>> call, Throwable t) {
                mensajeToastLong("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }


    public void registr(Sala obj){
        Call<Sala> call = serviceSala.insertaSala(obj);
        call.enqueue(new Callback<Sala>() {
            @Override
            public void onResponse(Call<Sala> call, Response<Sala> response) {
                if (response.isSuccessful()) {
                    Sala objSala = response.body();
                    mensajeAlert("REGISTRO EXITOSO!!"
                            + "\n Se ha registrado la Sala con el Id:"
                            + "\n ID >>> " + objSala.getIdSala()
                            + "\n Numero >>> " + objSala.getNumero()
                            + "\n Piso >>> " + objSala.getPiso()
                            + "\n Numeros de Alumnos >>> " + objSala.getNumAlumnos()
                            + "\n Recursos >>> " + objSala.getRecursos()
                            + "\n Fecha de Registro >>> " + objSala.getFechaRegistro()
                            + "\n Estado >>> " + objSala.getEstado());


                }
            }

            @Override
            public void onFailure(Call<Sala> call, Throwable t) {
                mensajeToastLong("Error al acceder al Servicio Rest >>> ");
            }
        });


    }

    public void actualiza(Sala obj){
        Call<Sala> call = serviceSala.actualizaSala(obj);
        call.enqueue(new Callback<Sala>() {
            @Override
            public void onResponse(Call<Sala> call, Response<Sala> response) {
                if (response.isSuccessful()) {
                    Sala objSala = response.body();
                    mensajeAlert("ACTUALIZACIÓN EXITOSA!!"
                            + "\n Se ha actualizado la Sala con el Id:"
                            + "\n ID >>> " + objSala.getIdSala()
                            + "\n Numero >>> " + objSala.getNumero()
                            + "\n Piso >>> " + objSala.getPiso()
                            + "\n Numeros de Alumnos >>> " + objSala.getNumAlumnos()
                            + "\n Recursos >>> " + objSala.getRecursos()
                            + "\n Fecha de Registro >>> " + objSala.getFechaRegistro()
                            + "\n Estado >>> " + objSala.getEstado());


                }
            }

            @Override
            public void onFailure(Call<Sala> call, Throwable t) {
                mensajeToastLong("Error al acceder al Servicio Rest >>> ");
            }
        });


    }
}