package com.repro.service;

import com.repro.dto.usuario.CrearUsuarioRequestDTO;
import com.repro.model.Usuario;

import java.util.List;

public interface UsuarioService {

    Usuario crear(CrearUsuarioRequestDTO dto);

    List<Usuario> listarActivos();

    void desactivar(Long id);

    Usuario obtenerActual();
}

