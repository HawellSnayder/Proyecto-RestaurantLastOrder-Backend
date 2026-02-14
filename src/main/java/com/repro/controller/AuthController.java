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
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request
    ) {

        // 1️⃣ Autenticar credenciales
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            System.out.println("¡Autenticación exitosa!");
        } catch (Exception e) {
            System.out.println("Error de autenticación: " + e.getMessage());
            return ResponseEntity.status(401).build();
        }

        // 2️⃣ Obtener usuario desde BD
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3️⃣ Generar token
        String token = jwtService.generateToken(
                usuario.getUsername(),
                usuario.getRol().getNombre()
        );

        // 4️⃣ Construir respuesta
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setUsername(usuario.getUsername());
        response.setRol(usuario.getRol().getNombre());

        return ResponseEntity.ok(response);
    }
}

