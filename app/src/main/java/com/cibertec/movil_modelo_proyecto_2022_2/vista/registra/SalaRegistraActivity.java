package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;

import static com.cibertec.movil_modelo_proyecto_2022_2.R.id.spnRegSede;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
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


public class SalaRegistraActivity extends NewAppCompatActivity {

    Spinner spnSede;
    ArrayAdapter<String> adaptador;
    ArrayList<String> sedes = new ArrayList<String>();

    //Servicio
    ServiceSala serviceSala;
    ServiceSede serviceSede;

    EditText txtNum, txtPiso, txtNumAlum, txtRec, txtFecReg, txtEst;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_registra);

        txtNum = findViewById(R.id.txtRegSalaNumero);
        txtPiso = findViewById(R.id.txtRegSalaPiso);
        txtNumAlum = findViewById(R.id.txtRegSalaNumAlumnos);
        txtRec = findViewById(R.id.txtRegSalaRecursos);
        btnRegistrar = findViewById(R.id.btnRegSalaEnviar);


        serviceSede = ConnectionRest.getConnection().create(ServiceSede.class);
        serviceSala = ConnectionRest.getConnection().create(ServiceSala.class);

        //Para el adapatador
        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sedes);
        spnSede = findViewById(spnRegSede);
        spnSede.setAdapter(adaptador);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = txtNum.getText().toString();
                String piso = txtPiso.getText().toString();
                String numalum = txtNumAlum.getText().toString();
                String rec = txtRec.getText().toString();
                int indSede = spnSede.getSelectedItemPosition();

                if (!num.matches(ValidacionUtil.NUMERO)){
                    mensajeToast("El Numero es de 3 a  30 caracteres");
                } else if (!piso.matches(ValidacionUtil.PISO)) {
                    mensajeToast("El Piso es 1 a 2 digitos");
                } else if (!numalum.matches(ValidacionUtil.NUMALUMNOS)) {
                    mensajeToast("El Numero de Alumnos son de 1 a 2 digitos ");
                } else if (!rec.matches(ValidacionUtil.RECURSOS)) {
                    mensajeToast("Los Recursos son de 3 a  30 caracteres");
                } else{
                    String sede = spnSede.getSelectedItem().toString();
                    String idSede = sede.split(":")[0];

                    Sede objSede = new Sede();
                    objSede.setIdSede(Integer.parseInt(idSede));

                    Sala objSala = new Sala();
                    objSala.setNumero(num);
                    objSala.setPiso(Integer.parseInt(piso));
                    objSala.setNumAlumnos(Integer.parseInt(numalum));
                    objSala.setRecursos(rec);
                    objSala.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objSala.setEstado(1);
                    objSala.setSede(objSede);

                    registrar(objSala);


                }
            }
        });

        cargaSede();
    }

    public void registrar(Sala obj){
        Call<Sala> call = serviceSala.insertaSala(obj);
        call.enqueue(new Callback<Sala>() {
            @Override
            public void onResponse(Call<Sala> call, Response<Sala> response) {
                Sala objSala = response.body();
                mensajeAlert("REGISTRO EXITOSO!!"
                        +"\n Se ha registrado la Sala con el Id:"
                        +"\n ID >>> " + objSala.getIdSala()
                        +"\n Numero >>> " + objSala.getNumero()
                        +"\n Piso >>> " + objSala.getPiso()
                        +"\n Numeros de Alumnos >>> " + objSala.getNumAlumnos()
                        +"\n Recursos >>> " + objSala.getRecursos()
                        +"\n Fecha de Registro >>> " + objSala.getFechaRegistro()
                        +"\n Estado >>> " + objSala.getEstado());


            }

            @Override
            public void onFailure(Call<Sala> call, Throwable t) {
                mensajeToast("Error al acceder al Servicio Rest >>> ");
            }
        });


    }

    public void cargaSede(){
        Call<List<Sede>> call = serviceSede.listaSede();
        call.enqueue(new Callback<List<Sede>>() {
            @Override
            public void onResponse(Call<List<Sede>> call, Response<List<Sede>>response) {
                if (response.isSuccessful()){
                    List<Sede> lstSedes =  response.body();
                    for(Sede obj: lstSedes){
                        sedes.add(obj.getIdSede() +":"+ obj.getNombre());
                    }
                    adaptador.notifyDataSetChanged();
                }else{
                    mensajeToast("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Sede>> call, Throwable t) {
                mensajeToast("Error al acceder al Servicio Rest >>> " + t.getMessage());
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