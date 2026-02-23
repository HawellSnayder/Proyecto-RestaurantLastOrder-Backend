package com.repro.service.imp;

import com.repro.dto.pedido.*;
import com.repro.model.*;
import com.repro.model.Enum.EstadoMesa;
import com.repro.model.Enum.EventoPedido;
import com.repro.repository.MesaRepository;
import com.repro.repository.PedidoRepository;
import com.repro.repository.PlatoRepository;
import com.repro.service.EstadoPedidoService;
import com.repro.service.PedidoService;
import com.repro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PlatoRepository platoRepository;
    private final EstadoPedidoService estadoPedidoService;
    private final MesaRepository mesaRepository;
    private final UsuarioService usuarioService;
    private final SimpMessagingTemplate messagingTemplate;


    @Override
    public PedidoResponseDTO crearPedido(PedidoRequestDTO dto) {

        Mesa mesa = obtenerMesaLibre(dto.getMesaNumero());
        mesa.setEstado(EstadoMesa.OCUPADA);

        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setMesero(usuarioService.obtenerActual());
        pedido.setEstado(estadoPedidoService.obtenerPorNombre("CREADO"));
        pedido.setObservaciones(dto.getObservaciones());

        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedidoRequestDTO det : dto.getDetalles()) {
            total = total.add(agregarDetalle(pedido, det));
        }

        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido);

        enviarEventoSocket(guardado, EventoPedido.NUEVO);

        return PedidoResponseDTO.from(guardado);
    }


    @Override
    public PedidoResponseDTO editarPedido(Long pedidoId, PedidoRequestDTO dto) {
        Pedido pedido = obtenerPedido(pedidoId);
        validarPedidoEditable(pedido);
        List<Long> idsNuevos = dto.getDetalles().stream()
                .map(DetallePedidoRequestDTO::getPlatoId)
                .toList();
        pedido.getDetalles().removeIf(det -> !idsNuevos.contains(det.getPlato().getId()));

        BigDecimal totalAcumulado = BigDecimal.ZERO;

        for (DetallePedidoRequestDTO detDto : dto.getDetalles()) {
            DetallePedido detalle = pedido.getDetalles().stream()
                    .filter(d -> d.getPlato().getId().equals(detDto.getPlatoId()))
                    .findFirst()
                    .orElse(null);

            if (detalle != null) {
                detalle.setCantidad(detDto.getCantidad());
                BigDecimal subtotal = detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detDto.getCantidad()));
                detalle.setSubtotal(subtotal);
                totalAcumulado = totalAcumulado.add(subtotal);
            } else {
                totalAcumulado = totalAcumulado.add(agregarDetalle(pedido, detDto));
            }
        }

        pedido.setTotal(totalAcumulado);
        pedido.setObservaciones(dto.getObservaciones());

        Pedido guardado = pedidoRepository.save(pedido);
        enviarEventoSocket(guardado, EventoPedido.EDITADO);

        return PedidoResponseDTO.from(guardado);
    }


    @Override
    public PedidoResponseDTO cambiarEstado(Long pedidoId, String nuevoEstado) {

        Pedido pedido = obtenerPedido(pedidoId);

        String estadoActual = pedido.getEstado().getNombre();

        if (estadoActual.equalsIgnoreCase("FINALIZADO") ||
                estadoActual.equalsIgnoreCase("CANCELADO")) {
            throw new IllegalStateException("El pedido ya está cerrado");
        }

        validarTransicion(estadoActual, nuevoEstado);

        EstadoPedido estado = estadoPedidoService.obtenerPorNombre(nuevoEstado);

        if (nuevoEstado.equalsIgnoreCase("CANCELADO")) {

            pedido.setEstado(estado);
            pedido.getMesa().setEstado(EstadoMesa.LIBRE);

            enviarEventoSocket(pedido, EventoPedido.CANCELADO);

            return PedidoResponseDTO.from(pedido);
        }

        if (nuevoEstado.equalsIgnoreCase("PAGADO")) {
            pedido.setEstado(estado);
            pedido.getMesa().setEstado(EstadoMesa.LIBRE);
            enviarEventoSocket(pedido, EventoPedido.ESTADO_CAMBIADO);

            return PedidoResponseDTO.from(pedido);
        }

        pedido.setEstado(estado);

        enviarEventoSocket(pedido, EventoPedido.ESTADO_CAMBIADO);

        return PedidoResponseDTO.from(pedido);
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
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(PedidoResponseDTO::from)
                .collect(Collectors.toList());
    }


    private void validarTransicion(String actual, String nuevo) {

        if (actual.equals("CREADO") &&
                !(nuevo.equals("EN_PREPARACION") || nuevo.equals("CANCELADO"))) {
            throw new IllegalStateException("Transición inválida");
        }

        if (actual.equals("EN_PREPARACION") &&
                !(nuevo.equals("LISTO") || nuevo.equals("CANCELADO"))) {
            throw new IllegalStateException("Transición inválida");
        }

        if (actual.equals("LISTO") &&
                !(nuevo.equals("ENTREGADO") || nuevo.equals("CANCELADO"))) {
            throw new IllegalStateException("Transición inválida");
        }

        if (actual.equals("ENTREGADO") &&
                !(nuevo.equals("PAGADO") || nuevo.equals("CANCELADO"))) {
            throw new IllegalStateException("Transición inválida");
        }
    }


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
        if (det.getPlatoId() == null) {
            throw new IllegalArgumentException("Error: El detalle del pedido contiene un ID de plato nulo");
        }

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

    private void enviarEventoSocket(Pedido pedido, EventoPedido evento) {

        PedidoSocketDTO dto = new PedidoSocketDTO();
        dto.setId(pedido.getId());
        dto.setMesaNumero(pedido.getMesa().getNumero());
        dto.setEstado(pedido.getEstado().getNombre());
        dto.setTotal(pedido.getTotal());
        dto.setEvento(evento);

        messagingTemplate.convertAndSend("/topic/pedidos", dto);
    }

}
