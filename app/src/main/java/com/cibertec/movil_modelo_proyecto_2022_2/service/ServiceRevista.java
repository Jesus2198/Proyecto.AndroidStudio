package com.cibertec.movil_modelo_proyecto_2022_2.service;

import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceRevista {
    @POST("revista")
    public Call<Revista> insertaRevista(@Body Revista obj);

    @GET("revista")
    public Call<List<Revista>> listaRevista();

    @PUT("revista")
    public Call<Revista> actualizaRevista(@Body Revista obj);

    @DELETE("revista/{id}")
    public Call<Revista> eliminaRevista(@Path("id") int id);

    @GET("revista/porNombre/{nombre}")
    public Call<List<Revista>> listaRevistaPorNombre(@Path("nombre") String nombre);
}
