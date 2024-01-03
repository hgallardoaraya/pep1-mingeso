package com.mingeso.topeducation.repositories;

import com.mingeso.topeducation.entities.EstadoRazon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRazonRepository extends JpaRepository<EstadoRazon, Integer> {
}
