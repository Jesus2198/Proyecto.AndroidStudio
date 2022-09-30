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
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Alumno;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Pais;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceAlumno;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServicePais;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlumnoRegistraActivity extends NewAppCompatActivity {

    Spinner spnPais;
    ArrayAdapter<String> adaptador;
    ArrayList<String> paises = new ArrayList<>();

    //servicio
    ServiceAlumno serviceAlumno;
    ServicePais servicePais;

    EditText txtNombres,txtApellidos,txtTelefono,txtDni,txtCorreo,txtFechaNacimiento,txtFechaRegistro,txtEstado;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alumno_registra);

        txtNombres=findViewById(R.id.txtRegAlumnoNombres);
        txtApellidos=findViewById(R.id.txtRegAlumnoApellidos);
        txtTelefono=findViewById(R.id.txtRegAlumnoTelefono);
        txtDni=findViewById(R.id.txtRegAlumnoDni);
        txtCorreo=findViewById(R.id.txtRegAlumnoCorreo);
        txtFechaNacimiento=findViewById(R.id.txtRegAlumnoFechaNacimiento);
        txtEstado=findViewById(R.id.txtRegAlumnoEstado);

        btnRegistrar=findViewById(R.id.btnRegAlumno);

        serviceAlumno= ConnectionRest.getConnection().create(ServiceAlumno.class);
        servicePais=ConnectionRest.getConnection().create(ServicePais.class);

        adaptador= new ArrayAdapter<String>(this, androidx.appcompat.R.layout.
                support_simple_spinner_dropdown_item, paises);
        spnPais=findViewById(R.id.spnRegAlumno);
        spnPais.setAdapter(adaptador);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = txtNombres.getText().toString();
                String ape = txtApellidos.getText().toString();
                String tel = txtTelefono.getText().toString();
                String dni = txtDni.getText().toString();
                String cor = txtCorreo.getText().toString();
                String fn = txtFechaNacimiento.getText().toString();
                String est = txtEstado.getText().toString();

                int indPais=spnPais.getSelectedItemPosition();
                btnRegistrar=findViewById(R.id.btnRegAlumno);
                if (!nom.matches(ValidacionUtil.TEXTO)){
                    mensajeToast("El Nombre es de 3 a 30 caracteres");
                }else if (!ape.matches(ValidacionUtil.TEXTO)){
                    mensajeToast("El Apellido es de 3 a 30 caracteres");
                }else if (!tel.matches(ValidacionUtil.TELEFONO)){
                    mensajeToast("El Telefono es de 9 digitos");
                }else if (!dni.matches(ValidacionUtil.DNI)){
                    mensajeToast("El DNI es de 8 digitos");
                }else if (!cor.matches(ValidacionUtil.CORREO)){
                    mensajeToast("El Correo debe ser ****@***.**");
                }else if (!fn.matches(ValidacionUtil.FECHA)){
                    mensajeToast("La Fecha de Nacimiento es formato YYYY-MM-DD");
                }else {
                    String pais = spnPais.getSelectedItem().toString();
                    String idPais = pais.split(":")[0];

                    Pais objPais = new Pais();
                    objPais.setIdPais(Integer.parseInt(idPais));


                    Alumno objAlumno = new Alumno();
                    objAlumno.setNombres(nom);
                    objAlumno.setApellidos(ape);
                    objAlumno.setTelefono(tel);
                    objAlumno.setDni(dni);
                    objAlumno.setCorreo(cor);
                    objAlumno.setFechaNacimiento(fn);
                    objAlumno.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objAlumno.setEstado(est);
                    objAlumno.setPais(objPais);

                    registrarAlumno(objAlumno);

                }

            }
        });


        cargarPais();

    }

    public  void registrarAlumno(Alumno obj){
        Call<Alumno> call= serviceAlumno.insertaAlumno(obj);
        call.enqueue(new Callback<Alumno>() {
            @Override
            public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                Alumno objAlumno= response.body();
                mensajeAlert("REGISTRO DE ALUMNO EXITOSO"+ "\n Id: " + objAlumno.getIdAlumno()+
                        "\n Nombres: " + objAlumno.getNombres()+
                        "\n Apellidos: " + objAlumno.getApellidos());
            }

            @Override
            public void onFailure(Call<Alumno> call, Throwable t) {
                mensajeToast("Error al registrar Alumno ->" + t.getMessage());
            }
        });
    }


    public void cargarPais(){
        Call<List<Pais>> call = servicePais.listaTodos();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if(response.isSuccessful()){
                    List<Pais> lstGrado = response.body();
                    for (Pais obj:lstGrado){
                        paises.add(obj.getIdPais()+":"+obj.getNombre());
                    }
                    adaptador.notifyDataSetChanged();
                }else{
                    mensajeToast("Error al acceder a la lista de Paises");
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                mensajeToast("Error de Registro: Cargar Paises ->" + t.getMessage());
            }
        });

    }



    public void mensajeAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }


    public void mensajeToast(String mensaje){
        Toast toast1 =  Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_LONG);
        toast1.show();
    }

}