package com.repro.service.imp;

import com.repro.model.Enum.EstadoMesa;
import com.repro.model.Mesa;
import com.repro.repository.MesaRepository;
import com.repro.service.MesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MesaServiceImpl implements MesaService {

    private final MesaRepository mesaRepository;

    @Override
    @Transactional
    public Mesa crear(Integer numero, Integer capacidad) {

        if (mesaRepository.existsByNumero(numero)) {
            throw new IllegalArgumentException("La mesa ya existe");
        }

        Mesa mesa = new Mesa();
        mesa.setNumero(numero);
        mesa.setCapacidad(capacidad);
        mesa.setEstado(EstadoMesa.LIBRE); // ✅ CORRECTO

        return mesaRepository.save(mesa);
    }

    @Override
    public List<Mesa> listarLibres() {
        return mesaRepository.findByEstado(EstadoMesa.LIBRE);
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
    }
}



