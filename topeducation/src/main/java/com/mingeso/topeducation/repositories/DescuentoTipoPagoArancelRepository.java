package com.mingeso.topeducation.repositories;

import com.mingeso.topeducation.entities.DescuentoTipoPagoArancel;
import com.mingeso.topeducation.entities.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DescuentoTipoPagoArancelRepository extends JpaRepository<DescuentoTipoPagoArancel, Integer> {
    @Query("select d.porcentajeDescuento from DescuentoTipoPagoArancel d where d.tipoPagoArancel.id = :id_tipo_pago_arancel")
    Integer findDescuentoByIdTipoPagoArancel(@Param("id_tipo_pago_arancel") Integer idTipoPagoArancel);
}
