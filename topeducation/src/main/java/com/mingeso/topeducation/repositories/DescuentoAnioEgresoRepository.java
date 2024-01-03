package com.mingeso.topeducation.repositories;

import com.mingeso.topeducation.entities.DescuentoAnioEgreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DescuentoAnioEgresoRepository extends JpaRepository<DescuentoAnioEgreso, Integer> {
    @Query(value = "SELECT d.porcentaje_descuento " +
            "FROM descuento_anio_egreso d " +
            "WHERE " +
            "   (SELECT EXTRACT(YEAR FROM CURDATE()) - :anio_egreso - 1) " +
            "   BETWEEN d.cantidad_anios_egreso_inferior AND d.cantidad_anios_egreso_superior", nativeQuery = true)
    Integer findDescuentoByAnioEgreso(@Param("anio_egreso") Integer anioEgreso);
}
