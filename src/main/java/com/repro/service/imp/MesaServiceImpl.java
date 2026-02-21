package com.repro.service.imp;

import com.repro.dto.mesa.MesaRequestDTO;
import com.repro.dto.mesa.MesaSocketDTO;
import com.repro.model.Enum.EstadoMesa;
import com.repro.model.Enum.EventoMesa;
import com.repro.model.Mesa;
import com.repro.repository.MesaRepository;
import com.repro.service.MesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MesaServiceImpl implements MesaService {

    private final MesaRepository mesaRepository;
    private final SimpMessagingTemplate messagingTemplate;



    @Override
    @Transactional
    public Mesa crear(MesaRequestDTO dto) { // <--- Cambiamos parámetros por el DTO

        // Ahora usamos dto.getNumero()
        if (mesaRepository.existsByNumero(dto.getNumero())) {
            throw new IllegalArgumentException("La mesa número " + dto.getNumero() + " ya existe");
        }

        Mesa mesa = new Mesa();
        mesa.setNumero(dto.getNumero());
        mesa.setCapacidad(dto.getCapacidad());
        mesa.setEstado(EstadoMesa.LIBRE);

        Mesa guardada = mesaRepository.save(mesa);

        enviarEvento(guardada, EventoMesa.CREADA);

        return guardada;
    }


    @Override
    @Transactional
    public void reservar(Long id) {

        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));

        if (mesa.getEstado() != EstadoMesa.LIBRE) {
            throw new IllegalStateException("La mesa no está disponible");
        }

        mesa.setEstado(EstadoMesa.RESERVADA);

        enviarEvento(mesa, EventoMesa.ACTUALIZADA);
    }


    @Transactional
    public void ocupar(Long id) {

        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));

        mesa.setEstado(EstadoMesa.OCUPADA);

        enviarEvento(mesa, EventoMesa.ACTUALIZADA);
    }


    @Transactional
    public void liberar(Long id) {

        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));

        mesa.setEstado(EstadoMesa.LIBRE);

        enviarEvento(mesa, EventoMesa.ACTUALIZADA);
    }

    // =========================
    // CONSULTAS
    // =========================

    @Override
    public List<Mesa> listarLibres() {
        return mesaRepository.findByEstado(EstadoMesa.LIBRE);
    }

    @Override
    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        if (mesa.getEstado() != EstadoMesa.LIBRE) {
            throw new RuntimeException("No se puede eliminar una mesa que está " + mesa.getEstado());
        }

        // Guardamos los datos antes de borrar para el último evento
        mesaRepository.delete(mesa);

        // UNIFICADO: Enviamos el evento con el tipo ELIMINADA
        enviarEvento(mesa, EventoMesa.ELIMINADA);
    }

    @Override
    @Transactional
    public Mesa actualizar(Long id, MesaRequestDTO dto) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La mesa con ID " + id + " no existe"));

        if (!mesa.getNumero().equals(dto.getNumero())) {
            if (mesaRepository.existsByNumero(dto.getNumero())) {
                throw new RuntimeException("Ya existe otra mesa con el número: " + dto.getNumero());
            }
            mesa.setNumero(dto.getNumero());
        }

        mesa.setCapacidad(dto.getCapacidad());
        Mesa mesaActualizada = mesaRepository.save(mesa);

        // UNIFICADO: Usamos tu método enviarEvento
        enviarEvento(mesaActualizada, EventoMesa.ACTUALIZADA);

        return mesaActualizada;
    }

    // =========================
    // SOCKET
    // =========================

    private void enviarEvento(Mesa mesa, EventoMesa evento) {

        MesaSocketDTO dto = new MesaSocketDTO();
        dto.setId(mesa.getId());
        dto.setNumero(mesa.getNumero());
        dto.setCapacidad(mesa.getCapacidad());
        dto.setEstado(mesa.getEstado());
        dto.setEvento(evento);

        messagingTemplate.convertAndSend("/topic/mesas", dto);
    }
}
