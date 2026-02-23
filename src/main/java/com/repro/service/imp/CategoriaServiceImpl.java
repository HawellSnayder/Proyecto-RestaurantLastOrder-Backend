package com.repro.service.imp;

import com.repro.dto.categoria.CategoriaRequestDTO;
import com.repro.dto.categoria.CategoriaResponseDTO;
import com.repro.dto.categoria.CategoriaSocketDTO;
import com.repro.model.CategoriaPlato;
import com.repro.model.Enum.EventoCategoria;
import com.repro.repository.CategoriaPlatoRepository;
import com.repro.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaPlatoRepository repository;
    private final SimpMessagingTemplate messagingTemplate;


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

        enviarEventoCategoria(categoria, EventoCategoria.DESACTIVADA);
        messagingTemplate.convertAndSend("/topic/categorias/desactivada", categoria.getId());
    }
    @Transactional
    public void cambiarEstado(Long id, Boolean activo) {

        CategoriaPlato categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        categoria.setActivo(activo);

        enviarEventoCategoria(categoria,
                activo ? EventoCategoria.ACTIVADA : EventoCategoria.DESACTIVADA);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        CategoriaPlato categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));

        try {
            repository.delete(categoria);
            repository.flush();

            CategoriaSocketDTO dto = new CategoriaSocketDTO();
            dto.setId(id);
            dto.setNombre(categoria.getNombre());
            dto.setActivo(false);
            dto.setEvento(EventoCategoria.DESACTIVADA);

            messagingTemplate.convertAndSend("/topic/categorias", dto);

            System.out.println("🗑️ Categoría eliminada y notificación enviada: " + id);

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new RuntimeException("No se puede eliminar: La categoría tiene platos asociados. " +
                    "Desactívala en su lugar para ocultarla del menú.");
        }
    }

    private void enviarEventoCategoria(CategoriaPlato categoria, EventoCategoria evento) {

        CategoriaSocketDTO dto = new CategoriaSocketDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setActivo(categoria.getActivo());
        dto.setEvento(evento);

        messagingTemplate.convertAndSend("/topic/categorias", dto);
    }
}

