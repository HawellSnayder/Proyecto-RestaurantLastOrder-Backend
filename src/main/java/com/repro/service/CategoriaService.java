package com.repro.service;

import com.repro.dto.categoria.CategoriaRequestDTO;
import com.repro.dto.categoria.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {

    CategoriaResponseDTO crear(CategoriaRequestDTO dto);

    CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO dto);

    List<CategoriaResponseDTO> listarActivas();

    List<CategoriaResponseDTO> listarTodas();

    void desactivar(Long id);
    void cambiarEstado(Long id, Boolean activo);
    void eliminar(Long id);
}

