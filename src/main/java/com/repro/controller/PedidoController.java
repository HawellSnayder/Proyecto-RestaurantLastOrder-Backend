package com.repro.controller;

import com.repro.dto.pedido.PedidoRequestDTO;
import com.repro.dto.pedido.PedidoResponseDTO;
import com.repro.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200",methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE})
public class PedidoController {

    private final PedidoService pedidoService;


    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crear(
            @RequestBody PedidoRequestDTO dto
    ) {
        return ResponseEntity.ok(
                pedidoService.crearPedido(dto)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> editar(
            @PathVariable("id") Long id,
            @RequestBody PedidoRequestDTO dto
    ) {
        return ResponseEntity.ok(
                pedidoService.editarPedido(id, dto)
        );
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado
    ) {
        return ResponseEntity.ok(
                pedidoService.cambiarEstado(id, estado)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                pedidoService.obtenerPorId(id)
        );
    }
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar(
            @RequestParam(required = false) String estado // Lo hacemos opcional
    ) {
        if (estado != null && !estado.isEmpty()) {
            return ResponseEntity.ok(pedidoService.listarPorEstado(estado));
        }
        // Necesitas tener este método en tu PedidoService para traer TODOS
        return ResponseEntity.ok(pedidoService.listarTodos());
    }
}


