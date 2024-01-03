package com.mingeso.topeducation.repositories;

import com.mingeso.topeducation.entities.TipoRazon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoRazonRepository extends JpaRepository<TipoRazon, Integer> {
}
