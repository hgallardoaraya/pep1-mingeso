package com.mingeso.topeducation.repositories;

import com.mingeso.topeducation.entities.InteresMesesAtraso;
import com.mingeso.topeducation.entities.Razon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteresMesesAtrasoRepository extends JpaRepository<InteresMesesAtraso, Integer> {
    @Query("SELECT IF((:meses_atraso > (SELECT MAX(i.mesesAtraso) FROM InteresMesesAtraso i)), " +
            "(SELECT MAX(i.porcentajeInteres) FROM InteresMesesAtraso i), " +
            "(SELECT i.porcentajeInteres FROM InteresMesesAtraso i WHERE i.mesesAtraso = :meses_atraso))")
    Integer findInteresByMesesAtraso(@Param("meses_atraso") Integer mesesAtraso);
}
