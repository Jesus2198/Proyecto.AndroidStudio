package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AlertDialog;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Grado;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceAutor;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceGrado;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutorRegistraActivity extends NewAppCompatActivity {

    Spinner spnGrado;
    ArrayAdapter<String> adaptador;
    ArrayList<String> grados = new ArrayList<>();

    //Servicion
    ServiceAutor serviceAutor;
    ServiceGrado serviceGrado;


    EditText txtNombres,txtApellidos,txtFechaNac,txtTelefono,txtFechRegis;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_autor_registra);

        txtNombres =findViewById(R.id.txtRegAutorNombres);
        txtApellidos=findViewById(R.id.txtRegAutorApellidos);
        txtFechaNac=findViewById(R.id.txtRegAutorfechaNacimiento);
        txtTelefono=findViewById(R.id.txtRegAutortelefono);

        btnRegistrar=findViewById(R.id.btnRegAutor);

        serviceAutor = ConnectionRest.getConnection().create(ServiceAutor.class);
        serviceGrado=ConnectionRest.getConnection().create(ServiceGrado.class);

        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.
                support_simple_spinner_dropdown_item, grados);
        spnGrado=findViewById(R.id.spnRegAutor);
        spnGrado.setAdapter(adaptador);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomb =txtNombres.getText().toString();
                String apell=txtApellidos.getText().toString();
                String fechNac=txtFechaNac.getText().toString();
                String telefono=txtTelefono.getText().toString();


                int indgrado =spnGrado.getSelectedItemPosition();
                btnRegistrar=findViewById(R.id.btnRegAutor);

                if (!nomb.matches(ValidacionUtil.TEXTO)){
                    mensajeToast("Nombrel es de 3 a 30 caracteres");
                }else if (!apell.matches(ValidacionUtil.TEXTO)){
                    mensajeToast("Apellido es de 3 a 30 caracteres");
                }else if (!fechNac.matches(ValidacionUtil.FECHA)){
                    mensajeToast("La fecha es YYYY-MM-DD");
                }else if(!telefono.matches(ValidacionUtil.TELEFONO)){
                    mensajeToast("Telefono es de 09 dígitos");
                }else{

                    String grado =spnGrado.getSelectedItem().toString();
                    String idGrado= grado.split(":")[0];

                    Grado objGrado = new Grado();
                    objGrado.setIdGrado(Integer.parseInt(idGrado));

                    Autor objAutor = new Autor();
                    objAutor.setNombres(nomb);
                    objAutor.setApellidos(apell);
                    objAutor.setFechaNacimiento(fechNac);
                    objAutor.setTelefono(telefono);
                    objAutor.setEstado(1);
                    objAutor.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objAutor.setGrado(objGrado);

                    registrarAutor(objAutor);
                }
            }

        });

        cargarGrado();

    }

    public  void registrarAutor(Autor obj){
        Call<Autor> call= serviceAutor.insertarAutor(obj);
        call.enqueue(new Callback<Autor>() {
            @Override
            public void onResponse(Call<Autor> call, Response<Autor> response) {
                Autor objAutor= response.body();
                mensajeAlert("Se ha Registrado el Autor:"+ "\n ID   >>>" + objAutor.getIdAutor()+
                        "\n Nombres   >>> " + objAutor.getNombres()+
                        "\n Apellidos >>> " + objAutor.getApellidos());
            }

            @Override
            public void onFailure(Call<Autor> call, Throwable t) {
                mensajeToast("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }


    public void cargarGrado(){
        Call<List<Grado>> call = serviceGrado.TodoLosGrados();
        call.enqueue(new Callback<List<Grado>>() {
            @Override
            public void onResponse(Call<List<Grado>> call, Response<List<Grado>> response) {
                if(response.isSuccessful()){
                    List<Grado> lstGrado = response.body();
                    for (Grado obj:lstGrado){
                        grados.add(obj.getIdGrado()+":"+obj.getDescripcion());
                    }
                    adaptador.notifyDataSetChanged();
                }else{
                    mensajeToast("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Grado>> call, Throwable t) {
                mensajeToast("Error al Acceder al Servicio Rest >>> " + t.getMessage());
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