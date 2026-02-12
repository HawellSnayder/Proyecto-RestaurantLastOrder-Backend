package com.repro.service;

import com.repro.model.EstadoPedido;

import java.util.List;

public interface EstadoPedidoService {

    EstadoPedido obtenerPorId(Long id);

    EstadoPedido obtenerPorNombre(String nombre);

    List<EstadoPedido> listarTodos();
}

