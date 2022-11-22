package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Editorial;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Grado;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Pais;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceAutor;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceGrado;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServicePais;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutorCrudFormularioActivity extends NewAppCompatActivity {

    Spinner spnGrado;
    ArrayAdapter<String> adaptador;
    ArrayList<String> grados = new ArrayList<String>();

    ServiceAutor serviceAutor;
    ServiceGrado serviceGrado;

    TextView txtTitulo;
    EditText txtNombre,txtApellido,txtFechNac,txtTelefono;
    Button btnRegistrar,btnRegresar;

    String tipo;

    Autor obj=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autor_crud_formulario);

        txtTitulo=findViewById(R.id.idCrudAutorFrmTitulo);
        txtNombre=findViewById(R.id.idCrudAutorFrmNombre);
        txtApellido=findViewById(R.id.idCrudAutorFrmApellido);
        txtFechNac=findViewById(R.id.idCrudAutorFrmNacimineto);
        txtTelefono=findViewById(R.id.idCrudAutorFrmTelefono);

        btnRegistrar=findViewById(R.id.idCrudAutorFrmBtnRegistrar);
        btnRegresar=findViewById(R.id.idCrudAutorFrmBtnRegresar);

        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, grados);
        spnGrado=findViewById(R.id.idCrudAutorFrmGrado);
        spnGrado.setAdapter(adaptador);

        serviceAutor= ConnectionRest.getConnection().create(ServiceAutor.class);
        serviceGrado=ConnectionRest.getConnection().create(ServiceGrado.class);

        ///Cargar Spiner Grado
        cargarGrado();

        Bundle extras=getIntent().getExtras();
        tipo=extras.getString("var_tipo");

        if(tipo.equals("REGISTRAR")){
            txtTitulo.setText("Mantenimiento Autor - REGISTRA");
            btnRegistrar.setText("REGISTRA");
        }else if(tipo.equals("ACTUALIZAR")){
            txtTitulo.setText("Mantenimiento Autor - ACTUALIZA");
            btnRegistrar.setText("ACTUALIZA");

            obj=(Autor) extras.get("var_item");

            txtNombre.setText(obj.getNombres());
            txtApellido.setText(obj.getApellidos());
            txtFechNac.setText(obj.getFechaNacimiento());
            txtTelefono.setText(obj.getTelefono());
        }

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom=txtNombre.getText().toString();
                String apell=txtApellido.getText().toString();
                String fechNac=txtFechNac.getText().toString();
                String telefono=txtTelefono.getText().toString();


                if(!nom.matches(ValidacionUtil.TEXTO)){
                    mensajeAlert("El Nombre es 2 a 20 caracteres");
                }else if(!apell.matches(ValidacionUtil.TEXTO)){
                    mensajeAlert("El Apellido es 2 a 20 caracteres");
                }else if(!fechNac.matches(ValidacionUtil.FECHA)){
                    mensajeAlert("La fecha es de formato YYYY-MM-dd");
                }else if(!telefono.matches(ValidacionUtil.TELEFONO)){
                    mensajeAlert("TELEFONO 09 DIGITOS");
                }else{

                    String grado=spnGrado.getSelectedItem().toString();
                    String idGrado=grado.split(":")[0];



                    Grado objGrado = new Grado();
                    objGrado.setIdGrado(Integer.parseInt(idGrado));


                    Autor objAutor= new Autor();
                    objAutor.setNombres(nom);
                    objAutor.setApellidos(apell);
                    objAutor.setFechaNacimiento(fechNac);
                    objAutor.setTelefono(telefono);
                    objAutor.setEstado(1);
                    objAutor.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objAutor.setGrado(objGrado);



                    if(tipo.equals("REGISTRAR")){
                        registraAutor(objAutor);
                    }else if(tipo.equals("ACTUALIZAR")){
                        Autor obj=(Autor) extras.get("var_item");
                        objAutor.setIdAutor(obj.getIdAutor());
                        actualizaAutor(objAutor);
                    }

                }



            }


        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AutorCrudFormularioActivity.this, AutorCrudListaActivity.class);
                startActivity(intent);
            }
        });


        txtFechNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar myCalendar= Calendar.getInstance();
                new DatePickerDialog(
                        AutorCrudFormularioActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month , int day) {
                                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", new Locale("es"));
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH,month);
                                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                                txtFechNac.setText(dateFormat.format(myCalendar.getTime()));
                            }
                            },
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }

    public void registraAutor(Autor objAutor) {
        Call<Autor> call = serviceAutor.insertarAutor(objAutor);
        call.enqueue(new Callback<Autor>() {
            @Override
            public void onResponse(Call<Autor> call, Response<Autor> response) {
                if (response.isSuccessful()) {
                    Autor objSalida = response.body();
                    mensajeAlert("Se registro Autor " +
                            "\nID >> " + objSalida.getIdAutor() +
                            "\nNombre >> " + objSalida.getNombres());
                }
            }

            @Override
            public void onFailure(Call<Autor> call, Throwable t) {
                mensajeAlert("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }

    public void cargarGrado(){
        Call<List<Grado>> call = serviceGrado.TodoLosGrados();
        call.enqueue(new Callback<List<Grado>>() {
            @Override
            public void onResponse(Call<List<Grado>> call, Response<List<Grado>> response) {
                if (response.isSuccessful()){
                    List<Grado> lstGrado =  response.body();
                    for(Grado obj: lstGrado){
                        grados.add(obj.getIdGrado() +":"+ obj.getDescripcion());
                    }
                    adaptador.notifyDataSetChanged();



                    if (tipo.equals("ACTUALIZAR")){

                        int idGrado = obj.getGrado().getIdGrado();
                        String nombreGrado = obj.getGrado().getDescripcion();

                        String itemGrado = idGrado+":"+nombreGrado;
                        int posSeleccionada = -1;
                        for(int i=0; i< grados.size(); i++){
                            if (grados.get(i).equals(itemGrado)){
                                posSeleccionada = i;
                                break;
                            }
                        }
                        spnGrado.setSelection(posSeleccionada);
                    }

                }else{
                    mensajeToastLong("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Grado>> call, Throwable t) {
                mensajeToastLong("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }

    public void actualizaAutor(Autor obj){
        Call<Autor> call = serviceAutor.actualizarAutor(obj);
        call.enqueue(new Callback<Autor>() {
            @Override
            public void onResponse(Call<Autor> call, Response<Autor> response) {
                if (response.isSuccessful()){
                    Autor objSalida =   response.body();
                    mensajeAlert("Se actualizo Autor " +
                            "\nID >> " + objSalida.getIdAutor() +
                            "\nNombre autor >> " + objSalida.getNombres() );
                }
            }
            @Override
            public void onFailure(Call<Autor> call, Throwable t) {
                mensajeAlert("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }




}