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
@CrossOrigin(origins = "http://localhost:4200")
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
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable Long id) {
        usuarioService.activar(id); // Debes crear este método en el Service
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id); // Implementar en el Service
        return ResponseEntity.noContent().build();
    }

    // Opcional: Para el Admin, necesitas ver TODOS, no solo activos
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
}
