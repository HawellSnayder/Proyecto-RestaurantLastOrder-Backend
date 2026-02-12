package com.repro.repository;

import com.repro.model.Enum.EstadoMesa;
import com.repro.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    Optional<Mesa> findByNumero(Integer numero);
    boolean existsByNumero(Integer numero);
    List<Mesa> findByEstado(EstadoMesa estado);
}
