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
@CrossOrigin(origins = "*")
public class MesaController {

    private final MesaService mesaService;

    // =========================
    // CREAR
    // =========================
    @PostMapping
    public ResponseEntity<MesaResponseDTO> crear(
            @RequestBody MesaRequestDTO dto
    ) {

        Mesa mesa = mesaService.crear(dto.getNumero(), dto.getCapacidad());
        return ResponseEntity.ok(convertToResponse(mesa));
    }

    // =========================
    // LISTAR LIBRES
    // =========================
    @GetMapping("/libres")
    public ResponseEntity<List<MesaResponseDTO>> listarLibres() {

        List<MesaResponseDTO> mesas = mesaService.listarLibres()
                .stream()
                .map(this::convertToResponse)
                .toList();

        return ResponseEntity.ok(mesas);
    }

    // =========================
    // LISTAR TODAS
    // =========================
    @GetMapping
    public ResponseEntity<List<MesaResponseDTO>> listarTodas() {

        List<MesaResponseDTO> mesas = mesaService.listarTodas()
                .stream()
                .map(this::convertToResponse)
                .toList();

        return ResponseEntity.ok(mesas);
    }

    // =========================
    // RESERVAR
    // =========================
    @PatchMapping("/{id}/reservar")
    public ResponseEntity<Void> reservar(@PathVariable Long id) {
        mesaService.reservar(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // OCUPAR (solo si decides exponerlo)
    // =========================
    @PatchMapping("/{id}/ocupar")
    public ResponseEntity<Void> ocupar(@PathVariable Long id) {
        mesaService.ocupar(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // LIBERAR (solo si decides exponerlo)
    // =========================
    @PatchMapping("/{id}/liberar")
    public ResponseEntity<Void> liberar(@PathVariable Long id) {
        mesaService.liberar(id);
        return ResponseEntity.noContent().build();
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



