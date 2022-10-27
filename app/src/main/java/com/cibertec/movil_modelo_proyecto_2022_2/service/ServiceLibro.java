package com.cibertec.movil_modelo_proyecto_2022_2.service;

import com.cibertec.movil_modelo_proyecto_2022_2.entity.Libro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceLibro {
    @POST("libro")
    public Call<Libro> insertarLibro(@Body Libro obj);

    @GET("libro")
    public Call<List<Libro>> listaLibro();
}