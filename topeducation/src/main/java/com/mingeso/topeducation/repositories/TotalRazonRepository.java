package com.mingeso.topeducation.repositories;

import com.mingeso.topeducation.entities.Estudiante;
import com.mingeso.topeducation.entities.TotalRazon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalRazonRepository extends JpaRepository<TotalRazon, Integer> {

    @Query("select t.total from TotalRazon t where t.tipo.tipo = :tipo_razon ")
    Integer findTotalByTipoRazon(@Param("tipo_razon") String tipoRazon);

}
