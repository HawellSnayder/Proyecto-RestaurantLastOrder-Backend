package com.repro.dto.mesa;

import com.repro.model.Enum.EstadoMesa;
import com.repro.model.Enum.EventoMesa;
import lombok.Data;

@Data
public class MesaSocketDTO {

    private Long id;
    private Integer numero;
    private Integer capacidad;
    private EstadoMesa estado;
    private EventoMesa evento;
}

