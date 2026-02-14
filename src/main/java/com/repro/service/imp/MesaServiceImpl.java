package com.repro.service.imp;

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
    public Mesa crear(Integer numero, Integer capacidad) {

        if (mesaRepository.existsByNumero(numero)) {
            throw new IllegalArgumentException("La mesa ya existe");
        }

        Mesa mesa = new Mesa();
        mesa.setNumero(numero);
        mesa.setCapacidad(capacidad);
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
