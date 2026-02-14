package com.repro.controller;

import com.repro.dto.usuario.CrearUsuarioRequestDTO;
import com.repro.dto.usuario.UsuarioResponseDTO;
import com.repro.model.Usuario;
import com.repro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(
            @RequestBody CrearUsuarioRequestDTO dto
    ) {
        Usuario usuario = usuarioService.crear(dto);
        return ResponseEntity.ok(UsuarioResponseDTO.from(usuario));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioResponseDTO>> listarActivos() {

        List<UsuarioResponseDTO> usuarios = usuarioService.listarActivos()
                .stream()
                .map(UsuarioResponseDTO::from)
                .toList();

        return ResponseEntity.ok(usuarios);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioService.desactivar(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> obtenerActual() {
        Usuario usuario = usuarioService.obtenerActual();
        return ResponseEntity.ok(UsuarioResponseDTO.from(usuario));
    }
}
