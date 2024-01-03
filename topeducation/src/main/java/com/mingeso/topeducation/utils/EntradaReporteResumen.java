package com.mingeso.topeducation.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
public class EntradaReporteResumen {
    String rut;
    Integer numeroExamenesRendidos;
    Integer promedioExamenes;
    Integer totalArancel;
    String tipoPago;
    Integer numeroCuotasPactadas;
    Integer numeroCuotasPagadas;
    Integer arancelPagado;
    Integer totalPagado;
    LocalDate fechaUltimoPago;
    Integer saldoArancelPendiente;
    Integer saldoTotalPendiente;
    Integer numeroCuotasAtrasadas;
}
