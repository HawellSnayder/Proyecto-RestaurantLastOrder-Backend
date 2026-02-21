package com.repro.service;

import com.repro.dto.mesa.MesaRequestDTO;
import com.repro.model.Mesa;

import java.util.List;

public interface MesaService {

    Mesa crear(MesaRequestDTO dto);

    List<Mesa> listarLibres();

    void reservar(Long id);

    void liberar(Long id);

    void ocupar(Long id);

    List<Mesa> listarTodas();
    void eliminar(Long id);
    Mesa actualizar(Long id, MesaRequestDTO dto);

}
