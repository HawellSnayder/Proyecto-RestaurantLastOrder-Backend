package com.repro.controller;

import com.repro.dto.auth.LoginRequestDTO;
import com.repro.dto.auth.LoginResponseDTO;
import com.repro.model.Usuario;
import com.repro.repository.UsuarioRepository;
import com.repro.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            return ResponseEntity.status(403).body("Tu cuenta ha sido desactivada. Contacta al administrador.");
        }

        String token = jwtService.generateToken(
                usuario.getUsername(),
                usuario.getRol().getNombre()
        );

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setUsername(usuario.getUsername());
        response.setNombre(usuario.getNombre());
        response.setRol(usuario.getRol().getNombre());

        return ResponseEntity.ok(response);
    }
}

