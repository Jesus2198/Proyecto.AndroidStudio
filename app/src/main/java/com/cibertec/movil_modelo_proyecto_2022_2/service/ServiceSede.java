package com.cibertec.movil_modelo_proyecto_2022_2.service;

import com.cibertec.movil_modelo_proyecto_2022_2.entity.Sede;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceSede {

    @GET("util/listaSede")
    public abstract Call<List<Sede>> listaSede();
}
