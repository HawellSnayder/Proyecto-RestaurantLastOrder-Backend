package com.repro.service.imp;

import com.repro.dto.categoria.CategoriaRequestDTO;
import com.repro.dto.categoria.CategoriaResponseDTO;
import com.repro.model.CategoriaPlato;
import com.repro.repository.CategoriaPlatoRepository;
import com.repro.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaPlatoRepository repository;

    @Override
    @Transactional
    public CategoriaResponseDTO crear(CategoriaRequestDTO dto) {

        CategoriaPlato categoria = new CategoriaPlato();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setActivo(true);

        return CategoriaResponseDTO.from(repository.save(categoria));
    }

    @Override
    @Transactional
    public CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO dto) {

        CategoriaPlato categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        return CategoriaResponseDTO.from(categoria);
    }

    @Override
    public List<CategoriaResponseDTO> listarActivas() {
        return repository.findByActivoTrue()
                .stream()
                .map(CategoriaResponseDTO::from)
                .toList();
    }

    @Override
    public List<CategoriaResponseDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(CategoriaResponseDTO::from)
                .toList();
    }

    @Override
    @Transactional
    public void desactivar(Long id) {

        CategoriaPlato categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        categoria.setActivo(false);
    }
}

