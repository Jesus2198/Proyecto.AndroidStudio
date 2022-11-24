package com.cibertec.movil_modelo_proyecto_2022_2.service;

import com.cibertec.movil_modelo_proyecto_2022_2.entity.Libro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceLibro {
    @POST("libro")
    public Call<Libro> insertarLibro(@Body Libro obj);

    @GET("libro")
    public Call<List<Libro>> listaLibro();




    @PUT("libro")
    public Call<Libro> actualizarLibro(@Body Libro obj);

    @DELETE("Libro/{id}")
    public Call<Libro> eliminarLibro(@Path("id") int idLibro);

    @GET("libro/porTitulo/{nombre}")
    public Call<List<Libro>> listaLibroPorTitulo(@Path("nombre")String nombre);




}
