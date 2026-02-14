package com.repro.service.imp;

import com.repro.dto.plato.PlatoRequestDTO;
import com.repro.dto.plato.PlatoResponseDTO;
import com.repro.dto.plato.PlatoSocketDTO;
import com.repro.model.CategoriaPlato;
import com.repro.model.Enum.EventoPlato;
import com.repro.model.Plato;
import com.repro.repository.CategoriaPlatoRepository;
import com.repro.repository.PlatoRepository;
import com.repro.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlatoServiceImpl implements PlatoService {

    private final PlatoRepository repository;
    private final CategoriaPlatoRepository categoriaPlatoRepository;
    private final SimpMessagingTemplate messagingTemplate;



    @Override
    public List<PlatoResponseDTO> listarDisponibles() {
        return repository.findByDisponibleTrue()
                .stream()
                .map(PlatoResponseDTO::from)
                .toList();
    }

    @Override
    @Transactional
    public PlatoResponseDTO crear(PlatoRequestDTO dto) {

        CategoriaPlato categoria = categoriaPlatoRepository
                .findById(dto.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        if (!categoria.getActivo()) {
            throw new IllegalStateException("La categoría está desactivada");
        }

        Plato plato = new Plato();
        plato.setNombre(dto.getNombre());
        plato.setPrecio(dto.getPrecio());
        plato.setCategoria(categoria);
        plato.setDisponible(true);

        Plato guardado = repository.save(plato);

        enviarEvento(guardado, EventoPlato.CREADO);

        return PlatoResponseDTO.from(guardado);
    }

    @Override
    @Transactional
    public PlatoResponseDTO actualizar(Long id, PlatoRequestDTO dto) {

        Plato plato = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Plato no encontrado")
                );

        CategoriaPlato categoria = categoriaPlatoRepository
                .findById(dto.getCategoriaId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Categoría no encontrada")
                );

        plato.setNombre(dto.getNombre());
        plato.setPrecio(dto.getPrecio());
        plato.setCategoria(categoria);

        return PlatoResponseDTO.from(plato);
    }

    @Override
    @Transactional
    public void desactivar(Long id) {

        Plato plato = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado"));

        plato.setDisponible(false);

        enviarEvento(plato, EventoPlato.DISPONIBILIDAD_CAMBIADA);
    }

    @Transactional
    public void cambiarDisponibilidad(Long id, Boolean disponible) {

        Plato plato = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado"));

        plato.setDisponible(disponible);

        enviarEvento(plato, EventoPlato.DISPONIBILIDAD_CAMBIADA);
    }


    private void enviarEvento(Plato plato, EventoPlato evento) {

        PlatoSocketDTO dto = new PlatoSocketDTO();
        dto.setId(plato.getId());
        dto.setNombre(plato.getNombre());
        dto.setPrecio(plato.getPrecio());
        dto.setCategoriaId(plato.getCategoria().getId());
        dto.setCategoriaNombre(plato.getCategoria().getNombre());
        dto.setDisponible(plato.getDisponible());
        dto.setEvento(evento);

        messagingTemplate.convertAndSend("/topic/platos", dto);
    }

}

