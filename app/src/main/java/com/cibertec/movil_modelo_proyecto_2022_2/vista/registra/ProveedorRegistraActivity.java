package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Pais;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Proveedor;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServicePais;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceProveedor;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProveedorRegistraActivity extends NewAppCompatActivity {


    EditText txtRazonSocial,
             txtRuc,
             txtDireccion,
             txtTelefono,
             txtCelular,
             txtContacto;
    //-----------------------------
    Spinner  spnPais;
    ArrayAdapter<String> adapter;
    ArrayList<String> pais = new ArrayList<>();
    //-----------------------------
    Button btnEnviar;

    ServiceProveedor serviceProveedor;
    ServicePais servicePais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor_registra);

        txtRazonSocial = findViewById(R.id.txtProveRazonSocial);
        txtRuc = findViewById(R.id.txtProveRuc);
        txtDireccion = findViewById(R.id.txtProveDireccion);
        txtTelefono = findViewById(R.id.txtProveTelefono);
        txtCelular = findViewById(R.id.txtProveCelular);
        txtContacto = findViewById(R.id.txtProveContacto);
        spnPais = findViewById(R.id.spnProvePais);
        btnEnviar = findViewById(R.id.btnRegProveedor);

        serviceProveedor = ConnectionRest.getConnection().create(ServiceProveedor.class);
        servicePais = ConnectionRest.getConnection().create(ServicePais.class);

        adapter = new ArrayAdapter<String>(
                this, androidx.appcompat.R.layout.
                support_simple_spinner_dropdown_item, pais);
        spnPais=findViewById(R.id.spnProvePais);
        spnPais.setAdapter(adapter);
      //-------------------------------------------------------------------------

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rs = txtRazonSocial.getText().toString();
                String ruc = txtRuc.getText().toString();
                String dir = txtDireccion.getText().toString();
                String tel = txtTelefono.getText().toString();
                String cel = txtCelular.getText().toString();
                String cont = txtContacto.getText().toString();
                String Pais = spnPais.getSelectedItem().toString();
                String idPais = Pais.split(":")[0];

                if (!rs.matches(ValidacionUtil.TEXTO)){
                    mensajeToast("El Valor no cumple los Parametros");
                }else if (!ruc.matches(ValidacionUtil.RUC)){
                    mensajeToast("El RUC es de 11 dígitos");
                }else if (!dir.matches(ValidacionUtil.DIRECCION)){
                    mensajeToast("la Direccion no cumple los Parametros ");
                }else if(!tel.matches(ValidacionUtil.FIJO)){
                    mensajeToast("Telefono es de 07 dígitos");
                }else if(!cel.matches(ValidacionUtil.TELEFONO)){
                    mensajeToast("Celular es de 09 dígitos");
                }else if(!cont.matches(ValidacionUtil.NOMBRE)){
                    mensajeToast("Contacto no Valido");
                }else {


                    Pais objPais = new Pais();
                    objPais.setIdPais(Integer.parseInt(idPais));

                    Proveedor obj = new Proveedor();
                    obj.setRazonsocial(rs);
                    obj.setRuc(ruc);
                    obj.setDireccion(dir);
                    obj.setTelefono(tel);
                    obj.setCelular(cel);
                    obj.setContacto(cont);
                    obj.setEstado(1);
                    obj.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    obj.setPais(objPais);

                    registraProveedor(obj);

                }
            }
        });

        cargarPais();
    }

    public void registraProveedor(Proveedor obj){
        Call<Proveedor> call =  serviceProveedor.insertarProveedor(obj);
        call.enqueue(new Callback<Proveedor>() {
            @Override
            public void onResponse(Call<Proveedor> call, Response<Proveedor> response) {
                   if (response.isSuccessful()){
                       Proveedor objSalida = response.body();
                       mensajeAlert("Registro Exitoso:"+ "\n Cod =" + objSalida.getIdProveedor()
                                        + "\n Razón Social =" + objSalida.getRazonsocial()+
                                            "\n Telefono =" + objSalida.getTelefono());


                   }
            }

            @Override
            public void onFailure(Call<Proveedor> call, Throwable t) {
                mensajeAlert("Error al acceder al servicio rest >>>" + t.getMessage());
            }
        });
    }

    public void cargarPais(){
        Call<List<Pais>> call = servicePais.listaTodos();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if(response.isSuccessful()){
                    List<Pais> listaPais = response.body();
                    for (Pais obj: listaPais) {
                        pais.add(obj.getIdPais() + ":" + obj.getNombre());
                    }
                    adapter.notifyDataSetChanged();
                    }else{
                        mensajeToast("Error al acceder al servicio rest >>> ");
                }
            }


            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                mensajeToast("Error al Acceder al servicio rest >>>" + t.getMessage());
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
        Toast toast1 = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG);
        toast1.show();
    }
}