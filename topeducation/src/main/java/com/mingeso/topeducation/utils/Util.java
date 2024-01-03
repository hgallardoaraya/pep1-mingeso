package com.mingeso.topeducation.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.DayOfWeek;
import java.time.LocalDate;

@NoArgsConstructor
@Data
public class Util {
    public static LocalDate obtenerFechaInicioProceso(LocalDate fechaActual){
        return fechaActual.getDayOfMonth() < 11 ?
                LocalDate.of(fechaActual.getYear(), fechaActual.getMonth(), 5)
                :
                LocalDate.of(fechaActual.getYear(), fechaActual.getMonth().plus(1), 5);
    }

    public static LocalDate obtenerFechaFinProceso(){
        LocalDate fechaActual = LocalDate.now();

        return fechaActual.getDayOfMonth() < 11 ?
                LocalDate.of(fechaActual.getYear(), fechaActual.getMonth(), 10)
                :
                LocalDate.of(fechaActual.getYear(), fechaActual.getMonth().plus(1), 10);
    }

    //Ultimo viernes de cada mes antes del próximo proceso de pago de arancel.
    public static LocalDate obtenerFechaExamenesProceso(LocalDate fechaInicioPago){
        // Obtén el primer día del mes anterior
        LocalDate primerDiaMesAnterior = fechaInicioPago.minusMonths(1).withDayOfMonth(1);

        // Busca el último viernes en el mes anterior
        LocalDate ultimoViernesMesAnterior = primerDiaMesAnterior;
        while (ultimoViernesMesAnterior.getMonth() == primerDiaMesAnterior.getMonth()) {
            if (ultimoViernesMesAnterior.getDayOfWeek() == DayOfWeek.FRIDAY) {
                primerDiaMesAnterior = ultimoViernesMesAnterior;
            }
            ultimoViernesMesAnterior = ultimoViernesMesAnterior.plusDays(1);
        }

        return primerDiaMesAnterior;
    }
}
