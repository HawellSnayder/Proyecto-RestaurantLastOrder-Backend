package com.repro.service;

import com.repro.model.Mesa;

import java.util.List;

public interface MesaService {

    Mesa crear(Integer numero, Integer capacidad);

    List<Mesa> listarLibres();

    void reservar(Long id);

    void liberar(Long id);

    void ocupar(Long id);

    List<Mesa> listarTodas();

}
