package com.repro.controller;

import com.repro.dto.categoria.CategoriaRequestDTO;
import com.repro.dto.categoria.CategoriaResponseDTO;
import com.repro.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaResponseDTO>> listarActivas() {
        return ResponseEntity.ok(categoriaService.listarActivas());
    }


    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }


    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crear(
            @RequestBody CategoriaRequestDTO dto
    ) {
        return ResponseEntity.ok(categoriaService.crear(dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody CategoriaRequestDTO dto
    ) {
        return ResponseEntity.ok(categoriaService.actualizar(id, dto));
    }


    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Boolean activo
    ) {
        categoriaService.cambiarEstado(id, activo);
        return ResponseEntity.ok().build();
    }
}

