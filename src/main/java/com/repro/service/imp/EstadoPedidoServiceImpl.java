package com.repro.service.imp;

import com.repro.model.EstadoPedido;
import com.repro.repository.EstadoPedidoRepository;
import com.repro.service.EstadoPedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EstadoPedidoServiceImpl implements EstadoPedidoService {

    private final EstadoPedidoRepository repository;

    public EstadoPedidoServiceImpl(EstadoPedidoRepository repository) {
        this.repository = repository;
    }

    @Override
    public EstadoPedido obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
    }

    @Override
    public EstadoPedido obtenerPorNombre(String nombre) {
        return repository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Estado no existe"));
    }

    @Override
    public List<EstadoPedido> listarTodos() {
        return repository.findAll();
    }
}

