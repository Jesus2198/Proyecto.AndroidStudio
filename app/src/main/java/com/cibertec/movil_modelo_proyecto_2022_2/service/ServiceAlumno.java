package com.cibertec.movil_modelo_proyecto_2022_2.service;

import com.cibertec.movil_modelo_proyecto_2022_2.entity.Alumno;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceAlumno {
    @POST("alumno")
    public Call<Alumno> insertaAlumno(@Body Alumno obj);
}
