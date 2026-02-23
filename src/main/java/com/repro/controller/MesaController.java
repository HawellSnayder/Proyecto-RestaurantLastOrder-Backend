package com.repro.controller;

import com.repro.dto.mesa.MesaRequestDTO;
import com.repro.dto.mesa.MesaResponseDTO;
import com.repro.model.Mesa;
import com.repro.service.MesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MesaController {

    private final MesaService mesaService;

    @PostMapping
    public ResponseEntity<MesaResponseDTO> crear(
            @RequestBody MesaRequestDTO dto
    ) {

        Mesa mesa = mesaService.crear(dto);
        return ResponseEntity.ok(convertToResponse(mesa));
    }

    @GetMapping("/libres")
    public ResponseEntity<List<MesaResponseDTO>> listarLibres() {

        List<MesaResponseDTO> mesas = mesaService.listarLibres()
                .stream()
                .map(this::convertToResponse)
                .toList();

        return ResponseEntity.ok(mesas);
    }

    @GetMapping
    public ResponseEntity<List<MesaResponseDTO>> listarTodas() {

        List<MesaResponseDTO> mesas = mesaService.listarTodas()
                .stream()
                .map(this::convertToResponse)
                .toList();

        return ResponseEntity.ok(mesas);
    }

    @PatchMapping("/{id}/reservar")
    public ResponseEntity<Void> reservar(@PathVariable Long id) {
        mesaService.reservar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ocupar")
    public ResponseEntity<Void> ocupar(@PathVariable Long id) {
        mesaService.ocupar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/liberar")
    public ResponseEntity<Void> liberar(@PathVariable Long id) {
        mesaService.liberar(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mesaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<MesaResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody MesaRequestDTO dto
    ) {
        Mesa mesa = mesaService.actualizar(id, dto);
        return ResponseEntity.ok(convertToResponse(mesa));
    }


    private MesaResponseDTO convertToResponse(Mesa mesa) {

        MesaResponseDTO dto = new MesaResponseDTO();
        dto.setId(mesa.getId());
        dto.setNumero(mesa.getNumero());
        dto.setCapacidad(mesa.getCapacidad());
        dto.setEstado(mesa.getEstado().name());

        return dto;
    }
}



