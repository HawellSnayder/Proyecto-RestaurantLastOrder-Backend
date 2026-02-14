package com.repro.controller;

import com.repro.dto.plato.PlatoRequestDTO;
import com.repro.dto.plato.PlatoResponseDTO;
import com.repro.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platos")
@RequiredArgsConstructor
public class PlatoController {

    private final PlatoService platoService;


    @GetMapping("/disponibles")
    public ResponseEntity<List<PlatoResponseDTO>> listarDisponibles() {
        return ResponseEntity.ok(platoService.listarDisponibles());
    }


    @PostMapping
    public ResponseEntity<PlatoResponseDTO> crear(@RequestBody PlatoRequestDTO dto) {
        return ResponseEntity.ok(platoService.crear(dto));
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
}

