package com.mingeso.topeducation.repositories;

import com.mingeso.topeducation.entities.MaxCuotasTipoColegio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaxCuotasTipoColegioRepository extends JpaRepository<MaxCuotasTipoColegio, Integer> {
    @Query("select m.maxCuotas from MaxCuotasTipoColegio m where m.tipoColegio.id = :id_tipo_colegio")
    Integer findMaxCuotasByTipoColegio(@Param("id_tipo_colegio") Integer idTipoColegio);
}
