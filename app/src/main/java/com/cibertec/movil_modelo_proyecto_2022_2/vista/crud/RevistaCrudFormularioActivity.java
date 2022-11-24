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
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Modalidad;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceModalidad;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceRevista;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevistaCrudFormularioActivity extends NewAppCompatActivity {

    Spinner spnModalidad;
    ArrayAdapter<String> adaptador;
    ArrayList<String> modalidades = new ArrayList<String>();

    ServiceRevista serviceRevista;
    ServiceModalidad serviceModalidad;

    EditText txtRevistaNombre, txtRevistaFrecuencia, txtRevistaFechaCre;
    Button btnRegistrar, btnEliminar, btnRegresar;
    TextView txtTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revista_crud_formulario);
        txtTitulo = findViewById(R.id.idCrudRevistaFrmTitulo);
        txtRevistaNombre = findViewById(R.id.txtCrudRevistaFrmNombre);
        txtRevistaFrecuencia = findViewById(R.id.txtCrudRevistaFrmFrecuencia);
        txtRevistaFechaCre = findViewById(R.id.txtCrudRevistaFrmFechaCre);
        btnRegistrar = findViewById(R.id.idCrudRevistaFrmEnviar);
        btnEliminar = findViewById(R.id.idCrudRevistaFrmEliminar);
        btnRegresar = findViewById(R.id.idCrudRevistaFrmRegresar);

        serviceModalidad = ConnectionRest.getConnection().create(ServiceModalidad.class);
        serviceRevista = ConnectionRest.getConnection().create(ServiceRevista.class);

        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, modalidades);
        spnModalidad = findViewById(R.id.idCrudRevistaFrmModalidad);
        spnModalidad.setAdapter(adaptador);

        txtRevistaFechaCre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar myCalendar= Calendar.getInstance();
                new DatePickerDialog(
                        RevistaCrudFormularioActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month , int day) {
                                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", new Locale("es"));
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH,month);
                                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                                txtRevistaFechaCre.setText(dateFormat.format(myCalendar.getTime()));
                            }
                            },
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        cargaModalidad();

        Bundle extras = getIntent().getExtras();
        String tipo = extras.getString("var_tipo");
        if (tipo.equals("REGISTRAR")){
            txtTitulo.setText("Mantenimiento Revista - REGISTRA");
            btnRegistrar.setText("REGISTRA");
            btnEliminar.setEnabled(false);
        }else  if (tipo.equals("ACTUALIZAR")){
            txtTitulo.setText("Mantenimiento Revista - ACTUALIZA");
            btnRegistrar.setText("ACTUALIZA");

            Revista objRevista = (Revista) extras.get("var_item");
            txtRevistaNombre.setText(objRevista.getNombre());
            txtRevistaFrecuencia.setText(objRevista.getFrecuencia());
            txtRevistaFechaCre.setText(objRevista.getFechaCreacion());
        }

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = txtRevistaNombre.getText().toString();
                String fre = txtRevistaFrecuencia.getText().toString();
                String fec = txtRevistaFechaCre.getText().toString();
                int indModalidad = spnModalidad.getSelectedItemPosition();
                if (!nom.matches(ValidacionUtil.TEXTO)){
                    mensajeToastLong("El nombre de la revista debe ser de 2 a 20 caracteres");
                }
                else if (!fre.matches(ValidacionUtil.TEXTO)){
                    mensajeToastLong("La frecuencia debe ser de 2 a 20 caracteres");
                }
                else if (!fec.matches(ValidacionUtil.FECHA)){
                    mensajeToastLong("La fecha es YYYY-MM-DD");
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

                    Bundle extras = getIntent().getExtras();
                    String tipo = extras.getString("var_tipo");
                    Revista obj = (Revista)extras.get("var_item");
                    if (tipo.equals("REGISTRAR")){
                        registra(objRevista);
                    }else if (tipo.equals("ACTUALIZAR")){
                        objRevista.setIdRevista(obj.getIdRevista());
                        actualiza(objRevista);
                    }
                }
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RevistaCrudFormularioActivity.this, RevistaCrudListaActivity.class);
                startActivity(intent);
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revista objEditorial = (Revista) extras.get("var_item");
                elimina(objEditorial.getIdRevista());

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

                    Bundle extras = getIntent().getExtras();
                    String tipo = extras.getString("var_tipo");
                    Revista obj = (Revista) extras.get("var_item");
                    if (tipo.equals("ACTUALIZAR")){
                        int idModalidad = obj.getModalidad().getIdModalidad();
                        String nombreModelidad = obj.getModalidad().getDescripcion();
                        String itemModalidad = idModalidad+":"+nombreModelidad;
                        int posSeleccionada = -1;
                        for(int i=0; i< modalidades.size(); i++){
                            if (modalidades.get(i).equals(itemModalidad)){
                                posSeleccionada = i;
                                break;
                            }
                        }
                        spnModalidad.setSelection(posSeleccionada);
                    }
                } else {
                    mensajeToastLong("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Modalidad>> call, Throwable t) {
                mensajeToastLong("Error al accede al Servicio Rest >>> " + t.getMessage());
            }
        });
    }
    public void registra(Revista obj){
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
                mensajeToastLong("Error al accede al Servicio Rest >>> " + t.getMessage());
            }
        });
    }
    public void actualiza(Revista obj){
        Call<Revista> call = serviceRevista.actualizaRevista(obj);
        call.enqueue(new Callback<Revista>() {
            @Override
            public void onResponse(Call<Revista> call, Response<Revista> response) {
                if (response.isSuccessful()){
                    Revista objRevista = response.body();
                    mensajeAlert("Se actualizó la revista de ID >>> " + objRevista.getIdRevista());
                }
                else {
                    mensajeAlert(response.message() + " - " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Revista> call, Throwable t) {
                mensajeToastLong("Error al accede al Servicio Rest >>> " + t.getMessage());
            }
        });
    }
    public void elimina(int id) {
        Call<Revista> call = serviceRevista.eliminaRevista(id);
        call.enqueue(new Callback<Revista>() {
            @Override
            public void onResponse(Call<Revista> call, Response<Revista> response) {
                if (response.isSuccessful()) {
                    Revista objSalida = response.body();
                    if (objSalida == null){
                        mensajeToastLong("ERROR -> NO se eliminó");
                    }else{
                        mensajeToastLong("ÉXITO -> Se eliminó correctamente : " + objSalida.getIdRevista());
                    }
                }else{
                    mensajeToastLong("ERROR -> Error en la respuesta");
                }
            }
            @Override
            public void onFailure(Call<Revista> call, Throwable t) {
                mensajeToastLong("ERROR -> " +   t.getMessage());
            }
        });
    }
}