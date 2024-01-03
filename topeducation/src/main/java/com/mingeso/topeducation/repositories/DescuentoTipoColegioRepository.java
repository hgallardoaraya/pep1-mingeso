package com.mingeso.topeducation.repositories;

import com.mingeso.topeducation.entities.DescuentoTipoColegio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DescuentoTipoColegioRepository extends JpaRepository<DescuentoTipoColegio, Integer> {

    @Query("select d.porcentajeDescuento from DescuentoTipoColegio d where d.tipoColegio.id = :id_tipo_colegio")
    Integer findDescuentoByIdTipoColegio(@Param("id_tipo_colegio") Integer idTipoColegio);
}
