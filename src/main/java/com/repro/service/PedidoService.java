package com.repro.service;

import com.repro.dto.pedido.PedidoRequestDTO;
import com.repro.dto.pedido.PedidoResponseDTO;

import java.util.List;

public interface PedidoService {

    PedidoResponseDTO crearPedido(PedidoRequestDTO dto);

    PedidoResponseDTO editarPedido(Long pedidoId, PedidoRequestDTO dto);

    PedidoResponseDTO cambiarEstado(Long pedidoId, String nuevoEstado);

    void cerrarPedido(Long pedidoId);

    List<PedidoResponseDTO> listarPorEstado(String estado);

    PedidoResponseDTO obtenerPorId(Long id);
}


