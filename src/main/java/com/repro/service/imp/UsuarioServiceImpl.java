package com.repro.service.imp;

import com.repro.dto.usuario.CrearUsuarioRequestDTO;
import com.repro.dto.usuario.UsuarioResponseDTO;
import com.repro.model.Rol;
import com.repro.model.Usuario;
import com.repro.repository.RolRepository;
import com.repro.repository.UsuarioRepository;
import com.repro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public Usuario crear(CrearUsuarioRequestDTO dto) {

        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(rol);
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> listarActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        messagingTemplate.convertAndSend("/topic/usuarios-desactivados", id);
        messagingTemplate.convertAndSend("/topic/logout/" + usuario.getUsername(),
                "Tu cuenta ha sido desactivada por el administrador.");
    }

    @Override
    public Usuario obtenerActual() {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No hay usuario autenticado");
        }

        return usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
    }
    @Override
    @Transactional
    public void activar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setActivo(true);
        usuarioRepository.save(usuario);
        messagingTemplate.convertAndSend("/topic/usuarios-desactivados", id);
    }
    @Override
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::from)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        messagingTemplate.convertAndSend("/topic/logout/" + usuario.getUsername(),
                "Tu cuenta ha sido eliminada por un administrador.");

        messagingTemplate.convertAndSend("/topic/usuarios-desactivados", id);
        usuarioRepository.delete(usuario);
    }
}


