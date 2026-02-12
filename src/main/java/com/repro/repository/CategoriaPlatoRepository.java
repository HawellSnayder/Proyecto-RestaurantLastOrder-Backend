package com.repro.repository;

import com.repro.model.CategoriaPlato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaPlatoRepository extends JpaRepository<CategoriaPlato, Long> {

    List<CategoriaPlato> findByActivoTrue();
}
