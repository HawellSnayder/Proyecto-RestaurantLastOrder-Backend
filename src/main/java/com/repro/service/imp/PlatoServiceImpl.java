package com.repro.service.imp;

import com.repro.dto.plato.PlatoRequestDTO;
import com.repro.dto.plato.PlatoResponseDTO;
import com.repro.model.CategoriaPlato;
import com.repro.model.Plato;
import com.repro.repository.CategoriaPlatoRepository;
import com.repro.repository.PlatoRepository;
import com.repro.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlatoServiceImpl implements PlatoService {

    private final PlatoRepository repository;
    private final CategoriaPlatoRepository categoriaPlatoRepository;


    @Override
    public List<PlatoResponseDTO> listarDisponibles() {
        return repository.findByActivoTrue()
                .stream()
                .map(PlatoResponseDTO::from)
                .toList();
    }

    @Override
    @Transactional
    public PlatoResponseDTO crear(PlatoRequestDTO dto) {

        CategoriaPlato categoria = categoriaPlatoRepository
                .findById(dto.getCategoriaId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Categoría no encontrada")
                );


        Plato plato = new Plato();
        plato.setNombre(dto.getNombre());
        plato.setPrecio(dto.getPrecio());
        plato.setCategoria(categoria);
        plato.setDisponible(true);

        return PlatoResponseDTO.from(
                repository.save(plato)
        );
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
        Plato plato = repository.findById(id).orElseThrow();
        plato.setDisponible(false);
    }

}

