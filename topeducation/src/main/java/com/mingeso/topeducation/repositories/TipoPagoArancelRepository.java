package com.mingeso.topeducation.repositories;

import com.mingeso.topeducation.entities.TipoPagoArancel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPagoArancelRepository extends JpaRepository<TipoPagoArancel, Integer> {
}
