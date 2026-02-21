package com.repro.service;

import com.repro.dto.plato.PlatoRequestDTO;
import com.repro.dto.plato.PlatoResponseDTO;
import com.repro.model.Plato;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlatoService {

    List<PlatoResponseDTO> listarDisponibles();

    PlatoResponseDTO crear(PlatoRequestDTO dto);

    PlatoResponseDTO actualizar(Long id, PlatoRequestDTO dto);

    void desactivar(Long id);
    void cambiarDisponibilidad(Long id, Boolean disponible);
    PlatoResponseDTO crearConImagen(PlatoRequestDTO dto, MultipartFile archivo);
    List<PlatoResponseDTO> listarTodos();
    void eliminar(Long id);
}


