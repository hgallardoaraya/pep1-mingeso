package com.mingeso.topeducation.repositories;

import com.mingeso.topeducation.entities.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
    @Query("select e from Estudiante e where e.rut = :rut")
    Optional<Estudiante> findByRut(@Param("rut") String rut);
}
