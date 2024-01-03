package com.mingeso.topeducation.repositories;

import com.mingeso.topeducation.entities.Razon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface RazonRepository extends JpaRepository<Razon, Integer> {
    @Query("SELECT r from Razon r where r.estudiante.rut = :rut")
    List<Razon> findAllByRut(@Param("rut") String rut);

    @Query("SELECT r " +
            "FROM Razon r " +
            "WHERE (r.fechaInicio <= :fecha_inicio_proceso) " +
            "AND (r.estado.id = 2 OR r.estado.id = 1)" +
            "AND r.estudiante.rut = :rut")
    List<Razon> findCuotasAPagarByRutAndDate(@Param("rut") String rut,
                                                  @Param("fecha_inicio_proceso") LocalDate fechaInicioProceso);

    @Query("SELECT r " +
            "FROM Razon r " +
            "WHERE (r.estado.id = 2 OR r.estado.id = 1)" +
            "AND r.estudiante.rut = :rut")
    List<Razon> findCuotasPendientesAndAtrasadasByRut(@Param("rut") String rut);
}
