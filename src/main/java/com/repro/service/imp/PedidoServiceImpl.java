package com.repro.service.imp;

import com.repro.dto.pedido.DetallePedidoRequestDTO;
import com.repro.dto.pedido.PedidoRequestDTO;
import com.repro.dto.pedido.PedidoResponseDTO;
import com.repro.model.*;
import com.repro.model.Enum.EstadoMesa;
import com.repro.repository.MesaRepository;
import com.repro.repository.PedidoRepository;
import com.repro.repository.PlatoRepository;
import com.repro.service.EstadoPedidoService;
import com.repro.service.PedidoService;
import com.repro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PlatoRepository platoRepository;
    private final EstadoPedidoService estadoPedidoService;
    private final MesaRepository mesaRepository;
    private final UsuarioService usuarioService;

    // =========================
    // MÉTODOS PÚBLICOS
    // =========================

    @Override
    public PedidoResponseDTO crearPedido(PedidoRequestDTO dto) {

        Pedido pedido = new Pedido();

        Mesa mesa = obtenerMesaLibre(dto.getMesaNumero());
        mesa.setEstado(EstadoMesa.OCUPADA);

        pedido.setMesa(mesa);
        pedido.setMesero(usuarioService.obtenerActual());
        pedido.setEstado(estadoPedidoService.obtenerPorNombre("CREADO"));
        pedido.setObservaciones(dto.getObservaciones());

        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedidoRequestDTO det : dto.getDetalles()) {
            total = total.add(agregarDetalle(pedido, det));
        }

        pedido.setTotal(total);

        return PedidoResponseDTO.from(pedidoRepository.save(pedido));
    }

    @Override
    public PedidoResponseDTO editarPedido(Long pedidoId, PedidoRequestDTO dto) {

        Pedido pedido = obtenerPedido(pedidoId);
        validarPedidoEditable(pedido);

        pedido.getDetalles().clear();

        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedidoRequestDTO det : dto.getDetalles()) {
            total = total.add(agregarDetalle(pedido, det));
        }

        pedido.setObservaciones(dto.getObservaciones());
        pedido.setTotal(total);

        return PedidoResponseDTO.from(pedido);
    }

    @Override
    public PedidoResponseDTO cambiarEstado(Long pedidoId, String nuevoEstado) {

        Pedido pedido = obtenerPedido(pedidoId);

        EstadoPedido estado = estadoPedidoService.obtenerPorNombre(nuevoEstado);
        pedido.setEstado(estado);

        return PedidoResponseDTO.from(pedido);
    }

    @Override
    public void cerrarPedido(Long pedidoId) {

        Pedido pedido = obtenerPedido(pedidoId);

        pedido.setEstado(
                estadoPedidoService.obtenerPorNombre("CERRADO")
        );

        pedido.getMesa().setEstado(EstadoMesa.LIBRE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorEstado(String estado) {

        return pedidoRepository.findByEstadoNombre(estado)
                .stream()
                .map(PedidoResponseDTO::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPorId(Long id) {
        return PedidoResponseDTO.from(obtenerPedido(id));
    }

    // =========================
    // MÉTODOS PRIVADOS (LÓGICA)
    // =========================

    private Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no existe"));
    }

    private Mesa obtenerMesaLibre(Integer numero) {

        Mesa mesa = mesaRepository.findByNumero(numero)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no existe"));

        if (mesa.getEstado() != EstadoMesa.LIBRE) {
            throw new IllegalStateException("La mesa no está disponible");
        }

        return mesa;
    }

    private void validarPedidoEditable(Pedido pedido) {
        if (!pedido.getEstado().getEditable()) {
            throw new IllegalStateException(
                    "El pedido no puede editarse en estado " +
                            pedido.getEstado().getNombre()
            );
        }
    }

    private BigDecimal agregarDetalle(Pedido pedido, DetallePedidoRequestDTO det) {

        Plato plato = platoRepository.findById(det.getPlatoId())
                .orElseThrow(() -> new IllegalArgumentException("Plato no existe"));

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setPlato(plato);
        detalle.setCantidad(det.getCantidad());
        detalle.setPrecioUnitario(plato.getPrecio());

        BigDecimal subtotal = plato.getPrecio()
                .multiply(BigDecimal.valueOf(det.getCantidad()));

        detalle.setSubtotal(subtotal);
        pedido.getDetalles().add(detalle);

        return subtotal;
    }
}
