package com.mingeso.topeducation.repositories;

import com.mingeso.topeducation.entities.TipoColegio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoColegioRepository extends JpaRepository<TipoColegio, Integer> {
}
