package com.repro.controller;

import com.repro.dto.plato.PlatoRequestDTO;
import com.repro.dto.plato.PlatoResponseDTO;
import com.repro.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/platos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PlatoController {

    private final PlatoService platoService;

    @GetMapping
    public ResponseEntity<List<PlatoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(platoService.listarTodos());
    }
    @GetMapping("/disponibles")
    public ResponseEntity<List<PlatoResponseDTO>> listarDisponibles() {
        return ResponseEntity.ok(platoService.listarDisponibles());
    }


    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<PlatoResponseDTO> crear(
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("categoriaId") Long categoriaId,
            @RequestParam("imagen") MultipartFile imagen
    ) throws IOException {
        PlatoRequestDTO dto = new PlatoRequestDTO();
        dto.setNombre(nombre);
        dto.setPrecio(precio);
        dto.setCategoriaId(categoriaId);

        return ResponseEntity.ok(platoService.crearConImagen(dto, imagen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody PlatoRequestDTO dto
    ) {
        return ResponseEntity.ok(platoService.actualizar(id, dto));
    }


    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<Void> cambiarDisponibilidad(
            @PathVariable Long id,
            @RequestParam Boolean disponible
    ) {
        platoService.cambiarDisponibilidad(id, disponible);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        platoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

